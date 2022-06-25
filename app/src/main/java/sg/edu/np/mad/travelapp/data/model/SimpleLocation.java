package sg.edu.np.mad.travelapp.data.model;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SimpleLocation implements Parcelable
{
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lng")
    @Expose
    private Double lng;
    public final static Creator<SimpleLocation> CREATOR = new Creator<SimpleLocation>() {
        @SuppressWarnings({
            "unchecked"
        })
        public SimpleLocation createFromParcel(android.os.Parcel in) {
            return new SimpleLocation(in);
        }
        public SimpleLocation[] newArray(int size) {
            return (new SimpleLocation[size]);
        }
    };

    protected SimpleLocation(android.os.Parcel in) {
        this.lat = ((Double) in.readValue((Double.class.getClassLoader())));
        this.lng = ((Double) in.readValue((Double.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public SimpleLocation() {
    }

    /**
     * 
     * @param lng
     * @param lat
     */
    public SimpleLocation(Double lat, Double lng) {
        super();
        this.lat = lat;
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(lat);
        dest.writeValue(lng);
    }

    public int describeContents() {
        return  0;
    }
}