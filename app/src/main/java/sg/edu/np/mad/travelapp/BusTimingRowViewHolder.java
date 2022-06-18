package sg.edu.np.mad.travelapp;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BusTimingRowViewHolder extends RecyclerView.ViewHolder {

    TextView busNumber;
    RecyclerView busTimingRecycler;

    public BusTimingRowViewHolder(@NonNull View itemView) {
        super(itemView);

        busNumber = itemView.findViewById(R.id.incomingBusNumber);
        busTimingRecycler = itemView.findViewById(R.id.busTimingRowRecyclerView);
    }
}
