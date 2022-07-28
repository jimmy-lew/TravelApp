package sg.edu.np.mad.travelapp.data.model.step;

import android.content.Context;

import androidx.core.content.ContextCompat;

import sg.edu.np.mad.travelapp.MinifiedStepAdapter;
import sg.edu.np.mad.travelapp.R;

public class TrainStep extends Step{

    private final LineEnum line;

    public TrainStep(Step step)
    {
        super(step);
        line = LineEnum.get(this.getDetails().getLine().getName());
    }

    @Override
    public void bind(MinifiedStepAdapter.ViewHolder viewHolder, Context context) {
        viewHolder.legIcon.setImageResource(R.drawable.train);
        viewHolder.legDesc.setText(line.getLineName());
        viewHolder.legDescBg.mutate();
        viewHolder.legDescBg.setColor(ContextCompat.getColor(context, line.getColorCode()));
    }
}
