package sg.edu.np.mad.travelapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.cardview.widget.CardView;

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
    String From = "";
    String To = "";
    double total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fare_calculator);

        Button computeButton = findViewById(R.id.ComputeButton);
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

        // [ToDo] Add Search Debouncing & Google API Auto complete to AutoCompleteTextViews
    }


    // [ToDo] Call API & Calculate Cost
    public double CalculateMrtFare(int fareType, String from, String to){
        return 0;
    }

    // --- [Main] Compute Function >> Call other functions; Handles Flow --
    public void Compute(String from, String to){
        Log.v(TAG, "Compute Function");
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
        RouteRepository.getInstance().getRoute(origin, destination, RouteList ->{
            for (Route route: RouteList){
                // Reset Values
                Log.v(TAG, "Route: " + "NEW ROUTE --------------- ");
                for (Leg leg : route.getLegs()){
                    total = 0;
                    for (Step step : leg.getSteps()){
                        // Log.v(TAG, "[BUS]: " + String.valueOf(step.getMode().equals("BUS")));
                        if(step.getMode().equals("BUS")){
                            Log.v(TAG, "[Bus Details]: " + step.getDetails().getFrom() + "| to |" + step.getDetails().getTo() + "Bus No: " + step.getDetails().getLine().getName());
                            Log.v(TAG, "[Bus Details] DISTANCE: " + step.getDistance());

                            // ===== Calculate Bus Fare =====
                            double distance = Double.parseDouble(step.getDistance().split(" ")[0]);
                            ArrayList<String> fareType = GetFareType();

                            // Tried using LTA's API (Fare Calculator) But their Bus Stop IDs are no the same as the Bus Stop Codes.
                            String url = "https://data.gov.sg/api/action/datastore_search?resource_id=cd2db3f7-bb5d-43d1-9264-955d71eeaf97";
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url(url)
                                    .build();

                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    e.printStackTrace();
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    if (!response.isSuccessful()) {
                                        throw new IOException("Unexpected code " + response);
                                    }
                                    String responseData = response.body().string();
                                    // Log.v(TAG, "Response: " + responseData);
                                    try {
                                        JSONArray records = new JSONObject(responseData).getJSONObject("result").getJSONArray("records");
                                        for (int i = 0; i < records.length(); i++) {
                                            JSONObject record = records.getJSONObject(i);

                                            if (record.getInt("_id") == ConvertDistanceToRange(distance)) {
                                                Log.v(TAG, "Query: " + ConvertDistanceToRange(distance));
                                                Log.v(TAG, "ID: " + record.getString("_id"));
                                                Log.v(TAG, "Bool: " +  String.valueOf(record.getInt("_id") == ConvertDistanceToRange(distance)));
                                                Log.v(TAG, "Distance: " + record.getString("distance"));
                                                total += Double.parseDouble(record.getString(fareType.get(0)))- Double.parseDouble(fareType.get(1));
                                                Log.v(TAG, "Total: " + total);
                                                Log.v(TAG, "_--------------------------------_");
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                        if(step.getMode().equals("MRT")){
                            total += 1;
                            //CalculateMrtFare(GetFareType(), step.getDetails().getFrom(), step.getDetails().getTo());
                        }
                    }
                }
            }

            // Append Details
        });
    }

    private String ConvertStopNameToCode(String from, String to){
        ArrayList<String> stopName = new ArrayList<>();
        stopName.add(from);
        stopName.add(to);
        APIUtilService.getInstance().getBusStopCode(stopName, stopCode -> {
            From = stopCode.get(0);
            To = stopCode.get(1);
            Log.v(TAG, "From: " + From);
            Log.v(TAG, "To: " + To);
        });
        return "";
    }

    private void DisplayFare(double fare){
        Log.v(TAG, "Fare: " + fare);
    }

    private double GeocodeLocation(String Location){
        // [ToDO] Call Google > Convert Location to coordinates
        return 0;
    }

    public ArrayList<String> GetFareType() {
        // [ToDo] Get Fare Type from User Settings
        // ----- Offsets -----
        // (using LTA Calculator > (201) Kent Ridge Terminal > Blk 455 (2.9km) | Celementi Station A (3.2km)
        //Student Offset : 30 cents
        //Adult Offset: 60 cents
        //Senior Citizen: 45 cents
        //workfare_transport : 50 cents
        //Disabilities: 45 cents

        String Type = "Student";
        switch (Type) {
            case "Adult":
                return new ArrayList<>(Arrays.asList("adult_card_fare_per_ride","60"));
            case "Student":
                return new ArrayList<>(Arrays.asList("student_card_fare_per_ride","30"));
            case "Senior":
                return new ArrayList<>(Arrays.asList("senior_citizen_card_fare_per_ride","45"));
            case "Workfare":
                return new ArrayList<>(Arrays.asList("workfare_transport_concession_card_fare_per_ride","30"));
            case "Disability":
                return new ArrayList<>(Arrays.asList("persons_with_disabilities_card_fare_per_ride","45"));
        }
        return new ArrayList<>(Arrays.asList("Cash","0"));
    }

    public double ConvertDistanceToRange(double input){
        if (input <= 40.2){ return input - 2.2;}
        else {return 39;}
    }
}
