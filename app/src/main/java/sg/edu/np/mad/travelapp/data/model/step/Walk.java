package sg.edu.np.mad.travelapp.data.model.step;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.travelapp.ExpandedStepAdapter;
import sg.edu.np.mad.travelapp.MinifiedStepAdapter;
import sg.edu.np.mad.travelapp.R;

public class Walk extends Step{

    public Walk(Step step)
    {
        super(step);
    }

    @Override
    public void bind(RecyclerView.ViewHolder viewHolder, Context context) {
        if (viewHolder instanceof MinifiedStepAdapter.ViewHolder){
            MinifiedStepAdapter.ViewHolder holder = (MinifiedStepAdapter.ViewHolder) viewHolder;

            holder.legIcon.setImageResource(R.drawable.walker);
            holder.legDesc.setVisibility(View.GONE);
        }
        else if (viewHolder instanceof ExpandedStepAdapter.WalkViewHolder){
            ExpandedStepAdapter.WalkViewHolder holder = (ExpandedStepAdapter.WalkViewHolder) viewHolder;

            String duration = String.format("Walk %s [%s]", getDuration(), getDistance());

            holder.destination.setText(getDetails().getTo());
            holder.duration.setText(duration);
            // TODO: Configure API to return timing
//            holder.time.setText();
        }
    }
}