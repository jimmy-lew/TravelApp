package sg.edu.np.mad.travelapp;

import java.util.ArrayList;

public class Service {

    public String ServiceNo;
    public ArrayList<Bus> busList;

    public Service(String serviceNo) {
        ServiceNo = serviceNo;
    }

    public ArrayList<Bus> getBusList() {
        return busList;
    }

    public void setBusList(ArrayList<Bus> busList) {
        this.busList = busList;
    }

    public String getServiceNo() {
        return ServiceNo;
    }

    public void setServiceNo(String serviceNo) {
        ServiceNo = serviceNo;
    }

    public Service(String serviceNo, ArrayList<Bus> busList){
        ServiceNo = serviceNo;
        this.busList = busList;
    }

}
