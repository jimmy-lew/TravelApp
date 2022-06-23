package sg.edu.np.mad.travelapp;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private ArrayList<BusStop> busStopList = new ArrayList<>();
    private User user;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

    public BusTimingCardAdapter(){
        busStopList = new ArrayList<BusStop>();
        user = new User("1", new ArrayList<String>());
    }

    public BusTimingCardAdapter(ArrayList<BusStop> busStopList, User user){
        this.busStopList = busStopList;
        this.user = user;
    }

    public void setBusStopList(ArrayList<BusStop> busStopList) {
        this.busStopList = busStopList;
    }

    public void setUser(User user) {
        this.user = user;
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
        ArrayList<String> favouritesList = user.getFavouritesList();

        boolean isFavourite = favouritesList.contains(busStop.getName());

        holder.stopNameTextView.setText(busStop.getName());
        holder.stopIDTextView.setText(busStop.getCode());

        if (isFavourite) {
            holder.favouriteImageView2.setImageResource(R.drawable.favorite);
        }

        // Sends userid and favourite stop codes when favourite img is clicked
        holder.favouriteImageView2.setOnClickListener(view -> {
            if(favouritesList.contains(busStop.getName())) {
                holder.favouriteImageView2.setImageResource(R.drawable.favorite_inactive);
                favouritesList.remove(busStop.getName());
            }
            else {
                holder.favouriteImageView2.setImageResource(R.drawable.favorite);
                favouritesList.add(busStop.getName());
            }
            user.setFavouritesList(favouritesList);
            ref.child(user.getUserID()).setValue(user);
        });

        holder.rootView.setOnClickListener(view -> {
            if (holder.hiddenGroup.getVisibility() == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(holder.rootView, new AutoTransition());
                holder.hiddenGroup.setVisibility(View.GONE);
                holder.favouriteImageView2.setVisibility(View.VISIBLE);
            } else {
                TransitionManager.beginDelayedTransition(holder.rootView, new AutoTransition());
                Log.v("Expand", "Expanded");
                holder.hiddenGroup.setVisibility(View.VISIBLE);
                holder.favouriteImageView2.setVisibility(View.GONE);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.busRecycler.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );

        layoutManager.setInitialPrefetchItemCount(busStop.getServiceList().size());

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