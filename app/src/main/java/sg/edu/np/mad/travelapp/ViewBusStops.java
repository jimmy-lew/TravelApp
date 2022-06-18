package sg.edu.np.mad.travelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class ViewBusStops extends AppCompatActivity {

    private final String TAG = "ViewBusStopActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bus_stops);

        RecyclerView busStopRecycler = findViewById(R.id.busStopRecycler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        BusTimingCardAdapter busTimingCardAdapter = new BusTimingCardAdapter(getBusStopList());
        busStopRecycler.setLayoutManager(layoutManager);
        busStopRecycler.setAdapter(busTimingCardAdapter);
    }

    private ArrayList<BusStop> getBusStopList() {
        ArrayList<BusStop> busStopList = new ArrayList<>();
        ArrayList<Service> serviceList = new ArrayList<>();
        ArrayList<Bus> busList = new ArrayList<>();

        Bus bus1 = new Bus("307", "WAB", "SB", "SDA", 1, 1, "Arr");
        busList.add(bus1);
        Bus bus2 = new Bus("307", "WAB", "SB", "SDA", 1, 1, "2 mins");
        busList.add(bus2);
        Bus bus3 = new Bus("307", "WAB", "SB", "SDA", 1, 1, "12 mins");
        busList.add(bus3);

        Service service1 = new Service("307", busList);
        serviceList.add(service1);

        Service service2 = new Service("84", busList);
        serviceList.add(service2);

        Service service3 = new Service("307A", busList);
        serviceList.add(service3);

        BusStop busStop = new BusStop("111111", "Yew Tee Rd", "Save my soul", (double)1, (double)1, serviceList);
        busStopList.add(busStop);

        BusStop busStop2 = new BusStop("111111", "Yew Tee Street", "Save my soul", (double)1, (double)1, serviceList);
        busStopList.add(busStop2);

        return busStopList;
    }
}