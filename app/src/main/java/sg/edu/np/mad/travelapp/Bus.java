package sg.edu.np.mad.travelapp;

import java.util.ArrayList;

public class Bus extends Service{
    // Documentation : https://datamall.lta.gov.sg/content/dam/datamall/datasets/LTA_DataMall_API_User_Guide.pdf#page=12&zoom=100,92,134



    public String Type;         // Wheelchair accessible?
    public String BusType;
    public String Load;         // How crowded the bus is | (SEA(for Seats Available) ▪ SDA (for Standing Available)▪ LSD (for Limited Standing)

    public double Latitude;
    public double Longitude;

    public int Timing;

    // ---- Getters & Setters ----
    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
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

    public int getTiming() {
        return Timing;
    }

    public void setTiming(int timing) {
        Timing = timing;
    }

    // --- Constructors ---
    public Bus(int serviceNo, String type, String busType, String load, double latitude, double longitude, int timing) {
        super(serviceNo);
        Type = type;
        BusType = busType;
        Load = load;
        Latitude = latitude;
        Longitude = longitude;
        Timing = timing;
    }

}
