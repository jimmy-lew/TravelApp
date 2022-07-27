package sg.edu.np.mad.travelapp.data.model;

import java.util.List;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Leg implements Parcelable
{

    @SerializedName("dptTime")
    @Expose
    private String dptTime;
    @SerializedName("arrTime")
    @Expose
    private String arrTime;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("steps")
    @Expose
    private List<Step> steps = null;
    public final static Creator<Leg> CREATOR = new Creator<Leg>() {

        @SuppressWarnings({
            "unchecked"
        })
        public Leg createFromParcel(android.os.Parcel in) {
            return new Leg(in);
        }

        public Leg[] newArray(int size) {
            return (new Leg[size]);
        }

    };

    protected Leg(android.os.Parcel in) {
        this.dptTime = ((String) in.readValue((String.class.getClassLoader())));
        this.arrTime = ((String) in.readValue((String.class.getClassLoader())));
        this.distance = ((String) in.readValue((String.class.getClassLoader())));
        this.duration = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.steps, (Step.class.getClassLoader()));
    }

    public Leg() {
    }

    public String getDptTime() {
        return dptTime;
    }

    public void setDptTime(String dptTime) {
        this.dptTime = dptTime;
    }

    public String getArrTime() {
        return arrTime;
    }

    public void setArrTime(String arrTime) {
        this.arrTime = arrTime;
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

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(dptTime);
        dest.writeValue(arrTime);
        dest.writeValue(distance);
        dest.writeValue(duration);
        dest.writeList(steps);
    }

    public int describeContents() {
        return  0;
    }

}
