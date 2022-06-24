package sg.edu.np.mad.travelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sg.edu.np.mad.travelapp.data.model.BusStop;
import sg.edu.np.mad.travelapp.data.model.User;
import sg.edu.np.mad.travelapp.data.repository.BusStopRepository;

public class ViewFavourites extends AppCompatActivity {
    private final String TAG = "ViewFavouritesActivity";
    private BusTimingCardAdapter busTimingCardAdapter;
    private ArrayList<String> query = new ArrayList<>();
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_favourites);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        //Location location = getIntent().getParcelableExtra("location");

        renderUI(new ArrayList<BusStop>(), new User("1", new ArrayList<String>()));

        ref.child("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                query = user.getFavouritesList();
                Log.v("Query", String.valueOf(query.size()));
                BusStopRepository.get_instance().getBusStopsByName(query, busStopList -> {
                    busTimingCardAdapter.setUser(user);
                    busTimingCardAdapter.setBusStopList(busStopList);
                    busTimingCardAdapter.notifyDataSetChanged();
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ref.child("1").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    User user = task.getResult().getValue(User.class);
                    query = user.getFavouritesList();
                    BusStopRepository.get_instance().getBusStopsByName(query, busStopList -> {
                        busTimingCardAdapter.setUser(user);
                        busTimingCardAdapter.setBusStopList(busStopList);
                        busTimingCardAdapter.notifyDataSetChanged();
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
    }

    public void renderUI(ArrayList<BusStop> busStopList, User user){
        this.runOnUiThread(() -> {
            RecyclerView busStopRecycler = this.findViewById(R.id.favouriteStopsRecycler);
            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    getApplicationContext()
            );
            busTimingCardAdapter = new BusTimingCardAdapter(busStopList, user);
            busStopRecycler.setLayoutManager(layoutManager);
            busStopRecycler.setAdapter(busTimingCardAdapter);
        });
    }
}