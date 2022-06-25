package sg.edu.np.mad.travelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Granularity;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

import sg.edu.np.mad.travelapp.data.model.User;
import sg.edu.np.mad.travelapp.data.repository.BusStopRepository;
import sg.edu.np.mad.travelapp.ui.BaseActivity;

public class MainActivity extends BaseActivity {
    private final String TAG  = "Main";
    private ArrayList<String> query;
    private final BusTimingCardAdapter nearbyAdapter = new BusTimingCardAdapter();
    private final BusTimingCardAdapter favouritesAdapter = new BusTimingCardAdapter();
    private final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
    private Location userLocation = new Location("");
    private static final CharacterStyle STYLE_BOLD = new StyleSpan(Typeface.BOLD);
    private ArrayList<SpannableString> predictionsList = new ArrayList<>();

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Initialize the SDK
        Places.initialize(getApplicationContext(), GetAPIKey());

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);


        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        ImageButton searchButton = findViewById(R.id.mainSearchButton);
        AutoCompleteTextView searchTextBox = findViewById(R.id.mainSearchTextbox);

        initializeRecycler(nearbyAdapter, findViewById(R.id.nearbyRecyclerView), true);
        initializeRecycler(favouritesAdapter, findViewById(R.id.favouriteStopsRecyclerView), true);

//        getUserLocation(location -> {
//            userLocation = location;
//            initializeNavbar(location);
//            BusStopRepository.get_instance().getNearbyBusStops(location, busStopList -> {
//                nearbyAdapter.setBusStopList(busStopList);
//                nearbyAdapter.notifyDataSetChanged();
//            });
//        });

        Location location = new Location("");
        location.setLatitude(1.3918577281406086);
        location.setLongitude(103.75166620390048);

        initializeNavbar(location);
        BusStopRepository.get_instance().getNearbyBusStops(location, busStopList -> {
            nearbyAdapter.setBusStopList(busStopList);
            nearbyAdapter.notifyDataSetChanged();
        });

        ref.child("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                nearbyAdapter.setUser(user);
                favouritesAdapter.setUser(user);

                query = user.getFavouritesList();
                BusStopRepository.get_instance().getBusStopsByName(query, busStopList -> {
                    favouritesAdapter.setBusStopList(busStopList);
                    favouritesAdapter.notifyDataSetChanged();
                    nearbyAdapter.notifyDataSetChanged();
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
            nearbyAdapter.setUser(user);
            favouritesAdapter.setUser(user);

            query = user.getFavouritesList();
            BusStopRepository.get_instance().getBusStopsByName(query, busStopList -> {
                favouritesAdapter.setBusStopList(busStopList);
                favouritesAdapter.notifyDataSetChanged();
                nearbyAdapter.notifyDataSetChanged();
            });
        });

        searchButton.setOnClickListener(view -> {
            String searchQuery = searchTextBox.getText().toString();
            Intent SearchBusStop = new Intent(getApplicationContext(), SearchBusStop.class);
            SearchBusStop.putExtra("query", searchQuery);
            SearchBusStop.putExtra(LOCATION, userLocation);
            startActivity(SearchBusStop);
        });

        // --- Autocomplete Suggestions ---
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.mainSearchTextbox);
        ArrayAdapter<SpannableString> arrayAdapter = new ArrayAdapter<SpannableString>(this, android.R.layout.simple_list_item_1, predictionsList);


        // --- Search Debounce ---
        long delay = 500; // 3 seconds after user stops typing
        long last_text_edit = 0;
        Handler handler = new Handler();

        Runnable CheckInputFinish = new Runnable() {
            public void run() {
                if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                    // Use the builder to create a FindAutocompletePredictionsRequest.
                    FindAutocompletePredictionsRequest predictRequest = FindAutocompletePredictionsRequest.builder()
                            .setCountries("SG")
                            .setTypeFilter(TypeFilter.ADDRESS)
                            .setSessionToken(token)
                            .setQuery(searchTextBox.getText().toString())
                            .build();

                    Log.v(TAG, "api log" + predictRequest);
                    placesClient.findAutocompletePredictions(predictRequest).addOnSuccessListener((FindAutocompletePredictionsResponse) -> {
                        for (AutocompletePrediction prediction : FindAutocompletePredictionsResponse.getAutocompletePredictions()) {
                            Log.i(TAG, prediction.getPlaceId());
                            Log.i(TAG, prediction.getPrimaryText(null).toString());
                            predictionsList.add(prediction.getFullText(STYLE_BOLD));
                        }
                    }).addOnFailureListener((exception) -> {
                        if (exception instanceof ApiException) {
                            ApiException apiException = (ApiException) exception;
                            Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                        }
                    });
                    Log.v(TAG, "Stopped");
                    autoCompleteTextView.setAdapter(arrayAdapter);
                }
            }
        };

        searchTextBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

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

    // --- Get API KEY from local.properties/AndroidManifest ---
    public String GetAPIKey(){
        try{
            ApplicationInfo info = this.getApplicationContext()
                    .getPackageManager()
                    .getApplicationInfo(this.getApplicationContext().getPackageName(),
                            PackageManager.GET_META_DATA);
            String key = info.metaData.get("MAP_KEY").toString();
            return key;
        }
        catch(Exception e){
            Log.e("API Key", e.getMessage());
            return null;
        }
    }

}