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
import sg.edu.np.mad.travelapp.ui.BaseActivity;

public class SearchBusStop extends BaseActivity {

    private final BusTimingCardAdapter adapter = new BusTimingCardAdapter();
    private final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
    private ArrayList<String> query = new ArrayList<>();
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bus_stop);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        query.add(getIntent().getStringExtra("query"));
        location = getIntent().getParcelableExtra(LOCATION);

        initializeNavbar(location);
        initializeRecycler(adapter, findViewById(R.id.searchedBusRecycler), false);

        ref.child("1").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    User user = task.getResult().getValue(User.class);
                    BusStopRepository.get_instance().getBusStopsByName(query, busStopList -> {
                        adapter.setUser(user);
                        adapter.setBusStopList(busStopList);
                        adapter.notifyDataSetChanged();
                    });
                }
            }
        });
    }
}