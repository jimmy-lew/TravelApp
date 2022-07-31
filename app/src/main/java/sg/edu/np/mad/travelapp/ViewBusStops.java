package sg.edu.np.mad.travelapp;

import android.location.Location;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        initializeRecycler(adapter, findViewById(R.id.nearbyBusRecycler), false);

        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mapFragmentContainerView, new MapFragment())
                .commit();

        /* ASYNC: Check if activity recieved a location, else get user location and display nearby bus stops */
        // TODO: Abstract error handling logic
        if (location == null) {
            getUserLocation(location -> {
                initializeNavbar(location);
                BUS_STOP_REPO.getNearbyBusStops(location, adapter::setBusStopList);
            });
        } else {
            initializeNavbar(location);
            BUS_STOP_REPO.getNearbyBusStops(location, adapter::setBusStopList);
        }

        /* ASYNC: Retrieve user information once & update data accordingly */
        REF.child(currentUser.getUid()).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) return;
            User user = task.getResult().getValue(User.class);
            adapter.setUser(user);
        });
    }
}