package sg.edu.np.mad.travelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import sg.edu.np.mad.travelapp.data.model.Bus;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private View decorView;
    Weather weather = new Weather();

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

        weather.checkWeatherStatus("10", "10");
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
        // --- Get Bus Stops (BusStop API) ---
        BusStops("01319");

        // --- Get Busses Arrival at Bus Stop (Bus Arrival API) ---
        ArrayList<Bus> busList = BusArrival("01319");


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

    public void BusStops(String query) {
        // ---- Bus API (Get ALL BUS STOPS)----
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://datamall2.mytransport.sg/ltaodataservice/BusStops?")
                .header("AccountKey", "RdoZ93saQ32Ts1JcHbFegg==")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // ERROR
                Log.v(TAG, "ERROR: " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject busStopsResponse = new JSONObject(response.body().string());
//                        String query = "01329";
                        for (int i = 0; i < busStopsResponse.getJSONArray("value").length(); i++) {
                            String busStopCode = busStopsResponse.getJSONArray("value").getJSONObject(i).getString("BusStopCode");
                            if (busStopCode.equals(query)) {
                                String roadName = busStopsResponse.getJSONArray("value").getJSONObject(i).getString("RoadName");
                                String description = busStopsResponse.getJSONArray("value").getJSONObject(i).getString("Description");
                                double longitude = Double.valueOf(busStopsResponse.getJSONArray("value").getJSONObject(i).getString("Longitude"));
                                double latitude = Double.valueOf(busStopsResponse.getJSONArray("value").getJSONObject(i).getString("Latitude"));
                                Log.v(TAG, "busStopCode:" + busStopCode + " roadName:" + roadName + " description:" + description + " longitude:" + longitude + " latitude:" + latitude);
                            }
                        }
                    } catch (JSONException e) {
                        // ERROR
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public ArrayList<Bus> BusArrival(String query){
        ArrayList<Bus> busList = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://datamall2.mytransport.sg/ltaodataservice/BusArrivalv2?BusStopCode=" + query)
                .header("AccountKey", "RdoZ93saQ32Ts1JcHbFegg==")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // ERROR
                Log.v(TAG, "ERROR: " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject busStopsResponse = new JSONObject(response.body().string());

                        // --- Extract Bus Info | IF THERE ARE STILL BUS SERVICES (NO BUSES PAST 12, sad D: )----
                        if(Integer.valueOf(busStopsResponse.getJSONArray("Services").length()) > 0){
                            String serviceNo = String.valueOf(busStopsResponse.getJSONArray("Services").getJSONObject(0).get("ServiceNo"));
                            for(int i=0; i < 3; i++)
                            {
                                String NextBus;
                                NextBus = i == 0 ? "NextBus" : String.format("NextBus%s", (i+1));

                                String feature = String.valueOf(busStopsResponse.getJSONArray("Services").getJSONObject(0).getJSONObject(NextBus).get("Feature"));
                                String busType = String.valueOf(busStopsResponse.getJSONArray("Services").getJSONObject(0).getJSONObject(NextBus).get("Type"));
                                String load = String.valueOf(busStopsResponse.getJSONArray("Services").getJSONObject(0).getJSONObject(NextBus).get("Load"));
                                String latitude = String.valueOf(busStopsResponse.getJSONArray("Services").getJSONObject(0).getJSONObject(NextBus).get("Latitude"));
                                String longitude = String.valueOf(busStopsResponse.getJSONArray("Services").getJSONObject(0).getJSONObject(NextBus).get("Longitude"));
                                String eta =  String.valueOf(busStopsResponse.getJSONArray("Services").getJSONObject(0).getJSONObject(NextBus).get("EstimatedArrival"));

                                // Check for empty values
                                if(latitude.equals("") || longitude.equals("")){
                                    latitude = "0.0";
                                    longitude = "0.0";
                                }

                                // Convert Latitude and Longitude to Double
                                double lat = Double.valueOf(latitude);
                                double lon = Double.valueOf(longitude);

                                // ETA Calculator
                                String etaSplit = eta.split("T")[1];
                                // 2017-04-29T07:20:24+08:00 > 2017-04-29, 07:20:24+08:00
                                eta = etaSplit.substring(0, etaSplit.length() - 9);
                                // 07:20:24+08:00 > 07:20

                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                                try {
                                    Date now = new Date();
                                    String currentTime = sdf.format(now);
                                    Date d1 = sdf.parse(currentTime);
                                    Date d2 = sdf.parse(eta);
                                    long elapsed = d2.getTime() - d1.getTime();
                                    long timeInMinutes = ((elapsed/(1000*60)) % 60);

                                    eta = timeInMinutes < 2 ? "Arr" : String.format("%s mins", timeInMinutes);
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }

                                Log.v(TAG, "busStopsResponse: " + "ETA: " + eta);
                                Bus bus = new Bus(serviceNo,feature,busType,load,lat,lon,eta);
                                busList.add(bus);
                            }
                            Log.v(TAG, "busStopsResponse: " + busList.size());
                        }
                        else{
                            Log.v(TAG, "busStopsResponse: " + "No Bus Services");
                        }
                    } catch (JSONException e) {
                        // ERROR
                        Log.v(TAG, "ERROR2: " + e);
                    }
                }
            }
        });
        return busList;
    }

}