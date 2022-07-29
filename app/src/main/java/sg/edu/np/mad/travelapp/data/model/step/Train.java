package sg.edu.np.mad.travelapp.data.model.step;

import android.content.Context;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.travelapp.ExpandedStepAdapter;
import sg.edu.np.mad.travelapp.MinifiedStepAdapter;
import sg.edu.np.mad.travelapp.R;

public class Train extends Step{

    private final Line line;

    public Train(Step step)
    {
        super(step);
        line = Line.get(this.getDetails().getLine().getName());
    }

    @Override
    public void bind(RecyclerView.ViewHolder viewHolder, Context context) {
        if (viewHolder instanceof MinifiedStepAdapter.ViewHolder){
            MinifiedStepAdapter.ViewHolder holder = (MinifiedStepAdapter.ViewHolder) viewHolder;

            holder.legIcon.setImageResource(R.drawable.train);
            holder.legDesc.setText(line.getDisplayName());
            holder.legDescBg.mutate();
            holder.legDescBg.setColor(ContextCompat.getColor(context, line.getColorCode()));
        }
        else if (viewHolder instanceof ExpandedStepAdapter.RideViewHolder){
            ExpandedStepAdapter.RideViewHolder holder = (ExpandedStepAdapter.RideViewHolder) viewHolder;

            String duration = String.format("Ride %s stops [%s]", getDetails().getNumStops(), getDuration());

            holder.rideIcon.setImageResource(R.drawable.train);

            holder.rideCode.setText(line.getDisplayName());
            holder.rideCodeBg.mutate();
            holder.rideCodeBg.setColor(ContextCompat.getColor(context, line.getColorCode()));

            holder.rideDuration.setText(duration);
            holder.rideTime.setText(getDetails().getArrTime());
            holder.rideDestination.setText(getDetails().getTo());
            holder.rideOrigin.setText(getDetails().getFrom());
        }
    }
}
