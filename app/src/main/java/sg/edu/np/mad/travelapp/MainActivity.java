package sg.edu.np.mad.travelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Granularity;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import sg.edu.np.mad.travelapp.data.model.BusStop;
import sg.edu.np.mad.travelapp.data.model.User;
import sg.edu.np.mad.travelapp.data.repository.BusStopRepository;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = "MainActivity";
    private View decorView;
    BusTimingCardAdapter busTimingCardAdapter;
    private Location userLocation = new Location("");
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

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
        //TODO: Not sure how to remove drop shadow for inactive

        FusedLocationProviderClient fusedLocationClient;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        CurrentLocationRequest.Builder builder = new CurrentLocationRequest.Builder();
        builder.setDurationMillis(Long.MAX_VALUE);
        builder.setGranularity(Granularity.GRANULARITY_FINE);
        builder.setMaxUpdateAgeMillis(0);
        builder.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
        CurrentLocationRequest request = builder.build();

        renderUI(new ArrayList<BusStop>(), new User("1", new ArrayList<String>()));

        fusedLocationClient.getCurrentLocation(request, null).addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null)
                {
                    userLocation = location;
                    BusStopRepository.get_instance().getNearbyBusStops(location, busStopList -> {
                        busTimingCardAdapter.setBusStopList(busStopList);
                        busTimingCardAdapter.notifyDataSetChanged();
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
                    busTimingCardAdapter.setUser(user);
                    busTimingCardAdapter.notifyDataSetChanged();
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

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(visibility -> {
            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                decorView.setSystemUiVisibility(hideSystemBars());
            }
        });

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

    public void renderUI(ArrayList<BusStop> busStopList, User user){
        this.runOnUiThread(() -> {
            RecyclerView busStopRecycler = this.findViewById(R.id.nearbyRecyclerView);
            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    getApplicationContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
            );
            busTimingCardAdapter = new BusTimingCardAdapter(busStopList, user);
            busStopRecycler.setLayoutManager(layoutManager);
            busStopRecycler.setAdapter(busTimingCardAdapter);
            busStopRecycler.setAdapter(busTimingCardAdapter);
        });
    }
}