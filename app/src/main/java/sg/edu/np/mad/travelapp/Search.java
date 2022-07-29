package sg.edu.np.mad.travelapp;

import android.location.Location;
import android.os.Bundle;

import sg.edu.np.mad.travelapp.data.model.SimpleLocation;
import sg.edu.np.mad.travelapp.ui.BaseActivity;

public class Search extends BaseActivity {
    private final RouteAdapter adapter = new RouteAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        String query = getIntent().getStringExtra("query");
        Location location = getIntent().getParcelableExtra(LOCATION);

        initializeNavbar(location);
        initializeRecycler(adapter, findViewById(R.id.searchRecycler), false);

        ROUTE_REPO.getRouteByName(SimpleLocation.fromAndroidLocation(location), query, adapter::setRouteList);
    }
}