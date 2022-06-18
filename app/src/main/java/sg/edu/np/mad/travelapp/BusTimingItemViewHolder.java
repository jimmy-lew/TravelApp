package sg.edu.np.mad.travelapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BusTimingItemViewHolder extends RecyclerView.ViewHolder {

    ImageView busImage;
    TextView busTime;

    public BusTimingItemViewHolder(@NonNull View itemView) {
        super(itemView);

        busImage = itemView.findViewById(R.id.busImageView);
        busTime = itemView.findViewById(R.id.incomingBusTime);
    }
}
