package sg.edu.np.mad.travelapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Array;
import java.util.ArrayList;

public class BusTimingItemAdapter extends RecyclerView.Adapter<BusTimingItemViewHolder> {

    private ArrayList<Bus> busList;

    public BusTimingItemAdapter(ArrayList<Bus> busList){
        this.busList = busList;
    }

    @NonNull
    @Override
    public BusTimingItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.bus_timing_card_item_regularbus,
                parent,
                false
        );

        return new BusTimingItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusTimingItemViewHolder holder, int position) {
        Bus bus = busList.get(position);
        holder.busTime.setText(bus.getTiming());

        //TODO: Implement different bus type icons
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
