package sg.edu.np.mad.travelapp.data.model;

import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Step implements Parcelable
{

    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("mode")
    @Expose
    private String mode;
    @SerializedName("details")
    @Expose
    private Details details;
    public final static Creator<Step> CREATOR = new Creator<Step>() {

        @SuppressWarnings({
            "unchecked"
        })
        public Step createFromParcel(android.os.Parcel in) {
            return new Step(in);
        }

        public Step[] newArray(int size) {
            return (new Step[size]);
        }

    }
    ;

    protected Step(android.os.Parcel in) {
        this.distance = ((String) in.readValue((String.class.getClassLoader())));
        this.duration = ((String) in.readValue((String.class.getClassLoader())));
        this.mode = ((String) in.readValue((String.class.getClassLoader())));
        this.details = ((Details) in.readValue((Details.class.getClassLoader())));
    }

    public Step() {
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
        this.details = details;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(distance);
        dest.writeValue(duration);
        dest.writeValue(mode);
        dest.writeValue(details);
    }

    public int describeContents() {
        return  0;
    }

}
