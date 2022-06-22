
package sg.edu.np.mad.travelapp.data.model;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Service implements Parcelable
{
    @SerializedName("serviceNo")
    @Expose
    private String serviceNo;
    @SerializedName("busList")
    @Expose
    private ArrayList<Bus> busList = null;
    public final static Creator<Service> CREATOR = new Creator<Service>() {
        @SuppressWarnings({
            "unchecked"
        })
        public Service createFromParcel(android.os.Parcel in) {
            return new Service(in);
        }
        public Service[] newArray(int size) {
            return (new Service[size]);
        }
    };

    protected Service(android.os.Parcel in) {
        this.serviceNo = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.busList, (Bus.class.getClassLoader()));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public Service() {
    }

    /**
     * 
     * @param serviceNo
     * @param busList
     */
    public Service(String serviceNo, ArrayList<Bus> busList) {
        super();
        this.serviceNo = serviceNo;
        this.busList = busList;
    }

    public String getServiceNo() {
        return serviceNo;
    }

    public void setServiceNo(String serviceNo) {
        this.serviceNo = serviceNo;
    }

    public ArrayList<Bus> getBusList() {
        return busList;
    }

    public void setBusList(ArrayList<Bus> busList) {
        this.busList = busList;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(serviceNo);
        dest.writeList(busList);
    }

    public int describeContents() {
        return  0;
    }

}
