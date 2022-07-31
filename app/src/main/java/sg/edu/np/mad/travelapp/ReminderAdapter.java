package sg.edu.np.mad.travelapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import sg.edu.np.mad.travelapp.data.model.Reminder;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderViewHolder>{

    private ArrayList<Reminder> ReminderList;
    private Context context;

    public ReminderAdapter (Context context, ArrayList<Reminder> reminderList) {
        ReminderList =  reminderList;
        this.context = context;
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(
                R.layout.recycler_reminder,
                parent,
                false
        );

        return new ReminderViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        holder.remindDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(ReminderList.get(position).getRemindDate()));
        holder.title.setText(ReminderList.get(position).getTitle());
        holder.description.setText(ReminderList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return ReminderList.size();
    }
}
