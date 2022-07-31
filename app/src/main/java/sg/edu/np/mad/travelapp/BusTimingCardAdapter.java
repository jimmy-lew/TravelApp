package sg.edu.np.mad.travelapp;

import android.annotation.SuppressLint;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import sg.edu.np.mad.travelapp.data.model.BusStop;
import sg.edu.np.mad.travelapp.data.model.User;
import sg.edu.np.mad.travelapp.data.repository.BusStopRepository;

@SuppressLint("NotifyDataSetChanged")
public class BusTimingCardAdapter extends RecyclerView.Adapter<BusTimingCardAdapter.ViewHolder> {

    private final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool(); // Allows for sharing of view with nested recyclers
    private ArrayList<BusStop> busStopList;
    private User user;
    private final DatabaseReference REF = FirebaseDatabase.getInstance().getReference("users");

    public BusTimingCardAdapter(){
        busStopList = new ArrayList<BusStop>();
        user = new User("1", new ArrayList<String>());
    }

    public BusTimingCardAdapter(ArrayList<BusStop> busStopList){
        this.busStopList = busStopList;
        user = new User("1", new ArrayList<String>());
    }

    public void setBusStopList(ArrayList<BusStop> busStopList) {
        this.busStopList = busStopList;
        this.notifyDataSetChanged();
    }

    public void setUser(User user) {
        this.user = user;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.bus_timing_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) { holder.onBind(position); }

    @Override
    public int getItemCount() { return busStopList.size(); }

    class ViewHolder extends RecyclerView.ViewHolder {

        CardView rootView;

        TextView stopNameTextView, stopIDTextView;
        ImageView lateImageView, weatherImageView, favouriteImageView2, refreshImageView;
        Group hiddenGroup;

        RecyclerView busRecycler;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            stopNameTextView = itemView.findViewById(R.id.locationTextView);
            stopIDTextView = itemView.findViewById(R.id.addressTextView);

            lateImageView = itemView.findViewById(R.id.isLateImageView);
            weatherImageView = itemView.findViewById(R.id.isRainingImageView);
            favouriteImageView2 = itemView.findViewById(R.id.favouriteImgView);
            refreshImageView = itemView.findViewById(R.id.busTimingRefresh);

            rootView = itemView.findViewById(R.id.busTimingCardView);
            hiddenGroup = itemView.findViewById(R.id.card_group);
            busRecycler = itemView.findViewById(R.id.busTimingRecyclerView);
        }

        protected void onBind(int position) {
            BusStop busStop = busStopList.get(position);
            BusTimingRowAdapter busTimingRowAdapter = new BusTimingRowAdapter(busStop.getServiceList());
            ArrayList<String> favouritesList = user.getFavouritesList();

            boolean isFavourite = favouritesList.contains(busStop.getName());

            stopNameTextView.setText(busStop.getName());
            stopIDTextView.setText(busStop.getCode());

            /* Toggle on/off favourite status */
            favouriteImageView2.setImageResource(isFavourite ? R.drawable.favorite : R.drawable.favorite_inactive);

            /* Toggle on/off favourite status and update RTDB */
            favouriteImageView2.setOnClickListener(view -> {
                favouriteImageView2.setImageResource(isFavourite ? R.drawable.favorite_inactive : R.drawable.favorite);
                if (isFavourite) favouritesList.remove(busStop.getName());
                else favouritesList.add(busStop.getName());

                user.setFavouritesList(favouritesList);
                REF.child(user.getUserID()).setValue(user);
            });

            /* Card expand & collapse functionality */
            rootView.setOnClickListener(view -> {
                boolean isVisible = hiddenGroup.getVisibility() == View.VISIBLE;
                TransitionManager.beginDelayedTransition(rootView, new AutoTransition());
                hiddenGroup.setVisibility(isVisible ? View.GONE : View.VISIBLE);
                favouriteImageView2.setVisibility(isVisible ? View.VISIBLE : View.GONE);
            });

            /* Refresh bus timings */
            refreshImageView.setOnClickListener(view -> {
                String query = busStop.getCode();
                BusStopRepository.getInstance().getBusStopTimings(query, busTimingRowAdapter::setServiceList);
            });

            /* Setup nested recycler */
            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    busRecycler.getContext(),
                    LinearLayoutManager.VERTICAL,
                    false
            );

            layoutManager.setInitialPrefetchItemCount(busStop.getServiceList().size());

            busRecycler.setLayoutManager(layoutManager);
            busRecycler.setAdapter(busTimingRowAdapter);
            busRecycler.setRecycledViewPool(viewPool);
        }
    }
}