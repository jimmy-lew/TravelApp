package sg.edu.np.mad.travelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import sg.edu.np.mad.travelapp.data.api.WeatherApi;
import sg.edu.np.mad.travelapp.data.model.Bus;
import sg.edu.np.mad.travelapp.data.repository.BusRepository;
import sg.edu.np.mad.travelapp.data.repository.BusStopRepository;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private View decorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //CardView favOutCardView = findViewById(R.id.favOutCardView);
        //CardView favInCardView = findViewById(R.id.favInCardView);
        //ImageView favIcon = findViewById(R.id.favIcon);
        CardView homeOutCardView = findViewById(R.id.homeOutCardView);
        CardView homeInCardView = findViewById(R.id.homeInCardView);
        ImageView homeIcon = findViewById(R.id.homeIcon);
        //CardView nearbyOutCardView = findViewById(R.id.nearbyOutCardView);
        //CardView nearbyInCardView = findViewById(R.id.nearbyInCardView);
        ImageView nearbyIcon = findViewById(R.id.nearbyIcon);

        //TODO: Need add drop shadow for navbar
        homeOutCardView.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"));
        homeInCardView.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"));
        homeIcon.setImageResource(R.drawable.home_active);
        //TODO: Not sure how to remove drop shadow for inactive
        nearbyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ViewBusStops = new Intent(getApplicationContext(), ViewBusStops.class);
                startActivity(ViewBusStops);
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

        new WeatherApi().getWeatherConditions("10", "10");

        Location location = new Location("");
        location.setLatitude(1.3918577281406086);
        location.setLongitude(103.75166620390048);

        try {
            BusStopRepository.get_instance(getApplicationContext()).findNearbyBusStops(location);
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        BusRepository.get_instance().getServiceList("15201");

        //navbarUpdate(currentNav);
    }

    // ---- Hide System Default UI Elements (Status Bar & Navigation Bar) ----
    // Documentation : https://developer.android.com/reference/android/app/Activity >> OnWindowFocusChanged ----
    // Called when the activity gains or loses window focus, called true if focused.
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
}