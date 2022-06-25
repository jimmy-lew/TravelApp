package sg.edu.np.mad.travelapp;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sg.edu.np.mad.travelapp.data.model.User;
import sg.edu.np.mad.travelapp.data.repository.BusStopRepository;
import sg.edu.np.mad.travelapp.ui.BaseActivity;

public class ViewFavourites extends BaseActivity {
    private BusTimingCardAdapter adapter = new BusTimingCardAdapter();
    private ArrayList<String> query = new ArrayList<>();
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_favourites);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Location location = getIntent().getParcelableExtra(LOCATION);

        initializeNavbar(location);
        initializeRecycler(adapter, findViewById(R.id.favouriteStopsRecycler), false);

        ref.child("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                query = user.getFavouritesList();
                BusStopRepository.get_instance().getBusStopsByName(query, busStopList -> {
                    adapter.setUser(user);
                    adapter.setBusStopList(busStopList);
                    adapter.notifyDataSetChanged();
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ref.child("1").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
                return;
            }

            User user = task.getResult().getValue(User.class);
            query = user.getFavouritesList();
            BusStopRepository.get_instance().getBusStopsByName(query, busStopList -> {
                adapter.setUser(user);
                adapter.setBusStopList(busStopList);
                adapter.notifyDataSetChanged();
            });
        });
    }
}