package sg.edu.np.mad.travelapp.data.model.step;

import android.content.Context;
import android.view.View;

import sg.edu.np.mad.travelapp.MinifiedStepAdapter;
import sg.edu.np.mad.travelapp.R;

public class WalkStep extends Step{

    public WalkStep(Step step)
    {
        super(step);
    }

    @Override
    public void bind(MinifiedStepAdapter.ViewHolder viewHolder, Context context) {
        viewHolder.legIcon.setImageResource(R.drawable.walker);
        viewHolder.legDesc.setVisibility(View.GONE);
    }
}