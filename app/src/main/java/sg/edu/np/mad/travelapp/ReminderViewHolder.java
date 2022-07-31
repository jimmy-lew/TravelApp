package sg.edu.np.mad.travelapp;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReminderViewHolder extends RecyclerView.ViewHolder {
    TextView remindDate;
    TextView title;
    TextView description;

    public ReminderViewHolder(@NonNull View itemView) {
        super(itemView);
        remindDate = itemView.findViewById(R.id.textRemindDate);
        title = itemView.findViewById(R.id.textReminderTitle);
        description = itemView.findViewById(R.id.textReminderDescription);
    }
}
