package sg.edu.np.mad.travelapp;

import android.location.Location;
import android.os.Bundle;

import sg.edu.np.mad.travelapp.data.model.User;
import sg.edu.np.mad.travelapp.data.repository.BusStopRepository;
import sg.edu.np.mad.travelapp.ui.BaseActivity;

/**
 * Display list of bus stops near user
 */
public class ViewBusStops extends BaseActivity {
    private final BusTimingCardAdapter adapter = new BusTimingCardAdapter(BusStopRepository.getInstance().getNearbyCache());
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bus_stops);

        location = getIntent().getParcelableExtra(LOCATION);

        initializeRecycler(adapter, findViewById(R.id.nearbyBusRecycler), false);

        /* ASYNC: Check if activity recieved a location, else get user location and display nearby bus stops */
        // TODO: Abstract error handling logic
        if (location == null) {
            getUserLocation(location -> {
                initializeNavbar(location);
                REPO.getNearbyBusStops(location, adapter::setBusStopList);
            });
        } else {
            initializeNavbar(location);
            REPO.getNearbyBusStops(location, adapter::setBusStopList);
        }

        /* ASYNC: Retrieve user information once & update data accordingly */
        REF.child("1").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) return;
            User user = task.getResult().getValue(User.class);
            adapter.setUser(user);
        });
    }
}