package sg.edu.np.mad.travelapp;

import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import sg.edu.np.mad.travelapp.data.model.SimpleLocation;
import sg.edu.np.mad.travelapp.data.model.User;
import sg.edu.np.mad.travelapp.ui.BaseActivity;

public class Search extends BaseActivity {
    private final RouteAdapter adapter = new RouteAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        String query = getIntent().getStringExtra("query");
        Location location = getIntent().getParcelableExtra(LOCATION);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        initializeNavbar(location);
        initializeRecycler(adapter, findViewById(R.id.searchRecycler), false);

        ROUTE_REPO.getRouteByName(SimpleLocation.fromAndroidLocation(location), query, adapter::setRouteList);

        REF.child(currentUser.getUid()).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) return;
            User user = task.getResult().getValue(User.class);
            adapter.setUser(user);
        });
    }
}