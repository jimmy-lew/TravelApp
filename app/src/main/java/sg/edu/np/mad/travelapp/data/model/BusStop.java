package sg.edu.np.mad.travelapp.data.model;

import java.util.ArrayList;

public class BusStop {
    public String BusStopCode;
    public String BusStopName;
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

    public String getBusStopName() {
        return BusStopName;
    }

    public void setBusStopName(String busStopName) {
        BusStopName = busStopName;
    }

    public BusStop(String busStopName){
        BusStopName = busStopName;
    }

    public BusStop(String busStopName, Double latitude, Double longitude){
        BusStopName = busStopName;
        Latitude = latitude;
        Longitude = longitude;
    }
}
