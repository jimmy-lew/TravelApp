package sg.edu.np.mad.travelapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Granularity;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import sg.edu.np.mad.travelapp.data.api.APIUtilService;
import sg.edu.np.mad.travelapp.data.api.BusAPI;
import sg.edu.np.mad.travelapp.data.model.BusStop;
import sg.edu.np.mad.travelapp.data.model.Leg;
import sg.edu.np.mad.travelapp.data.model.Route;
import sg.edu.np.mad.travelapp.data.model.SimpleLocation;
import sg.edu.np.mad.travelapp.data.model.step.Step;
import sg.edu.np.mad.travelapp.data.repository.BusStopRepository;
import sg.edu.np.mad.travelapp.data.repository.RouteRepository;
import sg.edu.np.mad.travelapp.ui.BaseActivity;

public class FareCalculator extends BaseActivity {
    private static final String TAG = "FareCalculator";
    private FusedLocationProviderClient fusedLocationClient;
    String From = "";
    String To = "";
    double total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fare_calculator);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Button computeButton = findViewById(R.id.ComputeButton);
        ImageButton userLocationButton = findViewById(R.id.UseLocationButton);
        CardView cheapestCard = findViewById(R.id.DetailsCheapestCard);
        CardView fastestCard = findViewById(R.id.DetailsFastestCard);
        AutoCompleteTextView originTextView = findViewById(R.id.OriginTextbox);
        AutoCompleteTextView destinationTextView = findViewById(R.id.DestinationTextBox);

        String origin = originTextView.getText().toString();
        String destination = destinationTextView.getText().toString();


        computeButton.setOnClickListener(view -> {
            fastestCard.setVisibility(CardView.VISIBLE);
            cheapestCard.setVisibility(CardView.VISIBLE);
            Log.v(TAG, "Origin: " + origin);
            Compute(origin, destination);
        });

        userLocationButton.setOnClickListener(view ->{
            GetCurrentLocation();
        });

        // [ToDo] Add Search Debouncing & Google API Auto complete to AutoCompleteTextViews
    }

    // --- [Main] Compute Function >> Call other functions; Handles Flow --
    public void Compute(String from, String to) {
        Log.v(TAG, "Compute Function");
        getFareList(fareList -> {
            // Find cheapest and find fastest fare
            double cheapest = 0;
            int index = 0;

            for (String fare : fareList) {
                if (index == 0) {
                    cheapest = Double.valueOf(fare);
                } else if (Double.valueOf(fare) < cheapest) {
                    cheapest = Double.valueOf(fare);
                    index++;
                }
            }
            Log.v(TAG, "Cheapest Fare: " + cheapest);
        });
    }

    private void getFareList(final OnComplete<ArrayList<String>> onComplete) {
        ArrayList<String> fareList = new ArrayList<>();
        SimpleLocation origin = new SimpleLocation();
//        origin.setLat(GeocodeLocation("from").get(0));
//        origin.setLat(GeocodeLocation("from").get(1));
        origin.setLat(1.4346539724345708);
        origin.setLng(103.76548453675491);
        Log.v(TAG, "Origin: " + origin.getLat() + "," + origin.getLng());

        SimpleLocation destination = new SimpleLocation();
//        destination.setLat(GeocodeLocation("from").get(0));
//        destination.setLat(GeocodeLocation("from").get(1));
        destination.setLat(1.4138053998770526);
        destination.setLng(103.8093035017651);
        RouteRepository.getInstance().getRoute(origin, destination, RouteList -> {
            ArrayList<String> transitModeCost = new ArrayList<>();
            for (Route route : RouteList) {
                // Reset Values
                transitModeCost.clear();
                Log.v(TAG, "Route: " + "NEW ROUTE --------------- ");
                for (Leg leg : route.getLegs()) {
                    total = 0;
                    for (Step step : leg.getSteps()) {
                        if (step.getMode().equals("WALKING")) continue;
                        Log.v(TAG, "Distance: " + step.getDistance());
                        Log.v(TAG, "String: " + String.format("%s_%s", step.getDistance().split(" ")[0], step.getMode()));
                        Log.v(TAG, "Boolean:" + step.getMode().equals("WALKING"));
                        transitModeCost.add(String.format("%s_%s", step.getDistance().split(" ")[0], step.getMode()));
                    }
                }
                APIUtilService.getInstance().getFare(transitModeCost, GetFareType(), fare -> {
                    fareList.add(fare);
                    Log.v(TAG, "Fare: " + fare);
                    onComplete.execute(fareList);
                });
            }
        });
    }

    private double GeocodeLocation(String Location) {
        // [ToDO] Call Google > Convert Location to coordinates
        return 0;
    }

    public String GetFareType() {
        // [ToDo] Get Fare Type from User Settings
        String Type = "Student";
        return Type;
    }

    private void GetCurrentLocation() {
        Log.v(TAG, "BOOL: " + String.valueOf(ActivityCompat.checkSelfPermission(FareCalculator.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(FareCalculator.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED));
        if (ActivityCompat.checkSelfPermission(FareCalculator.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(FareCalculator.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            CurrentLocationRequest request = new CurrentLocationRequest.Builder()
                    .setDurationMillis(Long.MAX_VALUE)
                    .setGranularity(Granularity.GRANULARITY_FINE)
                    .setMaxUpdateAgeMillis(0)
                    .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                    .build();

            fusedLocationClient.getCurrentLocation(request, null).addOnSuccessListener(this, location -> {
                if (location != null){
                    Log.v(TAG, "User Location: " + location.getLatitude() + "," + location.getLongitude());
                    SetCurrentLocation(location);
                }else{
                    Log.v(TAG, "Location is Null");
                    Toast.makeText(getApplicationContext(), "Unable to Get Location", Toast.LENGTH_LONG).show();
                }
            });
        }
        else{
            Log.v(TAG, "Location Permissions Not Granted");
            Toast.makeText(getApplicationContext(), "Location Permissions Not Granted, Please enable location permission in the system settings.", Toast.LENGTH_LONG).show();
            // Request permission
            ActivityCompat.requestPermissions(FareCalculator.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    private void SetCurrentLocation(Location location){
        AutoCompleteTextView originTextView = findViewById(R.id.OriginTextbox);
        originTextView.setText(location.getLatitude() + ", " + location.getLongitude());
    }

    private void DisplayCheapest(ArrayList<String> fareList){
        // [ToDo] Display Cheapest Fare
    }

    public interface OnComplete<T>{
        void execute(T data);
    }
}
