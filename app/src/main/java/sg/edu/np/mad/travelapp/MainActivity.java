package sg.edu.np.mad.travelapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sg.edu.np.mad.travelapp.data.model.User;
import sg.edu.np.mad.travelapp.data.repository.BusStopRepository;
import sg.edu.np.mad.travelapp.ui.BaseActivity;

public class MainActivity extends BaseActivity {
    private final BusTimingCardAdapter nearbyAdapter = new BusTimingCardAdapter(BUS_STOP_REPO.getNearbyCache());
    private final BusTimingCardAdapter favouriteStopsAdapter = new BusTimingCardAdapter(BUS_STOP_REPO.getFavouritesCache());
    private final RouteAdapter favouriteRoutesAdapter = new RouteAdapter();

    private Location userLocation;
    private ArrayList<String> query;

    private FirebaseAuth mAuth;

    @SuppressLint({"MissingPermission", "NotifyDataSetChanged"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton searchButton = findViewById(R.id.mainSearchButton);

        initializeRecycler(nearbyAdapter, findViewById(R.id.nearbyRecyclerView), true);
        initializeRecycler(favouriteStopsAdapter, findViewById(R.id.favouriteStopsRecyclerView), true);

        /* Gets user location and passes into callback to get list of nearby bus stops, their serices, their respective timings
        and update adapter's information to display on activity*/
        getUserLocation(location -> {
            userLocation = location;
            initializeNavbar(location);
            BusStopRepository.getInstance().getNearbyBusStops(location, nearbyAdapter::setBusStopList);
        });

        /* Listens to changes in user favourites and updates adapters accordingly */
        REF.child("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                nearbyAdapter.setUser(user);
                favouriteStopsAdapter.setUser(user);

                assert user != null;

                query = user.getFavouritesList();
                BusStopRepository.getInstance().getBusStopsByName(query, favouriteStopsAdapter::setBusStopList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        // Initialize AutoCompleteTextView for Google API Places AutoComplete
        AutoCompleteTextView searchTextBox = findViewById(R.id.mainSearchTextbox);
        InitializeGoogleAC(searchTextBox, getApplicationContext(), GetAPIKey(this, "API_KEY"));

        //Intent to Login Page
        ImageView profileIcon = findViewById(R.id.mainProfilePic);
        profileIcon.setOnClickListener(view -> {
            Intent Login = new Intent(getApplicationContext(), Login.class);
            startActivity(Login);
        });

        searchButton.setOnClickListener(view -> {
            String searchQuery = searchTextBox.getText().toString();
            Intent SearchBusStop = new Intent(getApplicationContext(), Search.class);
            SearchBusStop.putExtra("query", searchQuery);
            SearchBusStop.putExtra(LOCATION, userLocation);
            startActivity(SearchBusStop);
        });
    }
}