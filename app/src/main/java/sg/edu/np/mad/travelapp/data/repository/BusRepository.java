package sg.edu.np.mad.travelapp.data.repository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import sg.edu.np.mad.travelapp.data.model.Bus;
import sg.edu.np.mad.travelapp.data.model.Service;

public class BusRepository implements Repository{

    private static BusRepository _instance = null;
    private ArrayList<Service> serviceList = new ArrayList<>();
    private final OkHttpClient client = new OkHttpClient();

    private final String TAG = "BusRepo";

    private BusRepository(){}

    public static synchronized BusRepository get_instance(){
        return _instance == null ? _instance = new BusRepository() : _instance;
    }

    public void populateBusList(String busStopCode, final OnComplete<ArrayList<Service>> onComplete){
        Request request = new Request.Builder()
                .url("http://datamall2.mytransport.sg/ltaodataservice/BusArrivalv2?BusStopCode=" + busStopCode)
                .header("AccountKey", "RdoZ93saQ32Ts1JcHbFegg==")
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {
                    try {
                        JSONObject busStopsResponse = new JSONObject(response.body().string());
                        JSONArray services = busStopsResponse.getJSONArray("Services");
                        ArrayList<Bus> newBusList = new ArrayList<>();
                        ArrayList<Service> newServiceList = new ArrayList<>();

                        if(services.length() > 0){
                            for(int i = 0; i < services.length(); i++){
                                String serviceNo = (String) services.getJSONObject(i).get("ServiceNo");
                                JSONObject serviceObject = services.getJSONObject(i);
                                for(int j = 0; j < 3; j++){
                                    String NextBus;
                                    NextBus = j == 0 ? "NextBus" : String.format("NextBus%s", (j+1));
                                    String feature = (String) serviceObject.getJSONObject(NextBus).get("Feature");
                                    String busType = (String) serviceObject.getJSONObject(NextBus).get("Type");
                                    String load = (String) serviceObject.getJSONObject(NextBus).get("Load");
                                    String latitude = (String) serviceObject.getJSONObject(NextBus).get("Latitude");
                                    String longitude = (String) serviceObject.getJSONObject(NextBus).get("Longitude");
                                    String eta = (String) serviceObject.getJSONObject(NextBus).get("EstimatedArrival");

                                    // Check for empty values
                                    if(latitude.equals("") || longitude.equals("")){
                                        latitude = "0.0";
                                        longitude = "0.0";
                                    }

                                    // Convert Latitude and Longitude to Double
                                    double lat = Double.valueOf(latitude);
                                    double lon = Double.valueOf(longitude);

                                    // ETA Calculator
                                    if (eta.length() > 0){
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

                                        Bus bus = new Bus(serviceNo,feature,busType,load,lat,lon,eta);
                                        newBusList.add(bus);
                                    }
                                }
                                newServiceList.add(new Service(serviceNo, newBusList));
                                newBusList = new ArrayList<>();
                            }
                            serviceList = newServiceList;

                            onComplete.execute(serviceList);
                        }
                        else{

                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });
    }
}
