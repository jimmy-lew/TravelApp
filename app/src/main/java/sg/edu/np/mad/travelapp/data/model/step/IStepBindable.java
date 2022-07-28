package sg.edu.np.mad.travelapp.data.model.step;

import android.content.Context;

import sg.edu.np.mad.travelapp.MinifiedStepAdapter;

public interface IStepBindable {
    void bind(MinifiedStepAdapter.ViewHolder viewHolder, Context context);
}
