package sg.edu.np.mad.travelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import sg.edu.np.mad.travelapp.data.model.Bus;
import sg.edu.np.mad.travelapp.data.model.BusStop;
import sg.edu.np.mad.travelapp.data.model.Service;
import sg.edu.np.mad.travelapp.data.repository.BusStopRepository;

public class ViewBusStops extends AppCompatActivity{

    private final String TAG = "ViewBusStopActivity";
    private View decorView;
    private ArrayList<BusStop> data = new ArrayList<>();

    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bus_stops);

        location = getIntent().getParcelableExtra("location");

        RecyclerView busStopRecycler = findViewById(R.id.favouritesRecycler);

        ImageView homeIcon = findViewById(R.id.homeIcon);
        ImageView nearbyIcon = findViewById(R.id.nearbyIcon);
        ImageView favIcon = findViewById(R.id.favIcon);

        nearbyIcon.setImageResource(R.drawable.nearby_active);

//        Location location = new Location("");
//        location.setLatitude(1.3918577281406086);
//        location.setLongitude(103.75166620390048);
//        data = getBusStopList();

        try {
            int i = 0;

            while(i < 20){
                data = BusStopRepository.get_instance(getApplicationContext()).findNearbyBusStops(location);
                if(BusStopRepository.get_instance(getApplicationContext()).isResponseFulfilled){
                    break;
                }
                else{
                    TimeUnit.SECONDS.sleep(1);
                    ++i;
                }
            }

        } catch (JSONException | InterruptedException e) {
            e.printStackTrace();
        }



        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        BusTimingCardAdapter busTimingCardAdapter = new BusTimingCardAdapter(data);
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
                decorView.setSystemUiVisibility(hideSystemBars());}
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

        busList.add(new Bus("307", "WAB", "SB", "SDA", 1, 1, "Arr"));
        busList.add(new Bus("307", "WAB", "SB", "SDA", 1, 1, "2 mins"));

        serviceList.add(new Service("307", busList));
        serviceList.add(new Service("84", busList));

        BusStop busStop = new BusStop("11100", "Yew Tee Rd", "Save my soul", (double)1, (double)1, serviceList);
        busStop.setBusStopName("1");
        busStopList.add(busStop);

        serviceList = new ArrayList<>();
        busList.add(new Bus("307", "WAB", "SB", "SDA", 1, 1, "12 mins"));
        serviceList.add(new Service("307A", busList));

        BusStop busStop2 = new BusStop("111111", "Yew Tee Street", "Save my soul", (double)1, (double)1, serviceList);
        busStop2.setBusStopName("2");
        busStopList.add(busStop2);
        busStopList.add(new BusStop("111111", "Yew Tee Street", "Save my soul", (double)1, (double)1, serviceList));

        return busStopList;
    }
}