package sg.edu.np.mad.travelapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver;

import java.util.ArrayList;

import sg.edu.np.mad.travelapp.data.model.Service;

public class BusTimingRowAdapter extends RecyclerView.Adapter<BusTimingRowAdapter.ViewHolder> {

    private final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool(); // Allows for sharing of view with nested recyclers
    private ArrayList<Service> serviceList;

    public BusTimingRowAdapter(ArrayList<Service> serviceList){
        this.serviceList = serviceList;
    }

    public void setServiceList(ArrayList<Service> serviceList) {
        this.serviceList = serviceList;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.bus_timing_card_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) { holder.onBind(position); }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView busNumber;
        RecyclerView busTimingRecycler;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            busNumber = itemView.findViewById(R.id.incomingBusNumber);
            busTimingRecycler = itemView.findViewById(R.id.busTimingRowRecyclerView);
        }

        protected void onBind(int position) {
            Service service = serviceList.get(position);

            busNumber.setText(service.getServiceNo());

            /* Set up nested recycler view */
            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    busTimingRecycler.getContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
            );

            layoutManager.setInitialPrefetchItemCount(service.getBusList().size());

            BusTimingItemAdapter busTimingItemAdapter = new BusTimingItemAdapter(service.getBusList(), busTimingRecycler.getContext());
            busTimingRecycler.setLayoutManager(layoutManager);
            busTimingRecycler.setAdapter(busTimingItemAdapter);
            busTimingRecycler.setRecycledViewPool(viewPool);
        }
    }
}
