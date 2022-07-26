package sg.edu.np.mad.travelapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.mad.travelapp.data.model.Bus;

public class BusTimingItemAdapter extends RecyclerView.Adapter<BusTimingItemAdapter.ViewHolder> {

    private ArrayList<Bus> busList;

    public BusTimingItemAdapter(ArrayList<Bus> busList){
        this.busList = busList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.bus_timing_card_item,
                parent,
                false
        );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return busList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView busImage;
        TextView busTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            busImage = itemView.findViewById(R.id.busImageView);
            busTime = itemView.findViewById(R.id.incomingBusTime);
        }

        protected void onBind(int position){
            Bus bus = busList.get(position);
            busTime.setText(bus.getEstimatedTime());

            switch(bus.getType()){
                case "BD":
                    busImage.setImageResource(R.drawable.bendy);
                    break;
                case "DD":
                    busImage.setImageResource(R.drawable.double_decker);
                    break;
                default:
                    break;
            }
        }
    }
}
