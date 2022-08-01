package sg.edu.np.mad.travelapp.data.model;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import sg.edu.np.mad.travelapp.data.model.step.Bus;
import sg.edu.np.mad.travelapp.data.model.step.Step;
import sg.edu.np.mad.travelapp.data.model.step.Train;
import sg.edu.np.mad.travelapp.data.model.step.Walk;

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

    /**
     * Casts Step superclass into respective subclasses
     * @return StepList populated with respective Step subclasses
     */
    public ArrayList<Step> getStepList()
    {
        ArrayList<Step> stepList = new ArrayList<Step>();

        for (Leg leg : getLegs()) {
            for (Step step : leg.getSteps()) {
                Step newStep;
                String type = step.getMode();
                if (type.equals("MRT") || type.equals("LRT")){
                    newStep = new Train(step);
                }
                else if (type.equals("BUS"))
                {
                    newStep = new Bus(step);
                }
                else
                {
                    newStep = new Walk(step);
                }
                stepList.add(newStep);
            }
        }

        return stepList;
    }

}
