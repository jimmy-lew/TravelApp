package sg.edu.np.mad.travelapp;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class NavbarFragment extends Fragment {

    private Location userLocation;

    public NavbarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            userLocation = getArguments().getParcelable("location");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_navbar, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        CardView homeOutCardView = getView().findViewById(R.id.homeOutCardView);
        CardView homeInCardView = getView().findViewById(R.id.homeInCardView);

        ImageView favIcon = getView().findViewById(R.id.favIcon);
        ImageView homeIcon = getView().findViewById(R.id.homeIcon);
        ImageView nearbyIcon = getView().findViewById(R.id.nearbyIcon);

        homeOutCardView.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"));
        homeInCardView.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"));

        // TODO: Migrate to interface

        if(getActivity() instanceof MainActivity){
            homeIcon.setImageResource(R.drawable.home_active);
        } else if(getActivity() instanceof ViewBusStops){
            nearbyIcon.setImageResource(R.drawable.nearby_active);
        } else if (getActivity() instanceof  ViewFavourites){
            favIcon.setImageResource(R.drawable.favorite);
        }

        homeIcon.setOnClickListener(fragView -> {
            Intent MainActivity = new Intent(getActivity(), MainActivity.class);
            startActivity(MainActivity);
        });

        nearbyIcon.setOnClickListener(fragView -> {
            Intent ViewBusStops = new Intent(getActivity(), ViewBusStops.class);
            ViewBusStops.putExtra("location", userLocation);
            startActivity(ViewBusStops);
        });

        favIcon.setOnClickListener(fragView -> {
            Intent ViewFavourites = new Intent(getActivity(), ViewFavourites.class);
            ViewFavourites.putExtra("location", userLocation);
            startActivity(ViewFavourites);
        });
    }
}