package sg.edu.np.mad.travelapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;

import sg.edu.np.mad.travelapp.data.model.Reminder;

public class ViewReminderNotification extends AppCompatActivity {
    TextView notifTitle, notifDate, notifDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reminder_notification);
        notifTitle = findViewById(R.id.NotifTitle);
        notifDate = findViewById(R.id.NotifDate);
        notifDescription = findViewById(R.id.NotifDescription);

        Reminder reminder = getIntent().getParcelableExtra("REMINDER");

        notifDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(reminder.getRemindDate()));
        notifTitle.setText(reminder.getTitle());
        notifDescription.setText(reminder.getDescription());
    }
}