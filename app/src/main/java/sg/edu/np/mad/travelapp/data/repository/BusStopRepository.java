package sg.edu.np.mad.travelapp.data.repository;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import sg.edu.np.mad.travelapp.data.model.BusStop;

public class BusStopRepository {

    private static BusStopRepository _instance = null;
    private ArrayList<BusStop> busStopList = new ArrayList<>();
    private JSONArray busStopJson;
    private final OkHttpClient client = new OkHttpClient();

    private final String TAG = "BusStopRepo";

    private BusStopRepository(Context context) throws JSONException {
        busStopJson = new JSONArray(readBusStops(context));
    }

    public static synchronized BusStopRepository get_instance(Context context) throws JSONException {
        return _instance == null ? _instance = new BusStopRepository(context) : _instance;
    }

    public void findNearbyBusStops(Location location) throws JSONException {
        Request request = new Request.Builder()
                .url("https://maps.googleapis.com/maps/api/place/nearbysearch/json?keyword=bus+stop&location=" + location.getLatitude() +"%2C" + location.getLongitude() + "&radius=1500&type=bus_station&key=AIzaSyCnu98m6eMKGjpCfOfSMHFfa2bwbPZ0UcI")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){

                    ArrayList<BusStop> newBusStopList = new ArrayList<>();

                    try {
                        JSONObject placesRes = new JSONObject(response.body().string());
                        JSONArray results = placesRes.getJSONArray("results");

                        for (int i = 0; i < results.length(); i++){
                            JSONObject location = results.getJSONObject(i);
                            JSONObject latlng = location.getJSONObject("geometry").getJSONObject("location");

                            String stopName = (String) location.get("name");
                            String lat =  String.valueOf(latlng.get("lat"));
                            String lng = String.valueOf(latlng.get("lng"));

                            Log.v(TAG, stopName);

                            newBusStopList.add(getBusStopFromName(new BusStop(stopName, Double.valueOf(lat), Double.valueOf(lng))));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private BusStop getBusStopFromName(BusStop busStop) throws JSONException {
        for (int i = 0; i < busStopJson.length(); i++){
            JSONObject busStopObject = busStopJson.getJSONObject(i);
            String busStopName = (String) busStopObject.get("name");

            //Log.v(TAG, String.format("CachedStop: %s | GivenStop: %s | Match: %s", busStopName, busStop.getBusStopName(),busStopName.matches(busStop.getBusStopName())));

            if (busStopName.matches(busStop.getBusStopName())){
                Log.v("Matching Stop", busStop.getBusStopName());
                busStop.setBusStopCode((String) busStopObject.get("number"));
                busStop.setServiceList(BusRepository.get_instance().getServiceList(busStop.getBusStopCode()));
            }
        }

        return busStop;
    }

    private String readBusStops(Context context){
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
