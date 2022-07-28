package sg.edu.np.mad.travelapp;

import android.os.Bundle;
import android.util.Log;

import sg.edu.np.mad.travelapp.data.model.SimpleLocation;
import sg.edu.np.mad.travelapp.data.repository.RouteRepository;
import sg.edu.np.mad.travelapp.ui.BaseActivity;

public class RoutesActivity extends BaseActivity {
    private final RouteAdapter adapter = new RouteAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);

        initializeRecycler(adapter, findViewById(R.id.routeRecycler), false);

        SimpleLocation origin = new SimpleLocation(1.391841885183346,103.75166652244974);
        SimpleLocation destination = new SimpleLocation(1.3130129935971875,103.91341045018862);

        ROUTE_REPO.getRoute(origin, destination, adapter::setRouteList);
    }
}