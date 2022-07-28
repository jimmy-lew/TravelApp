package sg.edu.np.mad.travelapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import static android.content.Context.NOTIFICATION_SERVICE;

import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class LateNotification {
    private static final String CHANNEL_ID = "Late";
    private int NOTIFICATION_ID = 1;
    private static final String CHANNEL_NAME = "workmanager-reminder";

    static void sendNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.enableLights(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Late")
                .setContentText("Bus is late")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.late);

        notificationManager.notify(1, builder.build());
    }
}
