package sg.edu.np.mad.travelapp;
// By: Addison Chua Jie Yi, S10222525B
// FYI : Synchronous Calls causes errors > Network on Main thread thing

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
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
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
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

public class FareCalculator extends BaseActivity implements AdapterView.OnItemSelectedListener{
    private static final String TAG = "FareCalculator";
    private FusedLocationProviderClient fusedLocationClient;
    double total = 0;

    MainActivity mainActivity = new MainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fare_calculator);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Initializing Display Elements
        Button computeButton = findViewById(R.id.ComputeButton);
        ImageButton userLocationButton = findViewById(R.id.UseLocationButton);
        AutoCompleteTextView originTextView = findViewById(R.id.OriginTextbox);
        AutoCompleteTextView destinationTextView = findViewById(R.id.DestinationTextBox);
        CardView cheapestCard = findViewById(R.id.DetailsCheapestCard);
        CardView fastestCard = findViewById(R.id.DetailsFastestCard);

        // Initialize Spinner (Dropdown);
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.fareType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        // Compute Button Click Event > Compute Fare
        computeButton.setOnClickListener(view -> {
            String origin = originTextView.getText().toString();
            String destination = destinationTextView.getText().toString();
            if (origin.isEmpty() || destination.isEmpty())  // Check if TextBox values are empty
            {
                Toast.makeText(FareCalculator.this, "Please enter origin and destination", Toast.LENGTH_SHORT).show();
            } else {
                cheapestCard.setVisibility(View.VISIBLE);
                fastestCard.setVisibility(View.VISIBLE);
                Compute(); // Run Compute Function
            }
        });

        // Get Location button (Beside the "from" search)
        userLocationButton.setOnClickListener(view ->{
            Toast.makeText(FareCalculator.this, "Getting your current location...", Toast.LENGTH_SHORT).show();
            GetCurrentLocation();
        });

        /* --- Initializing AutoCompleteTextView w/ Google AutoComplete Suggestions --- */
        String API_Key = mainActivity.GetAPIKey(this,"API_KEY");
        mainActivity.InitializeGoogleAC(originTextView, this, API_Key);
        mainActivity.InitializeGoogleAC(destinationTextView, this, API_Key);
    }





    // ================= User Defined Functions =================




    // --- [Main] Compute Function >> Handles Flow ; Calls other Functions --
    public void Compute() {
        Log.v(TAG, "Compute Function");

        getFareList(rfList -> {
            for (RouteFare rf : rfList) {
                if (rf.getFare() == null) return;
            }
            // If API returns all the data then display the data.
            SetDisplay(rfList,1);
            SetDisplay(rfList,2);
        });
    }

    // getFareList Function > Calls Geocode Function, then calls API to get routes based on Geocode Locations
    private void getFareList(final OnComplete<ArrayList<RouteFare>> onComplete) {
        //Async Function, Returns value of "ArrayList<RouteFare>" to callback method after getting data from API
        GeocodeLocation(FromToCoordinates ->{
            for (List<Double> dList : FromToCoordinates) {
                Log.v(TAG, "FromToCoordinates: " + dList.toString());
                if (dList.isEmpty()) return;
                for(Double d : dList)  // Check if every element in the nested List has a value
                {
                    Log.v(TAG, "Coordinate: " + d);
                    if (d == null) return;
                }
            }

            SimpleLocation origin = new SimpleLocation();
            origin.setLat(FromToCoordinates.get(0).get(0));
            origin.setLng(FromToCoordinates.get(0).get(1));
            Log.v(TAG, "Origin: " + origin.getLat() + "," + origin.getLng());

            // Update (31/7/22, 9:11PM) : API Support for Raw String Addresses for Destinations. >> removed need to geocode for destination
            // SimpleLocation destination = new SimpleLocation();
            // destination.setLat(FromToCoordinates.get(1).get(0));
            // destination.setLng(FromToCoordinates.get(1).get(1));
            // Log.v(TAG, "Destination: " + destination.getLat() + "," + destination.getLng());
            String destination = ((AutoCompleteTextView)findViewById(R.id.DestinationTextBox)).getText().toString();

            // Call API to Get Routes
            RouteRepository.getInstance().getRouteByName(origin, destination, RouteList -> {
                ArrayList<RouteFare> rfList = new ArrayList<>();
                ArrayList<String> transitModeCost = new ArrayList<>();
                for (Route route : RouteList) {
                    // Reset Values
                    RouteFare routefare = new RouteFare();
                    routefare.setRoute(route);
                    transitModeCost.clear();
                    for (Leg leg : route.getLegs()) {
                        total = 0;
                        for (Step step : leg.getSteps()) {
                            if (step.getMode().equals("WALKING")) continue;
//                        Log.v(TAG, "Distance: " + step.getDistance());
//                        Log.v(TAG, "String: " + String.format("%s_%s", step.getDistance().split(" ")[0], step.getMode()));
//                        Log.v(TAG, "Boolean:" + step.getMode().equals("WALKING"));
                            transitModeCost.add(String.format("%s_%s", step.getDistance().split(" ")[0], step.getMode()));
                        }
                    }
                    APIUtilService.getInstance().getFare(transitModeCost, GetFareType(), fare -> {
                        routefare.setFare(fare);
                        onComplete.execute(rfList);
                    });
                    rfList.add(routefare);
                }
            });
        });


    }

    // GeocodeLocation Function > Geocodes Addresses into Coordinates (Using Google API)
    private void GeocodeLocation(final OnComplete<List<List<Double>>> onComplete) {
        String origin = ((AutoCompleteTextView)findViewById(R.id.OriginTextbox)).getText().toString();
        // Update (31/7/22, 9:11PM) : API Support for Raw String Addresses for Destinations. >> removed need to geocode for destination
        // String destination = ((AutoCompleteTextView)findViewById(R.id.DestinationTextBox)).getText().toString();
        ArrayList<String> toGeocode = new ArrayList<>( Arrays.asList(origin));

        // Prepare toast messages (for errors)
        Toast ToastGeocodeFailed = Toast.makeText(FareCalculator.this, "Geocoding Failed!", Toast.LENGTH_SHORT);

        List<List<Double>> FromToCoordsList = new ArrayList<>();
        // API Request
        for (String Location : toGeocode){
            OkHttpClient client = new OkHttpClient();
            String Country = "SG";
            String URL = "https://maps.googleapis.com/maps/api/geocode/json?address=" + Location + "&components=country:" + Country + "&key=" + mainActivity.GetAPIKey(this,"GEOCODE_API_KEY");
            Request request = new Request.Builder()
                    .url(URL)
                    .build();

            client.newCall(request)
                    .enqueue(new Callback() {
                        @Override
                        public void onFailure(final Call call, IOException e) {
                            // If API Request Fails, Show Toast Message
                            Log.v(TAG, "API Geocode Failed");
                            ToastGeocodeFailed.show();
                        }

                        @Override
                        public void onResponse(Call call, final Response response) throws IOException {
                            // If API Response is successful, geocode location into coordinates
                            String res = response.body().string();
                            Log.v(TAG, "[API] Geocode Response: " + res);
                            try {
                                JsonObject jsonObject = new JsonParser().parse(res).getAsJsonObject();

                                Double Longitude = jsonObject.getAsJsonArray("results").getAsJsonArray().getAsJsonArray().get(0).getAsJsonObject().get("geometry").getAsJsonObject().get("location").getAsJsonObject().get("lng").getAsDouble();
                                Double Latitude = jsonObject.getAsJsonArray("results").getAsJsonArray().getAsJsonArray().get(0).getAsJsonObject().get("geometry").getAsJsonObject().get("location").getAsJsonObject().get("lat").getAsDouble();
                                List<Double> coordinates = new ArrayList<>();
                                coordinates.add(Latitude);
                                coordinates.add(Longitude);
                                FromToCoordsList.add(coordinates);
                                Log.v(TAG, "[Geocode] Latitude: " + Latitude + " Longitude: " + Longitude);
                                // Call Callback Function > "return" values after getting API response
                                onComplete.execute(FromToCoordsList);
                            }
                            catch (Exception e) {
                                Log.v(TAG, "API Parsing Failed");
                                Log.v(TAG, "Exception: " + e);
                            }
                        }
                    });
        }
    }

    // GetFareType Function > Gets Spinner Value to be used as the "Fare Type" when calculating/calling the API
    public String GetFareType() {
        // [ToDo] Get Fare Type from User Settings (still waiting for ryan zzz)
        // (update | 31/7/22 5:31am): I will just do a dropdown.
        Spinner mySpinner = (Spinner) findViewById(R.id.spinner);
        String text = mySpinner.getSelectedItem().toString();
        return text.toLowerCase();
    }

    // GetCurrentLocation Function > Gets User Current (approximate) location
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

    // SetCurrentLocation Function > Sets User Current Location Into the Origin TextBox
    private void SetCurrentLocation(Location location){
        AutoCompleteTextView originTextView = findViewById(R.id.OriginTextbox);
        originTextView.setText(location.getLatitude() + ", " + location.getLongitude());
    }

    // SetDisplay > Insert information into the display cards
    private void SetDisplay(ArrayList<RouteFare> routeFares, int option){
        Log.v(TAG, "Setting Display!");
        switch (option){
            case 1: // Display Cheapest
                int index = 0;
                Double cheapestFare = 0.0;
                // -- Calculating & Comparing routes to find the Cheapest (based on Fare type)
                RouteFare cheapestRoute = new RouteFare();
                for (RouteFare routeFare : routeFares) {
                    if (index == 0) {
                        cheapestFare =  Double.parseDouble(routeFare.getFare());
                        cheapestRoute = routeFare;
                    }
                    else if (Double.parseDouble(routeFare.getFare()) < cheapestFare) {
                        cheapestFare = Double.parseDouble(routeFare.getFare());
                        cheapestRoute = routeFare;
                    }
                    index++;
                    Log.v(TAG,"[Cheapest] Fare: " + routeFare.getFare() + " | " + cheapestRoute.getRoute());
                }
                // Displaying Information
                TextView Cost = findViewById(R.id.CheapestCostText);
                Cost.setText("$" + cheapestFare/100 + " SGD");
                TextView Duration = findViewById(R.id.CheapestDurationText);
                Duration.setText(cheapestRoute.getTotalDuration());
                TextView Distance = findViewById(R.id.CheapestTotalDistanceText);
                Distance.setText(cheapestRoute.getWalkingDistance() + " KM");
                TextView NumSteps = findViewById(R.id.CheapestStepText);
                NumSteps.setText(cheapestRoute.getNoSteps() + " Steps");
                break;

            case 2: // Display Fastest
                index = 0;
                Double fastestDuration = 0.0;
                // -- Calculating & Comparing routes to find the Fastest (least amount of time)
                RouteFare fastestRoute = new RouteFare();
                for (RouteFare rf : routeFares){
                    if (index == 0){
                        fastestDuration = rf.getRawTotalDuration();
                        fastestRoute = rf;
                    }
                    else if (rf.getRawTotalDuration() < fastestDuration){
                        fastestDuration = rf.getRawTotalDuration();
                        fastestRoute = rf;
                    }
                    index++;
                    Log.v(TAG,"[FASTEST] Duration: " + rf.getTotalDuration() + " | " + fastestRoute.getTotalDuration());
                }
                // Displaying Information
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

    @Override // Listener for Spinner, Shows Toast Message when an option is clicked
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), "Selected " + text + " Fare Type", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    // ====== Interfaces for ASync (Callback Functions) ====== //
    public interface OnComplete<T>{
        void execute(T data);
    }
}
