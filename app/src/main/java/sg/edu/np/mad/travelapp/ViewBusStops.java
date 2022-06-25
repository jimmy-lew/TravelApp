package sg.edu.np.mad.travelapp;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import sg.edu.np.mad.travelapp.data.model.User;
import sg.edu.np.mad.travelapp.data.repository.BusStopRepository;
import sg.edu.np.mad.travelapp.ui.BaseActivity;

public class ViewBusStops extends BaseActivity {
    private final BusTimingCardAdapter adapter = new BusTimingCardAdapter();
    private final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bus_stops);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        location = getIntent().getParcelableExtra(LOCATION);

        // TODO: Abstract error handling logic
        if (location == null) {
            getUserLocation(location -> {
                initializeNavbar(location);
                BusStopRepository.get_instance().getNearbyBusStops(location, busStopList -> {
                    adapter.setBusStopList(busStopList);
                    adapter.notifyDataSetChanged();
                });
            });
        } else {
            BusStopRepository.get_instance().getNearbyBusStops(location, busStopList -> {
                adapter.setBusStopList(busStopList);
                adapter.notifyDataSetChanged();
            });
        }

        initializeRecycler(adapter, findViewById(R.id.nearbyBusRecycler), false);

        ref.child("1").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                    return;
                }

                User user = task.getResult().getValue(User.class);
                adapter.setUser(user);
                adapter.notifyDataSetChanged();
            }
        });
    }
}