package sg.edu.np.mad.travelapp.data.model.step;

import android.content.Context;
import android.os.Parcelable;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import sg.edu.np.mad.travelapp.data.model.Details;


public class Step implements Parcelable, IBindable
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
    };

    protected Step(android.os.Parcel in) {
        this.distance = ((String) in.readValue((String.class.getClassLoader())));
        this.duration = ((String) in.readValue((String.class.getClassLoader())));
        this.mode = ((String) in.readValue((String.class.getClassLoader())));
        this.details = ((Details) in.readValue((Details.class.getClassLoader())));
    }

    public Step() {
    }

    public Step(Step step){
        this.distance = step.getDistance();
        this.duration = step.getDuration();
        this.mode = step.getMode();
        this.details = step.getDetails();
    }

    public Step(String distance, String duration, String mode, Details details) {
        this.distance = distance;
        this.duration = duration;
        this.mode = mode;
        this.details = details;
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

    @Override
    public void bind(RecyclerView.ViewHolder viewHolder, Context context) { }
}
