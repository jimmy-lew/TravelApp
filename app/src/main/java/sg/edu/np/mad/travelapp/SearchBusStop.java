package sg.edu.np.mad.travelapp;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import sg.edu.np.mad.travelapp.data.model.User;
import sg.edu.np.mad.travelapp.data.repository.BusStopRepository;
import sg.edu.np.mad.travelapp.ui.BaseActivity;

public class SearchBusStop extends BaseActivity {
    private final BusTimingCardAdapter adapter = new BusTimingCardAdapter();
    private ArrayList<String> query = new ArrayList<>();
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bus_stop);

        query.add(getIntent().getStringExtra("query").replace(", Singapore", ""));
        location = getIntent().getParcelableExtra(LOCATION);

        initializeNavbar(location);
        initializeRecycler(adapter, findViewById(R.id.searchedBusRecycler), false);

        REF.child("1").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) return;
            User user = task.getResult().getValue(User.class);
            adapter.setUser(user);
            REPO.getBusStopsByName(query, adapter::setBusStopList);
        });
    }
}