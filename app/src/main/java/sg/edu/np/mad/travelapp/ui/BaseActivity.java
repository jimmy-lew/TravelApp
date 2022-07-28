package sg.edu.np.mad.travelapp.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Granularity;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import sg.edu.np.mad.travelapp.NavbarFragment;
import sg.edu.np.mad.travelapp.R;
import sg.edu.np.mad.travelapp.data.repository.BusStopRepository;

/**
 * Activity super class containing common functionality such as
 * recycler view & navbar fragment initialization
 */
public class BaseActivity extends AppCompatActivity {
    /* Intent TAG declarations */
    protected static final String LOCATION = "location";
    protected static final int FINE_LOCATION_CODE = 100;
    protected final BusStopRepository REPO = BusStopRepository.getInstance();
    protected final DatabaseReference REF = FirebaseDatabase.getInstance().getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    protected void initializeRecycler(RecyclerView.Adapter adapter, RecyclerView recycler, boolean isHorizontal) {
        this.runOnUiThread(() -> {
            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    getApplicationContext(),
                    isHorizontal ? RecyclerView.HORIZONTAL : RecyclerView.VERTICAL,
                    false
            );
            recycler.setLayoutManager(layoutManager);
            recycler.setAdapter(adapter);
        });
    }

    protected void initializeNavbar(Location location) {
        Bundle args = new Bundle();
        args.putParcelable(LOCATION, location);

        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.navFragmentContainerView, NavbarFragment.class, args)
                .commit();
    }

    protected void checkPermission(String permission, int requestCode)
    {
        boolean isNotGranted = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED;

        if (isNotGranted) { ActivityCompat.requestPermissions(this, new String[] { permission }, requestCode); }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);

        boolean isGranted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        Toast.makeText(this, String.format("Location permission %s", isGranted ? "granted. Please refresh app" : "denied"), Toast.LENGTH_LONG).show();
    }

    @SuppressLint("MissingPermission")
    protected void getUserLocation(final OnComplete<Location> onComplete) {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        CurrentLocationRequest request = new CurrentLocationRequest.Builder()
                .setDurationMillis(Long.MAX_VALUE)
                .setGranularity(Granularity.GRANULARITY_FINE)
                .setMaxUpdateAgeMillis(0)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .build();

        checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, FINE_LOCATION_CODE);

        fusedLocationClient.getCurrentLocation(request, null).addOnSuccessListener(this, location -> {
            if (location == null) return;
            onComplete.execute(location);
        });
    }

    protected interface OnComplete<T>{
        void execute(T data);
    }
}