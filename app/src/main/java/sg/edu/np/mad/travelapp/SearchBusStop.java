package sg.edu.np.mad.travelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;

import java.util.ArrayList;

import sg.edu.np.mad.travelapp.data.model.BusStop;
import sg.edu.np.mad.travelapp.data.model.User;
import sg.edu.np.mad.travelapp.data.repository.BusStopRepository;

public class SearchBusStop extends AppCompatActivity {

    private View decorView;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bus_stop);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        ImageView favIcon = findViewById(R.id.favIcon);
        ImageView homeIcon = findViewById(R.id.homeIcon);
        ImageView nearbyIcon = findViewById(R.id.nearbyIcon);

        ArrayList<String> query = new ArrayList<>();
        query.add(getIntent().getStringExtra("query"));
        Location location = getIntent().getParcelableExtra("location");

        ref.child("1").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    User user = task.getResult().getValue(User.class);
                    BusStopRepository.get_instance().getBusStopsByName(query, busStopList -> {
                        renderUI(busStopList, user);
                    });
                }
            }
        });

        nearbyIcon.setOnClickListener(view -> {
            Intent ViewBusStops = new Intent(getApplicationContext(), ViewBusStops.class);
            ViewBusStops.putExtra("location", location);
            startActivity(ViewBusStops);
        });
        homeIcon.setOnClickListener(view -> {
            Intent MainActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(MainActivity);
        });
        favIcon.setOnClickListener(view -> {
            Intent ViewFavourites = new Intent(getApplicationContext(), ViewFavourites.class);
            ViewFavourites.putExtra("location", location);
            startActivity(ViewFavourites);
        });

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(visibility -> {
            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                decorView.setSystemUiVisibility(hideSystemBars());
            }
        });
    }

    public void renderUI(ArrayList<BusStop> busStopList, User user){
        this.runOnUiThread(() -> {
            RecyclerView busStopRecycler = this.findViewById(R.id.searchedBusRecycler);
            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    getApplicationContext()
            );
            BusTimingCardAdapter busTimingCardAdapter = new BusTimingCardAdapter(busStopList, user);
            busStopRecycler.setLayoutManager(layoutManager);
            busStopRecycler.setAdapter(busTimingCardAdapter);
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        {
            // If there is focus on the window, hide the status bar and navigation bar.
            if (hasFocus) {
                decorView.setSystemUiVisibility(hideSystemBars());
            }
        }
    }

    public int hideSystemBars() {
        // Use Bitwise Operators to combine the flags
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    }
}