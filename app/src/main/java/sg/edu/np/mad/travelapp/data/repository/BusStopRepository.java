package sg.edu.np.mad.travelapp.data.repository;

import android.location.Location;
import android.util.Log;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sg.edu.np.mad.travelapp.data.api.RetrofitClient;
import sg.edu.np.mad.travelapp.data.model.BusStop;
import sg.edu.np.mad.travelapp.data.model.Service;

/**
 * Repository pattern to retrieve & store [WIP], bus stop information
 */
public class BusStopRepository implements IRepository {

    private static BusStopRepository _instance = null;
    public ArrayList<BusStop> nearbyCache = new ArrayList<>(); // TODO: implement caching
    public ArrayList<BusStop> favouritesCache = new ArrayList<>();

    private BusStopRepository(){ }

    /* Singleton pattern to ensure only one instance of the client is created / exists */
    // TODO: implement lazy load
    public static synchronized BusStopRepository getInstance() {
        return _instance == null ? _instance = new BusStopRepository() : _instance;
    }

    public ArrayList<BusStop> getFavouritesCache() {
        return favouritesCache;
    }

    public ArrayList<BusStop> getNearbyCache() {
        return nearbyCache;
    }

    public void getNearbyBusStops(Location location, final OnComplete<ArrayList<BusStop>> onComplete){
        Call<ArrayList<BusStop>> call = RetrofitClient.getInstance().getApi().getNearbyBusStops(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
        call.enqueue(new Callback<ArrayList<BusStop>>() {
            @Override
            public void onResponse(Call<ArrayList<BusStop>> call, Response<ArrayList<BusStop>> response) {
                Log.v("URL", String.valueOf(call.request().url()));
                ArrayList<BusStop> busStopList = response.body();
                nearbyCache = busStopList;
                onComplete.execute(busStopList);
            }

            @Override
            public void onFailure(Call<ArrayList<BusStop>> call, Throwable t) { }
        });
    }

    public void getBusStopTimings(String query, final OnComplete<ArrayList<Service>> onComplete) {
        Call<ArrayList<Service>> call = RetrofitClient.getInstance().getApi().getBusStopTimings(query);
        call.enqueue(new Callback<ArrayList<Service>>() {
            @Override
            public void onResponse(Call<ArrayList<Service>> call, Response<ArrayList<Service>> response) {
                Log.v("URL", String.valueOf(call.request().url()));
                ArrayList<Service> serviceList = response.body();
                onComplete.execute(serviceList);
            }

            @Override
            public void onFailure(Call<ArrayList<Service>> call, Throwable t) { }
        });
    }

    public void getBusStopsByCode(ArrayList<String> query, final OnComplete<ArrayList<BusStop>> onComplete){
        Call<ArrayList<BusStop>> call = RetrofitClient.getInstance().getApi().getBusStopsByCode(query);
        call.enqueue(new Callback<ArrayList<BusStop>>() {
            @Override
            public void onResponse(Call<ArrayList<BusStop>> call, Response<ArrayList<BusStop>> response) {
                Log.v("URL", String.valueOf(call.request().url()));
                ArrayList<BusStop> busStopList = response.body();
                onComplete.execute(busStopList);
            }

            @Override
            public void onFailure(Call<ArrayList<BusStop>> call, Throwable t) { }
        });
    }

    public void getFavouriteStops(ArrayList<String> query, final OnComplete<ArrayList<BusStop>> onComplete){
        Call<ArrayList<BusStop>> call = RetrofitClient.getInstance().getApi().getBusStopsByName(query);
        call.enqueue(new Callback<ArrayList<BusStop>>() {
            @Override
            public void onResponse(Call<ArrayList<BusStop>> call, Response<ArrayList<BusStop>> response) {
                Log.v("URL", String.valueOf(call.request().url()));
                ArrayList<BusStop> busStopList = response.body();
                favouritesCache = busStopList;
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
                ArrayList<BusStop> busStopList = response.body();
                onComplete.execute(busStopList);
            }

            @Override
            public void onFailure(Call<ArrayList<BusStop>> call, Throwable t) { }
        });
    }
}
