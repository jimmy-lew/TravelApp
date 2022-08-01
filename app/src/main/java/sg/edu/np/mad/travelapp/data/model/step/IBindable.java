package sg.edu.np.mad.travelapp.data.model.step;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.travelapp.ExpandedStepAdapter;
import sg.edu.np.mad.travelapp.MinifiedStepAdapter;

/**
 * Interface for binding DataModel to ViewHolder
 */
public interface IBindable {
    /**
     * Bind method
     * @param viewHolder Current ViewHolder
     * @param context ApplicationContext to retrieve application data i.e. color code values
     */
    void bind(RecyclerView.ViewHolder viewHolder, Context context);
}
