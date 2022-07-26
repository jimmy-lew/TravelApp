package sg.edu.np.mad.travelapp.data.model;

import java.util.ArrayList;

/**
 * Analytics model class to store User's analytics
 */
public class Analytics {

    private int LocationsChecked;   //No. of Locations checked by User per month
    private int BusStopsChecked;    //No. of Bus Stops checked by User per month
    private int TimesUsed;          //No. of times app has been opened by User per month

    // Constructor
    public Analytics() { }

    public Analytics(int locationsChecked, int busStopsChecked, int timesUsed) {
        LocationsChecked = locationsChecked;
        BusStopsChecked = busStopsChecked;
        TimesUsed = timesUsed;
    }

    //Getter and Setter
    public int getLocationsChecked() { return LocationsChecked; }
    public void setLocationsChecked(int locationsChecked) { LocationsChecked = locationsChecked; }

    public int getBusStopsChecked() { return BusStopsChecked; }
    public void setBusStopsChecked(int busStopsChecked) { BusStopsChecked = busStopsChecked; }

    public int getTimesUsed() { return TimesUsed; }
    public void setTimesUsed(int timesUsed) { TimesUsed = timesUsed; }
}
