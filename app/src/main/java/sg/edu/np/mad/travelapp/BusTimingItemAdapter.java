package sg.edu.np.mad.travelapp;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

import sg.edu.np.mad.travelapp.data.model.Bus;

public class BusTimingItemAdapter extends RecyclerView.Adapter<BusTimingItemAdapter.ViewHolder> {

    private final ArrayList<Bus> busList;
    private final Context context;

    public BusTimingItemAdapter(ArrayList<Bus> busList, Context context) {
        this.busList = busList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.bus_timing_card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) { holder.onBind(position); }

    @Override
    public int getItemCount() { return busList.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView busImage;
        TextView busTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            busImage = itemView.findViewById(R.id.busImageView);
            busTime = itemView.findViewById(R.id.incomingBusTime);
        }

        @SuppressLint("ResourceType")
        protected void onBind(int position){
            Bus bus = busList.get(position);
            busTime.setText(bus.getEstimatedTime());

            String busType = bus.getType().toLowerCase();
            String busLoad = bus.getLoad().toLowerCase();

            String fileName = String.format("%s_%s", busType, busLoad);

            int busIconID = context.getResources()
                    .getIdentifier(
                            fileName,
                            "drawable",
                            context.getPackageName()
                    );

            busImage.setImageResource(busIconID);
        }
    }
}
