package sg.edu.np.mad.travelapp.data.model;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import sg.edu.np.mad.travelapp.data.model.step.BusStep;
import sg.edu.np.mad.travelapp.data.model.step.Step;
import sg.edu.np.mad.travelapp.data.model.step.TrainStep;
import sg.edu.np.mad.travelapp.data.model.step.WalkStep;

public class Route implements Parcelable
{
    @SerializedName("legs")
    @Expose
    private List<Leg> legs = null;
    @SerializedName("duration")
    @Expose
    private String duration;

    public final static Creator<Route> CREATOR = new Creator<Route>() {

        @SuppressWarnings({
                "unchecked"
        })
        public Route createFromParcel(android.os.Parcel in) {
            return new Route(in);
        }

        public Route[] newArray(int size) {
            return (new Route[size]);
        }

    };

    protected Route(android.os.Parcel in) {
        this.duration = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.legs, (Leg.class.getClassLoader()));
    }

    public Route() {
    }

    public List<Leg> getLegs() {
        return legs;
    }

    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(duration);
        dest.writeList(legs);
    }

    public int describeContents() {
        return  0;
    }

    public ArrayList<Step> getStepList()
    {
        ArrayList<Step> stepList = new ArrayList<Step>();

        for (Leg leg : getLegs()) {
            for (Step step : leg.getSteps()) {
                Step newStep;
                String type = step.getMode();
                if (type.equals("MRT") || type.equals("LRT")){
                    newStep = new TrainStep(step);
                }
                else if (type.equals("BUS"))
                {
                    newStep = new BusStep(step);
                }
                else
                {
                    newStep = new WalkStep(step);
                }
                stepList.add(newStep);
            }
        }

        return stepList;
    }

}
