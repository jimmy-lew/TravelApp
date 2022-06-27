package sg.edu.np.mad.travelapp;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import sg.edu.np.mad.travelapp.data.model.User;
import sg.edu.np.mad.travelapp.data.repository.BusStopRepository;
import sg.edu.np.mad.travelapp.ui.BaseActivity;

/**
 * Display list of bus stops near user
 */
public class ViewBusStops extends BaseActivity {
    private final BusTimingCardAdapter adapter = new BusTimingCardAdapter();
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bus_stops);

        location = getIntent().getParcelableExtra(LOCATION);

        /* ASYNC: Check if activity recieved a location, else get user location and display nearby bus stops */
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
            initializeNavbar(location);
            BusStopRepository.get_instance().getNearbyBusStops(location, busStopList -> {
                adapter.setBusStopList(busStopList);
                adapter.notifyDataSetChanged();
            });
        }

        initializeRecycler(adapter, findViewById(R.id.nearbyBusRecycler), false);

        /* ASYNC: Retrieve user information once & update data accordingly */
        REF.child("1").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) return;
            User user = task.getResult().getValue(User.class);
            adapter.setUser(user);
            adapter.notifyDataSetChanged();
        });
    }
}