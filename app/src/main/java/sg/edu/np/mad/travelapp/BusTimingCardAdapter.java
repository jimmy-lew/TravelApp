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

import java.util.ArrayList;

import sg.edu.np.mad.travelapp.data.model.BusStop;

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


        String debug = String.format("Name: %s \nCode: %s\nServiceList: ", busStop.getBusStopName(), busStop.getBusStopCode());
        for (int i = 0; i < busStop.getServiceList().size(); i++){
            debug = String.format("%s %s", debug, busStop.getServiceList().get(i).ServiceNo);
        }

        Log.v("CardAdapter", debug);

        holder.stopNameTextView.setText(busStop.getBusStopName());
        holder.stopIDTextView.setText(busStop.getBusStopCode());

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