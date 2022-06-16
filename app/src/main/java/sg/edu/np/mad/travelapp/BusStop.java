package sg.edu.np.mad.travelapp;

import java.util.ArrayList;

public class BusStop {
    public int BusStopCode;
    public String RoadName;
    public String Description;
    public Double Longitude;
    public Double Latitude;

    // ---- Getters & Setters ----
    public int getBusStopCode() {
        return BusStopCode;
    }

    public void setBusStopCode(int busStopCode) {
        BusStopCode = busStopCode;
    }

    public String getRoadName() {
        return RoadName;
    }

    public void setRoadName(String roadName) {
        RoadName = roadName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public BusStop(int busStopCode, String roadName, String description, Double longitude, Double latitude) {
        BusStopCode = busStopCode;
        RoadName = roadName;
        Description = description;
        Longitude = longitude;
        Latitude = latitude;
    }

    // --- Function to get nearby bus stops (Based on current GPS Position & Radius Range) ---
    public ArrayList<BusStop> GetBusStops(double Longitude, double Latitude, ArrayList<BusStop> busStops, double Range) {
        ArrayList<BusStop> nearStops = new ArrayList<BusStop>();
        for (BusStop b: busStops) {
            if(b.getLongitude() > Longitude - Range
                    &&
                    b.getLongitude() < Longitude + Range
                    &&
                    b.getLatitude() > Latitude - Range
                    &&
                    b.getLatitude() < Latitude + Range
            ) {
                nearStops.add(b);
            }
        }
        return nearStops;
    }
}
