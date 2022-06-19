package sg.edu.np.mad.travelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import sg.edu.np.mad.travelapp.data.model.Bus;
import sg.edu.np.mad.travelapp.data.model.BusStop;
import sg.edu.np.mad.travelapp.data.model.Service;

public class ViewBusStops extends AppCompatActivity {

    private final String TAG = "ViewBusStopActivity";
    private View decorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bus_stops);

        RecyclerView busStopRecycler = findViewById(R.id.busStopRecycler);

        ImageView homeIcon = findViewById(R.id.homeIcon);
        ImageView nearbyIcon = findViewById(R.id.nearbyIcon);
        ImageView favIcon = findViewById(R.id.favIcon);

        nearbyIcon.setImageResource(R.drawable.nearby_active);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        BusTimingCardAdapter busTimingCardAdapter = new BusTimingCardAdapter(getBusStopList());
        busStopRecycler.setLayoutManager(layoutManager);
        busStopRecycler.setAdapter(busTimingCardAdapter);

        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent MainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(MainActivity);
            }
        });

        favIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ViewFavourites = new Intent(getApplicationContext(), ViewFavourites.class);
                startActivity(ViewFavourites);
            }
        });

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });
    }

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