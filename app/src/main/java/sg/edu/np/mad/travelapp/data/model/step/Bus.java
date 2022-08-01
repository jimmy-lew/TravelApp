package sg.edu.np.mad.travelapp.data.model.step;

import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.travelapp.ExpandedStepAdapter;
import sg.edu.np.mad.travelapp.MinifiedStepAdapter;
import sg.edu.np.mad.travelapp.R;

public class Bus extends Step {

    public Bus(Step step)
    {
        super(step);
    }

    @Override
    public void bind(RecyclerView.ViewHolder viewHolder, Context context) {
        if (viewHolder instanceof MinifiedStepAdapter.ViewHolder){
            MinifiedStepAdapter.ViewHolder holder = (MinifiedStepAdapter.ViewHolder) viewHolder;

            holder.legIcon.setImageResource(R.drawable.bus);
            holder.legDesc.setText(getDetails().getLine().getName());
        }
        else if (viewHolder instanceof ExpandedStepAdapter.RideViewHolder){
            ExpandedStepAdapter.RideViewHolder holder = (ExpandedStepAdapter.RideViewHolder) viewHolder;

            String duration = String.format("Ride %s stops [%s]", getDetails().getNumStops(), getDuration());

            holder.rideCode.setText(getDetails().getLine().getName());

            holder.rideDuration.setText(duration);
            holder.rideDestination.setText(getDetails().getTo());
            holder.rideOrigin.setText(getDetails().getFrom());
        }
    }
}
