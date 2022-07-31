package sg.edu.np.mad.travelapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import sg.edu.np.mad.travelapp.data.model.Reminder;
import sg.edu.np.mad.travelapp.data.model.User;

public class NewReminder extends AppCompatActivity {
    EditText EditTitle, EditDescription, EditDate;
    TextView Confirm;
    ImageView Back;
    Reminder reminder;
    ArrayList<Reminder> reminderList;
    User user;
    FirebaseAuth mAuth;
    FirebaseUser fUser;
    private final DatabaseReference REF = FirebaseDatabase.getInstance().getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reminder);
        EditTitle = findViewById(R.id.editTextTitle);
        EditDescription = findViewById(R.id.editTextDescription);
        EditDate = findViewById(R.id.editTextDate);
        Confirm = findViewById(R.id.confirmCreation);
        Back = findViewById(R.id.backToProfile);
        mAuth = FirebaseAuth.getInstance();
        fUser = mAuth.getCurrentUser();
        User user = getIntent().getParcelableExtra("USER");

        Confirm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                try {
                    Date date = new SimpleDateFormat("dd/MM/yyyy").parse(EditDate.getText().toString());
                    if (date.after(new Date())) {
                        //valid
                        reminder = new Reminder(date, EditTitle.getText().toString(), EditDescription.getText().toString());
                        reminderList = user.getReminderList();
                        reminderList.add(reminder);
                        user.setReminderList(reminderList);
                        addNotification(date);
                        Intent backToProfile = new Intent(NewReminder.this, Profile.class);
                        Bundle extras = new Bundle();
                        extras.putParcelable("USER", user);
                        backToProfile.putExtras(extras);
                        startActivity(backToProfile);
                    }
                    else {
                        //invalid
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                } finally {

                }
                return false;
            }
        });

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToProfile = new Intent(NewReminder.this, Profile.class);
                Bundle extras = new Bundle();
                extras.putParcelable("USER", user);
                backToProfile.putExtras(extras);
                startActivity(backToProfile);
            }
        });
    }

    private void addNotification(Date date) {
        createNotificationChannel();
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, "ID")
                        .setSmallIcon(R.drawable.logo) //set icon for notification
                        .setContentTitle(reminder.getTitle()) //set title of notification
                        .setContentText(new SimpleDateFormat("dd/MM/yyyy").format(reminder.getRemindDate()))//this is notification message
                        .setAutoCancel(false) // makes auto cancel of notification
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT); //set priority of notification

        Intent notificationIntent = new Intent(this, ViewReminderNotification.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //notification message will get at NotificationView
        notificationIntent.putExtra("REMINDER", reminder);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();

        // Add as notification
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(0, notification);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "REMINDERS";
            String description = "for reminders";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("ID", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}