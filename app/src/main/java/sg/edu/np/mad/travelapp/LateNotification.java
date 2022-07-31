package sg.edu.np.mad.travelapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.lang.reflect.Array;
import java.util.ArrayList;

import sg.edu.np.mad.travelapp.data.model.Bus;
import sg.edu.np.mad.travelapp.data.model.BusStop;
import sg.edu.np.mad.travelapp.data.model.Service;
import sg.edu.np.mad.travelapp.data.model.SimpleLocation;
import sg.edu.np.mad.travelapp.data.repository.BusStopRepository;

public class LateNotification {
    private static final String CHANNEL_ID = "Late";
    private static final String TAG = "Notif";
    private int NOTIFICATION_ID = 1;
    private static final String CHANNEL_NAME = "workmanager-reminder";
    private ArrayList<String> serviceNoList = new ArrayList<>();
    private ArrayList<String> busStopNameList = new ArrayList<>();

    static void sendNotification(Context context, String content) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        // Creates a NotificationChannel
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
                .setContentTitle("These buses are late!")
                .setContentText(content)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.logo);
        notificationManager.notify(1, builder.build());

    }
    public void CheckBusTimings(Context context){
        ArrayList<String> codeList = new ArrayList<String>();
        // Getting the cache of nearby bus stops from the repo as a list
        ArrayList<BusStop> cache = BusStopRepository.getInstance().getNearbyCache();
        // Acts as a way to "wait" for the API to store the nearby cache
        if (cache == null) return;

        ArrayList<Service> serviceListNew = new ArrayList<>();
        ArrayList<Bus> busListNew = new ArrayList<>();
        ArrayList<BusStop> busStopListNew = new ArrayList<>();
        Bus busNew = new Bus("10 mins", "SEA", "WAB","DD");
        busListNew.add(busNew);
        Service serviceNew = new Service("50", busListNew);
        serviceListNew.add(serviceNew);
        SimpleLocation simpleLocationNew = new SimpleLocation(1.404944, 103.909134);
        BusStop busStopNew = new BusStop(simpleLocationNew, "Damai Stn Exit A", "65309", serviceListNew);
        busStopListNew.add(busStopNew);
        //Creating the list of estimated times of the cached bus stops
        ArrayList<String> cacheTimeList = CreateTimeList(cache);
        // Adding each bus stop's codes from cache into codeList
        for (BusStop busStop : cache){
            codeList.add(busStop.getCode());
        }
        // Getting the bus stops from bus repo using the codes
        BusStopRepository.getInstance().getBusStopsByCode(codeList, busStopList -> {
            ArrayList<String> newTimeList = CreateTimeList(busStopList);
            // Comparing each bus's new estimated time to the cached estimated times
            for (String time : newTimeList){
                // Checking if the estimated time remained the same (same = late!)
                if (time.equals(cacheTimeList.get(newTimeList.indexOf(time)))){
                    // Getting the serviceNo and the name of the late bus and bus stop
                    String serviceNo = serviceNoList.get(newTimeList.indexOf(time));
                    String busStopName = busStopNameList.get(newTimeList.indexOf(time));
                    String content = String.format("Bus %s at %s may be late!", serviceNo, busStopName);
                    sendNotification(context, content);
                }
            }
        });
    }
    // Creates a list of the estimated times of each bus from a busStopList
    public ArrayList<String> CreateTimeList(ArrayList<BusStop> busStopList){
        ArrayList<String> timeList = new ArrayList<String>();
        // Adding both the serviceNo and estimated time of the buses to their respective lists
        for (BusStop busStop : busStopList){
            busStopNameList.add(busStop.getName());
            for (Service service : busStop.getServiceList()){
                serviceNoList.add(service.getServiceNo());
                ArrayList<Bus> busList = service.getBusList();
                // Index 0 to get nearest bus because the API returns the next 3 buses
                Bus firstBus = busList.get(0);
                timeList.add(firstBus.getEstimatedTime());
            }
        }
        return timeList;
    }
}
