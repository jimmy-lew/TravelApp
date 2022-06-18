package sg.edu.np.mad.travelapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BusTimingRowAdapter extends RecyclerView.Adapter<BusTimingRowViewHolder> {

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private ArrayList<Service> serviceList;

    public BusTimingRowAdapter(ArrayList<Service> serviceList){
        this.serviceList = serviceList;
    }

    @NonNull
    @Override
    public BusTimingRowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.bus_timing_card_row,
                parent,
                false
        );

        return new BusTimingRowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusTimingRowViewHolder holder, int position) {
        Service service = serviceList.get(position);

        holder.busNumber.setText(service.ServiceNo);

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.busTimingRecycler.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );

        layoutManager.setInitialPrefetchItemCount(service.getBusList().size());

        BusTimingItemAdapter busTimingItemAdapter = new BusTimingItemAdapter(service.busList);
        holder.busTimingRecycler.setLayoutManager(layoutManager);
        holder.busTimingRecycler.setAdapter(busTimingItemAdapter);
        holder.busTimingRecycler.setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }
}
