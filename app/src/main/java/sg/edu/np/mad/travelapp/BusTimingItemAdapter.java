package sg.edu.np.mad.travelapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.mad.travelapp.data.model.Bus;

public class BusTimingItemAdapter extends RecyclerView.Adapter<BusTimingItemViewHolder> {

    private ArrayList<Bus> busList;

    public BusTimingItemAdapter(ArrayList<Bus> busList){
        this.busList = busList;
    }

    @NonNull
    @Override
    public BusTimingItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.bus_timing_card_item,
                parent,
                false
        );

        return new BusTimingItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusTimingItemViewHolder holder, int position) {
        Bus bus = busList.get(position);
        holder.busTime.setText(bus.getEstimatedTime());

        switch(bus.getType()){
            case "BD":
                holder.busImage.setImageResource(R.drawable.bendy);
                break;
            case "DD":
                holder.busImage.setImageResource(R.drawable.double_decker);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return busList.size();
    }
}
