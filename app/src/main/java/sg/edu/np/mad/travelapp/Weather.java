package sg.edu.np.mad.travelapp;

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

public class Weather{

    public Weather() {
    }

    // --- Function to check the weather status at a particular bus stop (True [Rainy], False [Not Rainy]---
    public void checkWeatherStatus(String lon, String lat) {
        OkHttpClient client = new OkHttpClient();

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
                        // decoding of Weather API Response
                        try {
                            JSONObject weather = new JSONObject(response.body().string());
                            int data = Integer.parseInt(weather.getJSONArray("data").getJSONObject(0).getString("precip"));
                            Log.v("WEATHER","Weather " + data);

                            /*
                            // key indicator of rain is precip, when precip > 0, there is rain
                            if (data > 0) {

                            } else {

                            }
                            */
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


    }
}
