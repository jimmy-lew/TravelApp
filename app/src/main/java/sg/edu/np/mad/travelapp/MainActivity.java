package sg.edu.np.mad.travelapp;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.ImageButton;

import androidx.annotation.NonNull;

import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sg.edu.np.mad.travelapp.data.model.User;
import sg.edu.np.mad.travelapp.data.repository.BusStopRepository;
import sg.edu.np.mad.travelapp.ui.BaseActivity;

public class MainActivity extends BaseActivity {
    private final BusTimingCardAdapter nearbyAdapter = new BusTimingCardAdapter();
    private final BusTimingCardAdapter favouritesAdapter = new BusTimingCardAdapter();

    private Location userLocation;
    private ArrayList<String> query;

    private static final CharacterStyle STYLE_BOLD = new StyleSpan(Typeface.BOLD);
    private final ArrayList<SpannableString> predictionsList = new ArrayList<>();

    @SuppressLint({"MissingPermission", "NotifyDataSetChanged"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton searchButton = findViewById(R.id.mainSearchButton);

        initializeRecycler(nearbyAdapter, findViewById(R.id.nearbyRecyclerView), true);
        initializeRecycler(favouritesAdapter, findViewById(R.id.favouriteStopsRecyclerView), true);

        /* Gets user location and passes into callback to get list of nearby bus stops, their serices, their respective timings
        and update adapter's information to display on activity*/
        getUserLocation(location -> {
            userLocation = location;
            initializeNavbar(location);
            BusStopRepository.get_instance().getNearbyBusStops(location, busStopList -> {
                nearbyAdapter.setBusStopList(busStopList);
                nearbyAdapter.notifyDataSetChanged();
            });
        });

        /* Listens to changes in user favourites and updates adapters accordingly */
        REF.child("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                nearbyAdapter.setUser(user);
                favouritesAdapter.setUser(user);

                assert user != null;

                query = user.getFavouritesList();
                BusStopRepository.get_instance().getBusStopsByName(query, busStopList -> {
                    favouritesAdapter.setBusStopList(busStopList);
                    favouritesAdapter.notifyDataSetChanged();
                    nearbyAdapter.notifyDataSetChanged();
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        /* --- Autocomplete Suggestions --- */
        Places.initialize(getApplicationContext(), GetAPIKey());
        PlacesClient placesClient = Places.createClient(this);
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
        AutoCompleteTextView searchTextBox = findViewById(R.id.mainSearchTextbox);
        ArrayAdapter<SpannableString> arrayAdapter = new ArrayAdapter<SpannableString>(this, android.R.layout.simple_list_item_1, predictionsList);
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
        searchButton.setOnClickListener(view -> {
            String searchQuery = searchTextBox.getText().toString();
            Intent SearchBusStop = new Intent(getApplicationContext(), SearchBusStop.class);
            SearchBusStop.putExtra("query", searchQuery);
            SearchBusStop.putExtra(LOCATION, userLocation);
            startActivity(SearchBusStop);
        });
    }

    // TODO: Generalize
    private String GetAPIKey(){
        try{
            ApplicationInfo info = this.getApplicationContext()
                    .getPackageManager()
                    .getApplicationInfo(this.getApplicationContext().getPackageName(),
                            PackageManager.GET_META_DATA);
            String key = info.metaData.get("MAP_KEY").toString();
            return key;
        }
        catch(Exception e){
            return null;
        }
    }
}