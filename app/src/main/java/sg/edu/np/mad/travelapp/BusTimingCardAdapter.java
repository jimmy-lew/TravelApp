package sg.edu.np.mad.travelapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

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
                R.layout.bus_timing_card_expanded,
                parent,
                false
        );

        return new BusTimingCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusTimingCardViewHolder holder, int position) {
        BusStop busStop = busStopList.get(position);

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
