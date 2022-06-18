package sg.edu.np.mad.travelapp;

import java.util.ArrayList;

public class Service {

    public int ServiceNo;
    public ArrayList<Bus> busList;

    public ArrayList<Bus> getBusList() {
        return busList;
    }

    public void setBusList(ArrayList<Bus> busList) {
        this.busList = busList;
    }

    public int getServiceNo() {
        return ServiceNo;
    }

    public void setServiceNo(int serviceNo) {
        ServiceNo = serviceNo;
    }

    public Service(int serviceNo){
        ServiceNo = serviceNo;
    }

}
