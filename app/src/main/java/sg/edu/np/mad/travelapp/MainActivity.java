package sg.edu.np.mad.travelapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sg.edu.np.mad.travelapp.data.model.User;
import sg.edu.np.mad.travelapp.data.repository.BusStopRepository;
import sg.edu.np.mad.travelapp.ui.BaseActivity;

public class MainActivity extends BaseActivity {
    private ArrayList<String> query;
    private final BusTimingCardAdapter nearbyAdapter = new BusTimingCardAdapter();
    private final BusTimingCardAdapter favouritesAdapter = new BusTimingCardAdapter();
    private final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
    private Location userLocation = new Location("");

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        ImageButton searchButton = findViewById(R.id.mainSearchButton);
        EditText searchTextBox = findViewById(R.id.mainSearchTextbox);

        initializeRecycler(nearbyAdapter, findViewById(R.id.nearbyRecyclerView), true);
        initializeRecycler(favouritesAdapter, findViewById(R.id.favouriteStopsRecyclerView), true);

        getUserLocation(location -> {
            userLocation = location;
            initializeNavbar(location);
            BusStopRepository.get_instance().getNearbyBusStops(location, busStopList -> {
                nearbyAdapter.setBusStopList(busStopList);
                nearbyAdapter.notifyDataSetChanged();
            });
        });

        ref.child("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                nearbyAdapter.setUser(user);
                favouritesAdapter.setUser(user);

                query = user.getFavouritesList();
                BusStopRepository.get_instance().getBusStopsByName(query, busStopList -> {
                    favouritesAdapter.setBusStopList(busStopList);
                    favouritesAdapter.notifyDataSetChanged();
                    nearbyAdapter.notifyDataSetChanged();
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        searchButton.setOnClickListener(view -> {
            String searchQuery = searchTextBox.getText().toString();
            Intent SearchBusStop = new Intent(getApplicationContext(), SearchBusStop.class);
            SearchBusStop.putExtra("query", searchQuery);
            SearchBusStop.putExtra(LOCATION, userLocation);
            startActivity(SearchBusStop);
        });
    }
}