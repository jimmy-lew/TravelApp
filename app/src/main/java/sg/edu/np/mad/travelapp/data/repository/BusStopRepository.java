package sg.edu.np.mad.travelapp.data.repository;

import android.content.Context;
import android.location.Location;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import sg.edu.np.mad.travelapp.data.model.BusStop;

public class BusStopRepository implements Repository {

    private static BusStopRepository _instance = null;
    public ArrayList<BusStop> busStopList = new ArrayList<>();
    private JSONArray busStopJson;
    private final OkHttpClient client = new OkHttpClient();

    private final String TAG = "BusStopRepo";

    private BusStopRepository(Context context) throws JSONException {
        busStopJson = new JSONArray(readBusStops(context));
    }

    public static synchronized BusStopRepository get_instance(Context context) throws JSONException {
        return _instance == null ? _instance = new BusStopRepository(context) : _instance;
    }

    public void findNearbyBusStops(Location location, final OnComplete<ArrayList<BusStop>> onComplete) throws JSONException {
        Request request = new Request.Builder()
                .url("https://maps.googleapis.com/maps/api/place/nearbysearch/json?keyword=bus+stop&location=" + location.getLatitude() +"%2C" + location.getLongitude() + "&radius=150&type=[transit_station, bus_station]&key=AIzaSyCnu98m6eMKGjpCfOfSMHFfa2bwbPZ0UcI")
                .build();

        client.newCall(request).enqueue(new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){

                    try {
                        JSONObject placesRes = new JSONObject(response.body().string());
                        JSONArray results = placesRes.getJSONArray("results");

                        ArrayList<BusStop> busStopList = new ArrayList<>();
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject location = results.getJSONObject(i);
                            JSONObject latlng = location.getJSONObject("geometry").getJSONObject("location");

                            String stopName = (String) location.get("name");
                            String lat = String.valueOf(latlng.get("lat"));
                            String lng = String.valueOf(latlng.get("lng"));

                            getBusStopFromName(new BusStop(stopName, Double.valueOf(lat), Double.valueOf(lng)), busStop -> {
                                busStopList.add(busStop);
                                onComplete.execute(busStopList);
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void findBusStopFromNameQuery(String stopName, final OnComplete<ArrayList<BusStop>> onComplete) throws JSONException {
        ArrayList<BusStop> busStopList = new ArrayList<>();
        getBusStopFromName(new BusStop().setBusStopName(stopName), busStop -> {
            busStopList.add(busStop);
            onComplete.execute(busStopList);
        });
    }

    public void findBusStopFromNamesQuery(ArrayList<String> busStopNameList, final OnComplete<ArrayList<BusStop>> onComplete) throws JSONException {
        ArrayList<BusStop> busStopList = new ArrayList<>();
        if(busStopNameList != null){
            for (String stopName : busStopNameList){
                getBusStopFromName(new BusStop().setBusStopName(stopName), busStop -> {
                    busStopList.add(busStop);
                    onComplete.execute(busStopList);
                });
            }
        }
        else{
            onComplete.execute(busStopList);
        }
    }

    public void findBusStopFromCodesQuery(ArrayList<String> busStopCodeList, final OnComplete<ArrayList<BusStop>> onComplete) throws JSONException {
        ArrayList<BusStop> busStopList = new ArrayList<>();
        if(busStopCodeList != null){
            for (String stopCode : busStopCodeList){
                getBusStopFromCode(new BusStop().setBusStopCode(stopCode), busStop -> {
                    busStopList.add(busStop);
                    onComplete.execute(busStopList);
                });
            }
        }
        else{
            onComplete.execute(busStopList);
        }
    }

    private void getBusStopFromCode(BusStop busStop, final OnComplete<BusStop> onComplete){
        BusRepository.get_instance().populateBusList(busStop.getBusStopCode(), serviceList -> {
            busStop.setServiceList(serviceList);
            onComplete.execute(busStop);
        });
    }

    private void getBusStopFromName(BusStop busStop, final OnComplete<BusStop> onComplete) throws JSONException {
        for (int i = 0; i < busStopJson.length(); i++){
            JSONObject busStopObject = busStopJson.getJSONObject(i);
            String busStopName = (String) busStopObject.get("name");

            if (busStopName.matches(busStop.getBusStopName())){
                busStop.setBusStopCode((String) busStopObject.get("number"));
                BusRepository.get_instance().populateBusList(busStop.getBusStopCode(), serviceList -> {
                    busStop.setServiceList(serviceList);
                    onComplete.execute(busStop);
                });
            }
        }
    }

    private final String readBusStops(Context context){
        String json = null;
        try {
            InputStream is = context.getAssets().open("stops.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return json;
    }
}
