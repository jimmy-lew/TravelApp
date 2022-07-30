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
import android.widget.TextView;
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
import sg.edu.np.mad.travelapp.data.model.RouteFare;
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
    MainActivity mainActivity = new MainActivity();

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
        String API_Key = mainActivity.GetAPIKey(this);
        mainActivity.InitializeGoogleAC(originTextView, this, API_Key);
        mainActivity.InitializeGoogleAC(destinationTextView, this, API_Key);
    }





    // ================= User Defined Functions =================




    // --- [Main] Compute Function >> Call other functions; Handles Flow --
    public void Compute(String from, String to) {
        Log.v(TAG, "Compute Function");
        getFareList(rfList -> {
            for (RouteFare rf : rfList) {
                if (rf.getFare() == null) return;
            }
            SetDisplay(rfList,1);
            SetDisplay(rfList,2);
        });
    }

    private void getFareList(final OnComplete<ArrayList<RouteFare>> onComplete) {
        ArrayList<RouteFare> fareList = new ArrayList<>();
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
            ArrayList<RouteFare> rfList = new ArrayList<>();
            ArrayList<String> transitModeCost = new ArrayList<>();
            for (Route route : RouteList) {
                // Reset Values
                RouteFare routefare = new RouteFare();
                routefare.setRoute(route);
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
                    routefare.setFare(fare);
                    Log.v(TAG, "Fare: " + fare);
                    onComplete.execute(rfList);
                });
                rfList.add(routefare);
            }
        });
    }

    private double GeocodeLocation(String Location) {
        // [ToDO] Call Google > Convert Location to coordinates
        return 0;
    }

    public String GetFareType() {
        // [ToDo] Get Fare Type from User Settings
        String Type = "student";
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

    private void SetDisplay(ArrayList<RouteFare> routeFares, int option){

        switch (option){
            case 1:
                Log.v(TAG, "RouteFare Size: " + routeFares.size());

                // [ToDo] Display Cheapest
                int index = 0;
                Double cheapestFare = 0.0;
                RouteFare cheapestRoute = new RouteFare();
                for (RouteFare routeFare : routeFares) {
                    Log.v(TAG, "Fare: " + routeFare.getFare());

                    if (index == 0) {
                        cheapestFare =  Double.parseDouble(routeFare.getFare());
                        cheapestRoute = routeFare;
                    }
                    else if (Double.parseDouble(routeFare.getFare()) < cheapestFare) {
                        cheapestFare = Double.parseDouble(routeFare.getFare());
                        cheapestRoute = routeFare;
                    }
                    index++;
                }
                 // Putting Values into the Display Cards
                Log.v(TAG, "Cheapest Route: " + "Setting Display");
                TextView Cost = findViewById(R.id.CheapestCostText);
                Cost.setText("$" + cheapestFare/100 + " SGD");
                TextView Duration = findViewById(R.id.CheapestDurationText);
                Duration.setText(cheapestRoute.getTotalDuration());
                TextView Distance = findViewById(R.id.CheapestTotalDistanceText);
                Distance.setText(cheapestRoute.getWalkingDistance() + " KM");
                TextView NumSteps = findViewById(R.id.CheapestStepText);
                NumSteps.setText(cheapestRoute.getNoSteps() + " Steps");
                break;

            case 2:
                // [ToDo] Display Fastest
                index = 0;
                Double fastestDuration = 0.0;
                RouteFare fastestRoute = new RouteFare();
                for (RouteFare rf : routeFares){
                    Log.v(TAG, "Duration: " + rf.getTotalDuration());
                    if (index == 0){
                        fastestDuration = rf.getRawTotalDuration();
                        fastestRoute = rf;
                    }
                    else if (rf.getRawTotalDuration() < fastestDuration){
                        fastestDuration = rf.getRawTotalDuration();
                        fastestRoute = rf;
                    }
                    index++;
                }
                Double fare = Double.parseDouble(fastestRoute.getFare())/100;
                TextView fCost = findViewById(R.id.FastestCostText);
                fCost.setText("$" + fare + " SGD");
                TextView fDuration = findViewById(R.id.FastestDurationText);
                fDuration.setText(fastestRoute.getTotalDuration());
                TextView fDistance = findViewById(R.id.FastestTotalDistanceText);
                fDistance.setText(fastestRoute.getWalkingDistance() + " KM");
                TextView fNumSteps = findViewById(R.id.FastestStepText);
                fNumSteps.setText(fastestRoute.getNoSteps() + " Steps");
                break;
        }
    }


    public interface OnComplete<T>{
        void execute(T data);
    }
}
