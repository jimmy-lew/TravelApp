package sg.edu.np.mad.travelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = "MainActivity";
    private ArrayList<String> query;
    private View decorView;
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

        ImageButton searchButton = findViewById(R.id.mainSearchButton);
        EditText searchTextBox = findViewById(R.id.mainSearchTextbox);

        /*
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
         */

        searchButton.setOnClickListener(view -> {
            String searchQuery = searchTextBox.getText().toString();
            Intent SearchBusStop = new Intent(getApplicationContext(), SearchBusStop.class);
            SearchBusStop.putExtra("query", searchQuery);
            SearchBusStop.putExtra("location", userLocation);
            startActivity(SearchBusStop);
        });

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(visibility -> {
            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                decorView.setSystemUiVisibility(hideSystemBars());
            }
        });

        Bundle bundle = new Bundle();
        bundle.putParcelable("location", userLocation);
        NavbarFragment navbarFrag = new NavbarFragment();
        navbarFrag.setArguments(bundle);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFragmentContainerView, navbarFrag).commit();
    }

    // ---- Hide System Default UI Elements (Status Bar & Navigation Bar) ----
    // Documentation : https://developer.android.com/reference/android/app/Activity >> OnWindowFocusChanged ----
    // Called when the activity gains or loses window focus, called true if focused.
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        {
            // If there is focus on the window, hide the status bar and navigation bar.
            if (hasFocus) {
                decorView.setSystemUiVisibility(hideSystemBars());
            }
        }
    }

    public int hideSystemBars() {
        // Use Bitwise Operators to combine the flags
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
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
}