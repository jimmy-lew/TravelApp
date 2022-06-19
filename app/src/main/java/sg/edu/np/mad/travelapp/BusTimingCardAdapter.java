package sg.edu.np.mad.travelapp;

import android.provider.ContactsContract;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import sg.edu.np.mad.travelapp.data.model.BusStop;
import sg.edu.np.mad.travelapp.data.model.User;

public class BusTimingCardAdapter extends RecyclerView.Adapter<BusTimingCardViewHolder> {

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private ArrayList<BusStop> busStopList;

    public BusTimingCardAdapter(ArrayList<BusStop> busStopList){
        this.busStopList = busStopList;
    }

    @NonNull
    @Override
    public BusTimingCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.bus_timing_card,
                parent,
                false
        );

        return new BusTimingCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusTimingCardViewHolder holder, int position) {
        BusStop busStop = busStopList.get(position);
        TextView busStopName = holder.stopNameTextView;
        TextView busStopID = holder.stopIDTextView;
        ImageView favouriteImageView = holder.favouriteImageView2;

        // Create User object and favouritesList list
        User user = new User();
        ArrayList<String> favouritesList = new ArrayList<>();

        // Add objects to favouritesList
        favouritesList.add(busStop.RoadName);

        // Connect to database
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference reff = db.getReference();


        favouriteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.setUserID("2");
                user.setFavouritesList(favouritesList);
                reff.child("users").push().setValue(user);
            }
        });
        holder.rootView.setOnClickListener(view -> {
            if (holder.hiddenGroup.getVisibility() == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(holder.rootView, new AutoTransition());
                holder.hiddenGroup.setVisibility(View.GONE);
                holder.favouriteImageView2.setVisibility(View.VISIBLE);
            } else {
                TransitionManager.beginDelayedTransition(holder.rootView, new AutoTransition());
                holder.hiddenGroup.setVisibility(View.VISIBLE);
                holder.favouriteImageView2.setVisibility(View.GONE);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.busRecycler.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );

        layoutManager.setInitialPrefetchItemCount(busStopList.size());

        BusTimingRowAdapter busTimingRowAdapter = new BusTimingRowAdapter(busStop.getServiceList());
        holder.busRecycler.setLayoutManager(layoutManager);
        holder.busRecycler.setAdapter(busTimingRowAdapter);
        holder.busRecycler.setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount() {
        return busStopList.size();
    }
}
