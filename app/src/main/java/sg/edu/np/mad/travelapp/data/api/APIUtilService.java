package sg.edu.np.mad.travelapp.data.api;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APIUtilService {
    private static APIUtilService _instance = null;

    private APIUtilService() {}

    public static synchronized APIUtilService getInstance() {
        return _instance == null ? _instance = new APIUtilService() : _instance;
    }

    public void getBusStopCode(ArrayList<String> query, final OnComplete<ArrayList<String>> onComplete) {
        Call<ArrayList<String>> call = RetrofitClient.getInstance().getApi().getBusStopCode(query);
        call.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<String>> call, @NonNull Response<ArrayList<String>> response) {
                Log.v("URL", String.valueOf(call.request().url()));
                ArrayList<String> busStopCodeList = response.body();
                onComplete.execute(busStopCodeList);
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<String>> call, @NonNull Throwable t) { }
        });
    }

    public interface OnComplete<T> {
        void execute(T data);
    }
}
