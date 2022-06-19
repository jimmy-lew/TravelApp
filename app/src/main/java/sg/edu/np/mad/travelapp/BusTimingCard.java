package sg.edu.np.mad.travelapp;

import java.util.ArrayList;

import sg.edu.np.mad.travelapp.data.model.Bus;

public class BusTimingCard {
    String stopName;
    String stopID;
    String stopRoad;

    ArrayList<Bus> busList;

    boolean isRaining;
    boolean isLate;
    boolean isFavourite;
    boolean isVisible;

    public BusTimingCard(String stopName, String stopID, String stopRoad, ArrayList<Bus> busList, boolean isRaining, boolean isLate, boolean isFavourite) {
        this.stopName = stopName;
        this.stopID = stopID;
        this.stopRoad = stopRoad;
        this.busList = busList;
        this.isRaining = isRaining;
        this.isLate = isLate;
        this.isFavourite = isFavourite;
        this.isVisible = true;
    }
}
