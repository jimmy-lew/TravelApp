package sg.edu.np.mad.travelapp.data.model;

import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Details implements Parcelable
{
    @SerializedName("arrTime")
    @Expose
    private String arrTime;
    @SerializedName("from")
    @Expose
    private String from;
    @SerializedName("to")
    @Expose
    private String to;
    @SerializedName("num_stops")
    @Expose
    private Integer numStops;
    @SerializedName("line")
    @Expose
    private Line line;
    public final static Creator<Details> CREATOR = new Creator<Details>() {

        @SuppressWarnings({
            "unchecked"
        })
        public Details createFromParcel(android.os.Parcel in) {
            return new Details(in);
        }

        public Details[] newArray(int size) {
            return (new Details[size]);
        }

    };

    protected Details(android.os.Parcel in) {
        this.arrTime = ((String) in.readValue((String.class.getClassLoader())));
        this.from = ((String) in.readValue((String.class.getClassLoader())));
        this.to = ((String) in.readValue((String.class.getClassLoader())));
        this.numStops = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.line = ((Line) in.readValue((Line.class.getClassLoader())));
    }

    public Details() {
    }

    public String getArrTime() {
        return arrTime;
    }

    public void setArrTime(String arrTime) {
        this.arrTime = arrTime;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Integer getNumStops() {
        return numStops;
    }

    public void setNumStops(Integer numStops) {
        this.numStops = numStops;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(arrTime);
        dest.writeValue(from);
        dest.writeValue(to);
        dest.writeValue(numStops);
        dest.writeValue(line);
    }

    public int describeContents() {
        return  0;
    }

}
