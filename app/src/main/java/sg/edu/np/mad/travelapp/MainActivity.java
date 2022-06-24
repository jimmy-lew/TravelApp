package sg.edu.np.mad.travelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Granularity;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import org.json.JSONException;

import java.util.ArrayList;

import sg.edu.np.mad.travelapp.data.model.BusStop;
import sg.edu.np.mad.travelapp.data.model.User;
import sg.edu.np.mad.travelapp.data.repository.BusStopRepository;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ArrayList<String> query;
    private BusTimingCardAdapter nearbyAdapter = new BusTimingCardAdapter();
    private BusTimingCardAdapter favouritesAdapter = new BusTimingCardAdapter();
    private Location userLocation = new Location("");
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Initialize the SDK
        Places.initialize(getApplicationContext(), GetAPIKey());

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);

        CardView homeOutCardView = findViewById(R.id.homeOutCardView);
        CardView homeInCardView = findViewById(R.id.homeInCardView);

        ImageView favIcon = findViewById(R.id.favIcon);
        ImageView homeIcon = findViewById(R.id.homeIcon);
        ImageView nearbyIcon = findViewById(R.id.nearbyIcon);

        ImageButton searchButton = findViewById(R.id.mainSearchButton);
        EditText searchTextBox = findViewById(R.id.mainSearchTextbox);


        //TODO: Need add drop shadow for navbar
        homeOutCardView.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"));
        homeInCardView.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"));
        homeIcon.setImageResource(R.drawable.home_active);

        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        // Use the builder to create a FindAutocompletePredictionsRequest.
        FindAutocompletePredictionsRequest predictRequest = FindAutocompletePredictionsRequest.builder()
            .setCountries("SG")
            .setTypeFilter(TypeFilter.ADDRESS)
            .setSessionToken(token)
            .setQuery(searchTextBox.getText().toString())
            .build();

        placesClient.findAutocompletePredictions(predictRequest).addOnSuccessListener((response) -> {
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {

                Log.i(TAG, prediction.getPlaceId());
                Log.i(TAG, prediction.getPrimaryText(null).toString());
            }
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Log.e(TAG, "Place not found: " + apiException.getStatusCode());
            }
        });

        FusedLocationProviderClient fusedLocationClient;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        CurrentLocationRequest request = new CurrentLocationRequest.Builder()
                .setDurationMillis(Long.MAX_VALUE)
                .setGranularity(Granularity.GRANULARITY_FINE)
                .setMaxUpdateAgeMillis(0)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .build();

        renderUI(nearbyAdapter, findViewById(R.id.nearbyRecyclerView));
        renderUI(favouritesAdapter, findViewById(R.id.favouriteStopsRecyclerView));

        fusedLocationClient.getCurrentLocation(request, null).addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null)
                {
                    userLocation = location;
                    BusStopRepository.get_instance().getNearbyBusStops(location, busStopList -> {
                        nearbyAdapter.setBusStopList(busStopList);
                        nearbyAdapter.notifyDataSetChanged();
                    });
                }
            }
        });

        ref.child("1").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    User user = task.getResult().getValue(User.class);
                    nearbyAdapter.setUser(user);
                    nearbyAdapter.notifyDataSetChanged();
                    query = user.getFavouritesList();
                    BusStopRepository.get_instance().getBusStopsByName(query, busStopList -> {
                        favouritesAdapter.setUser(user);
                        favouritesAdapter.setBusStopList(busStopList);
                        favouritesAdapter.notifyDataSetChanged();
                    });
                }
            }
        });

        searchButton.setOnClickListener(view -> {
            String searchQuery = searchTextBox.getText().toString();
            Intent SearchBusStop = new Intent(getApplicationContext(), SearchBusStop.class);
            SearchBusStop.putExtra("query", searchQuery);
            SearchBusStop.putExtra("location", userLocation);
            startActivity(SearchBusStop);
        });

        // --- Autocomplete Suggestions ---
        String [] suggestions = {"airplane", "bird", "chicken"};
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.mainSearchTextbox);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, suggestions);
        autoCompleteTextView.setAdapter(arrayAdapter);

        // --- Search Debounce ---
        long delay = 1000; // 1 seconds after user stops typing
        long last_text_edit = 0;
        Handler handler = new Handler();

        Runnable CheckInputFinish = new Runnable() {
            public void run() {
                if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                    // TODO: do what you need here
                    Log.v(TAG, "Stopped");
                }
            }
        };

        searchTextBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                //You need to remove this to run only once
                handler.removeCallbacks(CheckInputFinish);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //avoid triggering event when text is empty
                if (s.length() > 0) {
                    long last_text_edit = System.currentTimeMillis();
                    handler.postDelayed(CheckInputFinish, delay);
                }
            }
        });

        nearbyIcon.setOnClickListener(view -> {
            Intent ViewBusStops = new Intent(getApplicationContext(), ViewBusStops.class);
            ViewBusStops.putExtra("location", userLocation);
            startActivity(ViewBusStops);
        });

        favIcon.setOnClickListener(view -> {
            Intent ViewFavourites = new Intent(getApplicationContext(), ViewFavourites.class);
            ViewFavourites.putExtra("location", userLocation);
            startActivity(ViewFavourites);
        });
    }

    public void renderUI(BusTimingCardAdapter adapter, RecyclerView recycler){
        this.runOnUiThread(() -> {
            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    getApplicationContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
            );
            recycler.setLayoutManager(layoutManager);
            recycler.setAdapter(adapter);
            recycler.setAdapter(adapter);
        });
    }

    // --- Get API KEY from local.properties/AndroidManifest ---
    public String GetAPIKey(){
        try{
            ApplicationInfo info = this.getApplicationContext()
                    .getPackageManager()
                    .getApplicationInfo(this.getApplicationContext().getPackageName(),
                            PackageManager.GET_META_DATA);
            String key = info.metaData.get("MAP_KEY").toString();
            return key;
        }
        catch(Exception e){
            Log.e("API Key", e.getMessage());
            return null;
        }
    }

}