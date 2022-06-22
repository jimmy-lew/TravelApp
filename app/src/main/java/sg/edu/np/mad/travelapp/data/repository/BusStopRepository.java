package sg.edu.np.mad.travelapp.data.repository;

import android.location.Location;
import android.util.Log;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sg.edu.np.mad.travelapp.data.api.RetrofitClient;
import sg.edu.np.mad.travelapp.data.model.BusStop;

public class BusStopRepository implements Repository {

    private static BusStopRepository _instance = null;
    public ArrayList<BusStop> busStopCache = new ArrayList<>();

    private final String TAG = "BusStopRepo";

    private BusStopRepository(){
//        busStopJson = new JSONArray(readBusStops(context));
    }

    public static synchronized BusStopRepository get_instance() {
        return _instance == null ? _instance = new BusStopRepository() : _instance;
    }

    public void getNearbyBusStops(Location location, final OnComplete<ArrayList<BusStop>> onComplete){
        Call<ArrayList<BusStop>> call = RetrofitClient.getInstance().getApi().getNearbyBusStops(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
        call.enqueue(new Callback<ArrayList<BusStop>>() {
            @Override
            public void onResponse(Call<ArrayList<BusStop>> call, Response<ArrayList<BusStop>> response) {
                ArrayList<BusStop> busStopList = (ArrayList<BusStop>) response.body();
                busStopCache = busStopList;
                onComplete.execute(busStopList);
            }

            @Override
            public void onFailure(Call<ArrayList<BusStop>> call, Throwable t) {

            }
        });
    }

    public void getBusStopsByName(ArrayList<String> query, final OnComplete<ArrayList<BusStop>> onComplete){
        Call<ArrayList<BusStop>> call = RetrofitClient.getInstance().getApi().getBusStopsByName(query);
        call.enqueue(new Callback<ArrayList<BusStop>>() {
            @Override
            public void onResponse(Call<ArrayList<BusStop>> call, Response<ArrayList<BusStop>> response) {
                Log.v("URL", String.valueOf(call.request().url()));
                ArrayList<BusStop> busStopList = (ArrayList<BusStop>) response.body();
                busStopCache = busStopList;
                onComplete.execute(busStopList);
            }

            @Override
            public void onFailure(Call<ArrayList<BusStop>> call, Throwable t) {

            }
        });
    }

//    public void findNearbyBusStops(Location location, final OnComplete<ArrayList<BusStop>> onComplete) throws JSONException {
//        Request request = new Request.Builder()
//                .url("https://maps.googleapis.com/maps/api/place/nearbysearch/json?keyword=bus+stop&location=" + location.getLatitude() +"%2C" + location.getLongitude() + "&radius=150&type=[transit_station, bus_station]&key=AIzaSyCnu98m6eMKGjpCfOfSMHFfa2bwbPZ0UcI")
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//        @Override
//        public void onFailure(@NonNull Call call, @NonNull IOException e) {
//        }
//
//        @Override
//        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                if(response.isSuccessful()){
//
//                    try {
//                        JSONObject placesRes = new JSONObject(response.body().string());
//                        JSONArray results = placesRes.getJSONArray("results");
//
//                        ArrayList<BusStop> busStopList = new ArrayList<>();
//                        for (int i = 0; i < results.length(); i++) {
//                            JSONObject location = results.getJSONObject(i);
//                            JSONObject latlng = location.getJSONObject("geometry").getJSONObject("location");
//
//                            String stopName = (String) location.get("name");
//                            Double lat = (Double) latlng.get("lat");
//                            Double lng = (Double) latlng.get("lng");
//
//                            SimpleLocation simpleLocation = new SimpleLocation(lat, lng);
//
//                            getBusStopFromName(new BusStop(stopName, simpleLocation), busStop -> {
//                                busStopList.add(busStop);
//                                onComplete.execute(busStopList);
//                            });
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//    }
//
//    public void findBusStopFromNameQuery(String stopName, final OnComplete<ArrayList<BusStop>> onComplete) throws JSONException {
//        ArrayList<BusStop> busStopList = new ArrayList<>();
//        getBusStopFromName(new BusStop().setName(stopName), busStop -> {
//            busStopList.add(busStop);
//            onComplete.execute(busStopList);
//        });
//    }
//
//    public void findBusStopFromNamesQuery(ArrayList<String> busStopNameList, final OnComplete<ArrayList<BusStop>> onComplete) throws JSONException {
//        ArrayList<BusStop> busStopList = new ArrayList<>();
//        if(busStopNameList != null){
//            for (String stopName : busStopNameList){
//                getBusStopFromName(new BusStop().setName(stopName), busStop -> {
//                    busStopList.add(busStop);
//                    onComplete.execute(busStopList);
//                });
//            }
//        }
//        else{
//            onComplete.execute(busStopList);
//        }
//    }

//    public void findBusStopFromCodesQuery(ArrayList<String> busStopCodeList, final OnComplete<ArrayList<BusStop>> onComplete) throws JSONException {
//        ArrayList<BusStop> busStopList = new ArrayList<>();
//        if(busStopCodeList != null){
//            for (String stopCode : busStopCodeList){
//                getBusStopFromCode(new BusStop().setBusStopCode(stopCode), busStop -> {
//                    busStopList.add(busStop);
//                    onComplete.execute(busStopList);
//                });
//            }
//        }
//        else{
//            onComplete.execute(busStopList);
//        }
//    }
//
//    private void getBusStopFromCode(BusStop busStop, final OnComplete<BusStop> onComplete){
//        BusRepository.get_instance().populateBusList(busStop.getBusStopCode(), serviceList -> {
//            busStop.setServiceList(serviceList);
//            onComplete.execute(busStop);
//        });
//    }
//
//    private void getBusStopFromName(BusStop busStop, final OnComplete<BusStop> onComplete) throws JSONException {
//        for (int i = 0; i < busStopJson.length(); i++){
//            JSONObject busStopObject = busStopJson.getJSONObject(i);
//            String busStopName = (String) busStopObject.get("name");
//
//            if (busStopName.matches(busStop.getName())){
//                busStop.setCode((String) busStopObject.get("number"));
//                BusRepository.get_instance().populateBusList(busStop.getCode(), serviceList -> {
//                    busStop.setServiceList(serviceList);
//                    onComplete.execute(busStop);
//                });
//            }
//        }
//    }
//
//    private final String readBusStops(Context context){
//        String json = null;
//        try {
//            InputStream is = context.getAssets().open("stops.json");
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//            json = new String(buffer, "UTF-8");
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//
//        return json;
//    }
}
