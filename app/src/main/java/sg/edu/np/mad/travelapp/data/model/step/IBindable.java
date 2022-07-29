package sg.edu.np.mad.travelapp.data.model.step;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.travelapp.ExpandedStepAdapter;
import sg.edu.np.mad.travelapp.MinifiedStepAdapter;

public interface IBindable {
    void bind(RecyclerView.ViewHolder viewHolder, Context context);
}
