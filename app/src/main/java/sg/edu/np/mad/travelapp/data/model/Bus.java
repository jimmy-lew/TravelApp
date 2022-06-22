
package sg.edu.np.mad.travelapp.data.model;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bus implements Parcelable
{

    @SerializedName("estimatedTime")
    @Expose
    private String estimatedTime;
    @SerializedName("load")
    @Expose
    private String load;
    @SerializedName("feature")
    @Expose
    private String feature;
    @SerializedName("type")
    @Expose
    private String type;
    public final static Creator<Bus> CREATOR = new Creator<Bus>() {

        @SuppressWarnings({
            "unchecked"
        })
        public Bus createFromParcel(android.os.Parcel in) {
            return new Bus(in);
        }

        public Bus[] newArray(int size) {
            return (new Bus[size]);
        }

    };

    protected Bus(android.os.Parcel in) {
        this.estimatedTime = ((String) in.readValue((String.class.getClassLoader())));
        this.load = ((String) in.readValue((String.class.getClassLoader())));
        this.feature = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public Bus() {
    }

    /**
     * 
     * @param estimatedTime
     * @param load
     * @param feature
     * @param type
     */
    public Bus(String estimatedTime, String load, String feature, String type) {
        super();
        this.estimatedTime = estimatedTime;
        this.load = load;
        this.feature = feature;
        this.type = type;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getLoad() {
        return load;
    }

    public void setLoad(String load) {
        this.load = load;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(estimatedTime);
        dest.writeValue(load);
        dest.writeValue(feature);
        dest.writeValue(type);
    }

    public int describeContents() {
        return  0;
    }

}
