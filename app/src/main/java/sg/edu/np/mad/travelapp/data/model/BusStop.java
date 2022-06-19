package sg.edu.np.mad.travelapp.data.model;

import java.util.ArrayList;

public class BusStop {
    public String BusStopCode;
    public String RoadName;
    public String Description;
    public Double Longitude;
    public Double Latitude;

    public boolean hasLateBus;
    public boolean isRaining;

    public ArrayList<Service> ServiceList;

    // ---- Getters & Setters ----
    public String getBusStopCode() {
        return BusStopCode;
    }

    public void setBusStopCode(String busStopCode) {
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

    public boolean isHasLateBus() {
        return hasLateBus;
    }

    public void setHasLateBus(boolean hasLateBus) {
        this.hasLateBus = hasLateBus;
    }

    public boolean isRaining() {
        return isRaining;
    }

    public void setRaining(boolean raining) {
        isRaining = raining;
    }

    public ArrayList<Service> getServiceList() { return ServiceList; }

    public void setServiceList(ArrayList<Service> serviceList) { ServiceList = serviceList; }

    public BusStop(String busStopCode, String roadName, String description, Double longitude, Double latitude, ArrayList<Service> serviceList) {
        BusStopCode = busStopCode;
        RoadName = roadName;
        Description = description;
        Longitude = longitude;
        Latitude = latitude;

        ServiceList = serviceList;
    }


    // --- Function to get nearby bus stops (Based on current GPS Position & Radius Range) ---
    public ArrayList<BusStop> GetBusStops(double Longitude, double Latitude, ArrayList<BusStop> busStops, double Range) {
        ArrayList<BusStop> nearStops = new ArrayList<>();
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
