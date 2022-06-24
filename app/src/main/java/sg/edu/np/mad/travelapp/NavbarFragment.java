package sg.edu.np.mad.travelapp;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;



public class NavbarFragment extends Fragment {

    public NavbarFragment() {
        // Required empty public constructor
    }

    public static void newInstance(final FragmentActivity activity){
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragmentContainerView, NavbarFragment.class, null)
                .commit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            Log.v("args", getArguments().getString("key"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_navbar, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){

        //Location userLocation = getArguments().getParcelable("location");

        CardView homeOutCardView = getView().findViewById(R.id.homeOutCardView);
        CardView homeInCardView = getView().findViewById(R.id.homeInCardView);

        ImageView favIcon = getView().findViewById(R.id.favIcon);
        ImageView homeIcon = getView().findViewById(R.id.homeIcon);
        ImageView nearbyIcon = getView().findViewById(R.id.nearbyIcon);

        //TODO: Need add drop shadow for navbar
        homeOutCardView.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"));
        homeInCardView.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"));
        //TODO: Not sure how to remove drop shadow for inactive

        if(getActivity() instanceof MainActivity){
            homeIcon.setImageResource(R.drawable.home_active);
        }

        if(getActivity() instanceof ViewBusStops){
            nearbyIcon.setImageResource(R.drawable.nearby_active);
        }

        if(getActivity() instanceof  ViewFavourites){
            favIcon.setImageResource(R.drawable.favorite);
        }

        homeIcon.setOnClickListener(View -> {
            Intent MainActivity = new Intent(getActivity(), MainActivity.class);
            startActivity(MainActivity);
        });

        nearbyIcon.setOnClickListener(fragView -> {
            Intent ViewBusStops = new Intent(getActivity(), ViewBusStops.class);
            //ViewBusStops.putExtra("location", userLocation);
            startActivity(ViewBusStops);
        });

        favIcon.setOnClickListener(fragView -> {
            Intent ViewFavourites = new Intent(getActivity(), ViewFavourites.class);
            //ViewFavourites.putExtra("location", userLocation);
            startActivity(ViewFavourites);
        });
    }
}