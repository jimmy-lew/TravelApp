package sg.edu.np.mad.travelapp;

import java.util.ArrayList;

public class Bus extends Service{
    // Documentation : https://datamall.lta.gov.sg/content/dam/datamall/datasets/LTA_DataMall_API_User_Guide.pdf#page=12&zoom=100,92,134

    public String serviceNo;    // Bus Number
    public String Feature;      // Wheelchair accessible?
    public String BusType;      // SD (for Single Deck) | DD (for Double Deck) | BD (for Bendy)
    public String Load;         // How crowded the bus is | (SEA(for Seats Available) ▪ SDA (for Standing Available)▪ LSD (for Limited Standing)

    public double Latitude;
    public double Longitude;

    public String EstimatedArrival;

    // ---- Getters & Setters ----
    @Override
    public String getServiceNo() {
        return serviceNo;
    }

    @Override
    public void setServiceNo(String serviceNo) {
        this.serviceNo = serviceNo;
    }

    public String getFeature() {
        return Feature;
    }

    public void setFeature(String feature) {
        Feature = feature;
    }

    public String getBusType() {
        return BusType;
    }

    public void setBusType(String busType) {
        BusType = busType;
    }

    public String getLoad() {
        return Load;
    }

    public void setLoad(String load) {
        Load = load;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getEstimatedArrival() {
        return EstimatedArrival;
    }

    public void setEstimatedArrival(String estimatedArrival) {
        EstimatedArrival = estimatedArrival;
    }

    // --- Constructors ---
    public Bus(String serviceNo, String feature, String busType, String load, double latitude, double longitude, String estimatedArrival) {
        super(serviceNo);
        Feature = feature;
        BusType = busType;
        Load = load;
        Latitude = latitude;
        Longitude = longitude;
        EstimatedArrival = estimatedArrival;
    }
}
