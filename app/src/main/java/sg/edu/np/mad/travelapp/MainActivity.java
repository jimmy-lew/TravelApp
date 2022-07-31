package sg.edu.np.mad.travelapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import sg.edu.np.mad.travelapp.data.model.User;
import sg.edu.np.mad.travelapp.data.repository.BusStopRepository;
import sg.edu.np.mad.travelapp.ui.BaseActivity;

public class MainActivity extends BaseActivity {
    private final BusTimingCardAdapter nearbyAdapter = new BusTimingCardAdapter(BUS_STOP_REPO.getNearbyCache());
    private final BusTimingCardAdapter favouriteStopsAdapter = new BusTimingCardAdapter(BUS_STOP_REPO.getFavouritesCache());
    private final RouteAdapter favouriteRoutesAdapter = new RouteAdapter();
    private Location userLocation;
    private ArrayList<String> query;

    private final Timer myTimer = new Timer();
    private final LateNotification lateNotif = new LateNotification();

    @SuppressLint({"MissingPermission", "NotifyDataSetChanged"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Notifications */
        TimerTask myTask = new TimerTask () {
            @Override
            public void run () {
                lateNotif.CheckBusTimings(getApplicationContext());
            }
        };
        myTimer.scheduleAtFixedRate(myTask , (60 * 1000), (60 * 1000)); // Runs every 1 min

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            startActivity(new Intent(getApplicationContext(), SignUp.class));
            return;
        }

        ImageButton searchButton = findViewById(R.id.mainSearchButton);

        initializeRecycler(nearbyAdapter, findViewById(R.id.nearbyRecyclerView), true);
        initializeRecycler(favouriteStopsAdapter, findViewById(R.id.favouriteStopsRecyclerView), true);
        initializeRecycler(favouriteRoutesAdapter, findViewById(R.id.favRoutesRecyclerView), false);

        /* Gets user location and passes into callback to get list of nearby bus stops, their serices, their respective timings
        and update adapter's information to display on activity*/
        getUserLocation(location -> {
            userLocation = location;
            initializeNavbar(location);
            BusStopRepository.getInstance().getNearbyBusStops(location, nearbyAdapter::setBusStopList);
        });

        /* Listens to changes in user favourites and updates adapters accordingly */
        REF.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                assert user != null;

                nearbyAdapter.setUser(user);
                favouriteStopsAdapter.setUser(user);
                favouriteRoutesAdapter.setUser(user);

                query = user.getFavouritesList();
                favouriteRoutesAdapter.setRouteList(user.getFavouriteRoutes());
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
            Intent Search = new Intent(getApplicationContext(), Search.class);
            Search.putExtra("query", searchQuery);
            Search.putExtra(LOCATION, userLocation);
            startActivity(Search);
        });
    }
}