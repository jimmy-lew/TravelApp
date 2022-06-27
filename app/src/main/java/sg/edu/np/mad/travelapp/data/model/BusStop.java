package sg.edu.np.mad.travelapp.data.model;

import java.util.ArrayList;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Bus Stop model class
 */
public class BusStop implements Parcelable
{
    @SerializedName("location")
    @Expose
    private SimpleLocation simpleLocation;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("serviceList")
    @Expose
    private ArrayList<Service> serviceList = null;

    /* Parcelable constructor */
    public final static Creator<BusStop> CREATOR = new Creator<BusStop>() {
        @SuppressWarnings({
                "unchecked"
        })
        public BusStop createFromParcel(android.os.Parcel in) {
            return new BusStop(in);
        }
        public BusStop[] newArray(int size) {
            return (new BusStop[size]);
        }
    };

    /* Parcelable object declarations */
    protected BusStop(android.os.Parcel in) {
        this.simpleLocation = ((SimpleLocation) in.readValue((SimpleLocation.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.code = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.serviceList, (Service.class.getClassLoader()));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public BusStop() {
    }

    /**
     *
     * @param code
     * @param name
     * @param serviceList
     * @param simpleLocation
     */
    public BusStop(SimpleLocation simpleLocation, String name, String code, ArrayList<Service> serviceList) {
        super();
        this.simpleLocation = simpleLocation;
        this.name = name;
        this.code = code;
        this.serviceList = serviceList;
    }

    public SimpleLocation getLocation() {
        return simpleLocation;
    }

    public void setLocation(SimpleLocation simpleLocation) {
        this.simpleLocation = simpleLocation;
    }

    public String getName() {
        return name;
    }

    public BusStop setName(String name) {
        this.name = name;
        return this;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<Service> getServiceList() {
        return serviceList;
    }

    public void setServiceList(ArrayList<Service> serviceList) {
        this.serviceList = serviceList;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(simpleLocation);
        dest.writeValue(name);
        dest.writeValue(code);
        dest.writeList(serviceList);
    }

    public int describeContents() {
        return  0;
    }

}
