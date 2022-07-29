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
    double total = 0.0;
    String From = "";
    String To = "";

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

    // [ToDo] API Call to calculate fare
    public double CalculateBusFare(ArrayList<String> fareType, double distance){
        String url = "https://data.gov.sg/api/action/datastore_search?resource_id=cd2db3f7-bb5d-43d1-9264-955d71eeaf97";
        // Tried using LTA's API (Fare Calculator) But their Bus Stop IDs are no the same as the Bus Stop Codes.
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
                        Log.v(TAG, "Query: " + ConvertDistanceToRange(distance));
                        Log.v(TAG, "Bool: " + record.getString("_id").equals(ConvertDistanceToRange(distance)));
                        if (record.getString("_id").equals(ConvertDistanceToRange(distance))) {
                            Log.v(TAG, "Distance: " + record.getString("distance"));
                            total += Double.parseDouble(record.getString(fareType.get(0)))- Double.parseDouble(fareType.get(1));
                        }
                    }


                    // Log.v(TAG, "Records + " );

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        return 0;
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
                Log.v(TAG,"Total: " + total);
                Log.v(TAG, "Route: " + "NEW ROUTE --------------- ");
                From = "";
                To = "";
                total = 0;
                for (Leg leg : route.getLegs()){
                    for (Step step : leg.getSteps()){
                        // Log.v(TAG, "[BUS]: " + String.valueOf(step.getMode().equals("BUS")));
                        if(step.getMode().equals("BUS")){
                            Log.v(TAG, "[Bus Details]: " + step.getDetails().getFrom() + "| to |" + step.getDetails().getTo() + "Bus No: " + step.getDetails().getLine().getName());
                             CalculateBusFare(GetFareType(), Double.parseDouble(step.getDistance().split(" ")[0]));
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

    public int ConvertDistanceToRange(double input){
        if (input <= 3.2){ return 1;}
        else if (input <= 4.2){ return 2;}
        else if (input <= 5.2){ return 3;}
        else if (input <= 6.2){ return 4;}
        else if (input <= 7.2){ return 5;}
        else if (input <= 8.2){ return 6;}
        else if (input <= 9.2){ return 7;}
        else if (input <= 10.2){ return 8;}
        else if (input <= 11.2){ return 9;}
        else if (input <= 12.2){ return 10;}
        else if (input <= 13.2){ return 11;}
        else if (input <= 14.2){ return 12;}
        else if (input <= 15.2){ return 13;}
        else if (input <= 16.2){ return 14;}
        else if (input <= 17.2){ return 15;}
        else if (input <= 18.2){ return 16;}
        else if (input <= 19.2){ return 17;}
        else if (input <= 20.2){ return 18;}
        else if (input <= 21.2){ return 19;}
        else if (input <= 22.2){ return 20;}
        else if (input <= 23.2){ return 21;}
        else if (input <= 24.2){ return 22;}
        else if (input <= 25.2){ return 23;}
        else if (input <= 26.2){ return 24;}
        else if (input <= 27.2){ return 25;}
        else if (input <= 28.2){ return 26;}
        else if (input <= 29.2){ return 27;}
        else if (input <= 30.2){ return 28;}
        else if (input <= 31.2){ return 29;}
        else if (input <= 32.2){ return 30;}
        else if (input <= 33.2){ return 31;}
        else if (input <= 34.2){ return 32;}
        else if (input <= 35.2){ return 33;}
        else if (input <= 36.2){ return 34;}
        else if (input <= 37.2){ return 35;}
        else if (input <= 38.2){ return 36;}
        else if (input <= 39.2){ return 37;}
        else if (input <= 40.2){ return 38;}
        else {return 39;}
    }
}
