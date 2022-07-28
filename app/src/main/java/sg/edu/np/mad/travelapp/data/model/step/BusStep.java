package sg.edu.np.mad.travelapp.data.model.step;

import android.content.Context;

import sg.edu.np.mad.travelapp.MinifiedStepAdapter;
import sg.edu.np.mad.travelapp.R;

public class BusStep extends Step {

    public BusStep(Step step)
    {
        super(step);
    }

    @Override
    public void bind(MinifiedStepAdapter.ViewHolder viewHolder, Context context) {
        viewHolder.legIcon.setImageResource(R.drawable.bus);
        viewHolder.legDesc.setText(this.getDetails().getLine().getName());
    }
}
