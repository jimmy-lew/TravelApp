package sg.edu.np.mad.travelapp.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Granularity;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import sg.edu.np.mad.travelapp.NavbarFragment;
import sg.edu.np.mad.travelapp.R;
import sg.edu.np.mad.travelapp.data.repository.BusStopRepository;
import sg.edu.np.mad.travelapp.data.repository.RouteRepository;

/**
 * Activity super class containing common functionality such as
 * recycler view & navbar fragment initialization
 */
public class BaseActivity extends AppCompatActivity {
    /* Intent TAG declarations */
    protected static final String LOCATION = "location";
    protected static final int FINE_LOCATION_CODE = 100;
    protected final BusStopRepository BUS_STOP_REPO = BusStopRepository.getInstance();
    protected final RouteRepository ROUTE_REPO = RouteRepository.getInstance();
    protected final DatabaseReference REF = FirebaseDatabase.getInstance().getReference("users");
    private final CharacterStyle STYLE_BOLD = new StyleSpan(Typeface.BOLD);

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

    private void checkPermission(String permission, int requestCode)
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

    protected String GetAPIKey(Context context, String keyName) {
        try{
            ApplicationInfo info = context.getApplicationContext()
                    .getPackageManager()
                    .getApplicationInfo(context.getApplicationContext().getPackageName(),PackageManager.GET_META_DATA);
            return info.metaData.get(keyName).toString();
        }
        catch(Exception e){
            return null;
        }
    }

    protected void InitializeGoogleAC(AutoCompleteTextView ACTV, Context AppContext, String APIKey){
        final ArrayList<SpannableString> predictionsList = new ArrayList<>();
        /* --- Autocomplete Suggestions --- */
        // Places.initialize(getApplicationContext(), GetAPIKey());
        Places.initialize(AppContext, GetAPIKey(AppContext, "API_KEY"));
        PlacesClient placesClient = Places.createClient(AppContext);
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
        AutoCompleteTextView searchTextBox = ACTV;
        ArrayAdapter<SpannableString> arrayAdapter = new ArrayAdapter<SpannableString>(AppContext.getApplicationContext(), android.R.layout.simple_list_item_1, predictionsList);
        searchTextBox.setAdapter(arrayAdapter);

        /* --- Search Debounce --- */
        long delay = 500; // delay to request
        long last_text_edit = 0;
        Handler handler = new Handler();

        /* Debouncer function to only send request to google api when user stops typing */
        Runnable CheckInputFinish = new Runnable() {
            public void run() {
                if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                    FindAutocompletePredictionsRequest predictRequest = FindAutocompletePredictionsRequest.builder()
                            .setCountries("SG")
                            .setSessionToken(token)
                            .setQuery(searchTextBox.getText().toString())
                            .build();

                    placesClient.findAutocompletePredictions(predictRequest).addOnSuccessListener((FindAutocompletePredictionsResponse) -> {
                        predictionsList.clear();
                        for (AutocompletePrediction prediction : FindAutocompletePredictionsResponse.getAutocompletePredictions()) {
                            SpannableString predictionString = prediction.getFullText(STYLE_BOLD);
                            predictionsList.add(predictionString);
                        }
                        arrayAdapter.clear();
                        arrayAdapter.addAll(predictionsList);
                        arrayAdapter.notifyDataSetChanged();

                    }).addOnFailureListener((exception) -> {
                        if (exception instanceof ApiException) {
                            ApiException apiException = (ApiException) exception;
                        }
                    });
                }
            }
        };

        // TODO: Implement observer pattern
        searchTextBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                //You need to remove this to run only once
                handler.removeCallbacks(CheckInputFinish);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //avoid triggering event when text is empty
                if (s.length() > 0) {
                    long last_text_edit = System.currentTimeMillis();
                    handler.postDelayed(CheckInputFinish, delay);
                }
            }
        });
    }

    protected interface OnComplete<T>{
        void execute(T data);
    }
}