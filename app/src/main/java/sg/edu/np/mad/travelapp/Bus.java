package sg.edu.np.mad.travelapp;

import java.util.ArrayList;

public class Bus {
    // Documentation : https://datamall.lta.gov.sg/content/dam/datamall/datasets/LTA_DataMall_API_User_Guide.pdf#page=12&zoom=100,92,134

    public int ServiceNo;

    public String Type;         // Wheelchair accessible?
    public String BusType;
    public String Load;         // How crowded the bus is | (SEA(for Seats Available) ▪ SDA (for Standing Available)▪ LSD (for Limited Standing)

    public double Latitude;
    public double Longitude;

    public ArrayList<Integer> Timings;

    // ---- Getters & Setters ----
    public int getServiceNo() {
        return ServiceNo;
    }

    public void setServiceNo(int serviceNo) {
        ServiceNo = serviceNo;
    }

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

    public ArrayList<Integer> getTimings() {
        return Timings;
    }

    public void setTimings(ArrayList<Integer> timings) {
        Timings = timings;
    }

    // --- Constructors ---
    public Bus(int serviceNo, String type, String busType, String load, double latitude, double longitude, ArrayList<Integer> timings) {
        ServiceNo = serviceNo;
        Type = type;
        BusType = busType;
        Load = load;
        Latitude = latitude;
        Longitude = longitude;
        Timings = timings;
    }

}
