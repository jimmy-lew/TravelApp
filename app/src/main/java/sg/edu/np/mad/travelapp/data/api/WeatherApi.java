package sg.edu.np.mad.travelapp.data.api;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherApi {
    private final OkHttpClient client = new OkHttpClient();
    private boolean isRaining;

    public WeatherApi() {

    }

    public void getWeatherConditions(String lat, String lon){
        Request request = new Request.Builder()
                .url("https://weatherbit-v1-mashape.p.rapidapi.com/current?lon=" + lat + "&lat=" + lon + "&units=metric&lang=en")
                .get()
                .addHeader("X-RapidAPI-Key", "db09627fefmshbb7e0a02975ba60p1e9b1fjsn7a01a15d0b4c")
                .addHeader("X-RapidAPI-Host", "weatherbit-v1-mashape.p.rapidapi.com")
                .build();

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull final Call call, @NonNull IOException e) {
                        // Error
                    }
                    @Override
                    public void onResponse(@NonNull Call call, @NonNull final Response response) throws IOException {
                        try {
                            JSONObject weather = new JSONObject(response.body().string());
                            int data = Integer.parseInt(weather.getJSONArray("data").getJSONObject(0).getString("precip"));
                            isRaining = data > 0;
                            Log.v("Weather", String.valueOf(isRaining));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
