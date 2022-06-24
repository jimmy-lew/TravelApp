package sg.edu.np.mad.travelapp;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class ViewBusStops extends AppCompatActivity{

    private final String TAG = "ViewBusStopActivity";
    private ArrayList<BusStop> data = new ArrayList<>();
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bus_stops);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        location = getIntent().getParcelableExtra("location");

        ImageView homeIcon = findViewById(R.id.homeIcon);
        ImageView nearbyIcon = findViewById(R.id.nearbyIcon);
        ImageView favIcon = findViewById(R.id.favIcon);

        nearbyIcon.setImageResource(R.drawable.nearby_active);

        ref.child("1").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    User user = task.getResult().getValue(User.class);
                    BusStopRepository.get_instance().getNearbyBusStops(location, busStopList -> {
                        renderUI(busStopList, user);
                    });
                }
            }
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
    }

    public void renderUI(ArrayList<BusStop> busStopList, User user){
        this.runOnUiThread(() -> {
            RecyclerView busStopRecycler = this.findViewById(R.id.nearbyBusRecycler);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            BusTimingCardAdapter busTimingCardAdapter = new BusTimingCardAdapter(busStopList, user);
            busStopRecycler.setLayoutManager(layoutManager);
            busStopRecycler.setAdapter(busTimingCardAdapter);
        });
    }
}