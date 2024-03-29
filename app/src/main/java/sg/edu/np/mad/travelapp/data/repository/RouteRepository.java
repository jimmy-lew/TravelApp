package sg.edu.np.mad.travelapp.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sg.edu.np.mad.travelapp.data.api.RetrofitClient;
import sg.edu.np.mad.travelapp.data.model.Route;
import sg.edu.np.mad.travelapp.data.model.SimpleLocation;

/**
 * Repository to retrieve {@link Route} information from API
 */
public class RouteRepository implements IRepository {
    private static RouteRepository _instance = null;

    private RouteRepository() {}

    /* Singleton pattern to ensure only one instance of the client is created / exists */
    public static synchronized RouteRepository getInstance() {
        return _instance == null ? _instance = new RouteRepository() : _instance;
    }

    public void getRoute(SimpleLocation origin, SimpleLocation destination, final OnComplete<ArrayList<Route>> onComplete) {
        String originString = String.format("%s,%s", origin.getLat(), origin.getLng());
        String destinationString = String.format("%s,%s", destination.getLat(), destination.getLng());

        Call<ArrayList<Route>> call = RetrofitClient.getInstance().getApi().getRoute(originString, destinationString);
        call.enqueue(new Callback<ArrayList<Route>>() {
            @Override
            public void onResponse(Call<ArrayList<Route>> call, Response<ArrayList<Route>> response) {
                Log.v("URL", String.valueOf(call.request().url()));
                ArrayList<Route> routeList = response.body();
                onComplete.execute(routeList);
            }

            @Override
            public void onFailure(Call<ArrayList<Route>> call, Throwable t) { }
        });
    }

    public void getRouteByName(SimpleLocation origin, String destination, final OnComplete<ArrayList<Route>> onComplete) {
        String originString = String.format("%s,%s", origin.getLat(), origin.getLng());

        Call<ArrayList<Route>> call = RetrofitClient.getInstance().getApi().getRouteByName(originString, destination);
        call.enqueue(new Callback<ArrayList<Route>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Route>> call, @NonNull Response<ArrayList<Route>> response) {
                Log.v("URL", String.valueOf(call.request().url()));
                ArrayList<Route> routeList = response.body();
                onComplete.execute(routeList);
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Route>> call, @NonNull Throwable t) { }
        });
    }
}
