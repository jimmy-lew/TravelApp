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

    private BusStopRepository(){ }

    public static synchronized BusStopRepository get_instance() {
        return _instance == null ? _instance = new BusStopRepository() : _instance;
    }

    public void getNearbyBusStops(Location location, final OnComplete<ArrayList<BusStop>> onComplete){
        Call<ArrayList<BusStop>> call = RetrofitClient.getInstance().getApi().getNearbyBusStops(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
        call.enqueue(new Callback<ArrayList<BusStop>>() {
            @Override
            public void onResponse(Call<ArrayList<BusStop>> call, Response<ArrayList<BusStop>> response) {
                Log.v("URL", String.valueOf(call.request().url()));
                ArrayList<BusStop> busStopList = (ArrayList<BusStop>) response.body();
                busStopCache = busStopList;
                onComplete.execute(busStopList);
            }

            @Override
            public void onFailure(Call<ArrayList<BusStop>> call, Throwable t) { }
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
            public void onFailure(Call<ArrayList<BusStop>> call, Throwable t) { }
        });
    }
}
