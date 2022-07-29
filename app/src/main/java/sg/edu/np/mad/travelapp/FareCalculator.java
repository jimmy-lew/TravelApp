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
import sg.edu.np.mad.travelapp.data.model.BusStop;
import sg.edu.np.mad.travelapp.data.model.Leg;
import sg.edu.np.mad.travelapp.data.model.Route;
import sg.edu.np.mad.travelapp.data.model.SimpleLocation;
import sg.edu.np.mad.travelapp.data.model.Step;
import sg.edu.np.mad.travelapp.data.repository.BusStopRepository;
import sg.edu.np.mad.travelapp.data.repository.RouteRepository;
import sg.edu.np.mad.travelapp.ui.BaseActivity;

public class FareCalculator extends BaseActivity {
    private static final String TAG = "FareCalculator";
    double total = 0.0;

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
    public double CalculateBusFare(int fareType, String from, String to, String busNo){
        Log.v(TAG, "Calculating fare for " + busNo);
        String f = ConvertStopNameToCode(from);
        String t = ConvertStopNameToCode(to);

        f = "4465";
        t = "2907";
        String tripInfo = "usiAccumulatedDistance1=0-usiAccumulatedDistance2=0-usiAccumulatedDistance3=0-usiAccumulatedDistance4=0-usiAccumulatedDistance5=0-usiAccumulatedDistance6=0-usiAccumulatedFare1=0-usiAccumulatedFare2=0-usiAccumulatedFare3=0-usiAccumulatedFare4=0-usiAccumulatedFare5=0-usiAccumulatedFare6=0";
        String url = "https://www.lta.gov.sg/content/ltagov/en/map/fare-calculator/jcr:content/map2-content/farecalculator.busget.html?fare="+ fareType + "&from=" + f + "&to=" + t + "&tripInfo=" + tripInfo + "&bus=" + busNo;
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("fare=", String.valueOf(GetFareType()))
                .add("from=", f)
                .add("to=", t)
                .add("tripInfo=", tripInfo)
                .add("bus=", busNo)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
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
                Log.v(TAG, "Response: " + responseData);
                try {
                    JSONObject jsonResponse = new JSONObject(responseData);
                    jsonResponse.get("fare");
                    Log.v(TAG, "Fare: " + jsonResponse.get("fare"));
                    total += Double.parseDouble(jsonResponse.get("fare").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return 0;
    }

    private String ConvertStopNameToCode(String query){
        String code = "";
        return code;
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
            Log.v(TAG, "Route List: " + RouteList.size());
            total = 0;
            for (Route route: RouteList){
                for (Leg leg : route.getLegs()){
                    for (Step step : leg.getSteps()){
                        Log.v(TAG, "[BUS]" + String.valueOf(step.getMode().equals("BUS")));
                        Log.v(TAG, step.getMode());
                        if(step.getMode().equals("BUS")){
                            Log.v(TAG, "[Bus Details]: " + step.getDetails().getFrom() + "| to |" + step.getDetails().getTo() + "Bus No: " + step.getDetails().getLine().getName());
                             CalculateBusFare(GetFareType(), step.getDetails().getFrom(), step.getDetails().getTo(), step.getDetails().getLine().getName());
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

    public int GetFareType() {
        // [ToDo] Get Fare Type from User Settings
        String Type = "Student";
        switch (Type) {
            case "Adult":
                return 30;
            case "Student":
                return 40;
            case "Senior Citizen":
                return 39;
            case "Workfare Transport Concession Scheme":
                return 38;
        }
        return 0;
    }
}
