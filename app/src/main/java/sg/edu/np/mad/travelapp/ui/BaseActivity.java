package sg.edu.np.mad.travelapp.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Granularity;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import sg.edu.np.mad.travelapp.NavbarFragment;
import sg.edu.np.mad.travelapp.R;

public class BaseActivity extends AppCompatActivity {
    public static final String LOCATION = "location";

    public void initializeRecycler(RecyclerView.Adapter adapter, RecyclerView recycler, boolean isHorizontal) {
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

    public void initializeNavbar(Location location) {
        Bundle args = new Bundle();
        args.putParcelable(LOCATION, location);

        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.navFragmentContainerView, NavbarFragment.class, args)
                .commit();
    }

    @SuppressLint("MissingPermission")
    public void getUserLocation(final OnComplete<Location> onComplete) {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        CurrentLocationRequest request = new CurrentLocationRequest.Builder()
                .setDurationMillis(Long.MAX_VALUE)
                .setGranularity(Granularity.GRANULARITY_FINE)
                .setMaxUpdateAgeMillis(0)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .build();

        fusedLocationClient.getCurrentLocation(request, null).addOnSuccessListener(this, location -> {
            if (location == null) return;
            onComplete.execute(location);
        });
    }

    public interface OnComplete<T>{
        void execute(T data);
    }
}