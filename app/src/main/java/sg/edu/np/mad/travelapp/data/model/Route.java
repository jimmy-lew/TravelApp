package sg.edu.np.mad.travelapp.data.model;

import java.util.List;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Route implements Parcelable
{
    @SerializedName("legs")
    @Expose
    private List<Leg> legs = null;
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

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeList(legs);
    }

    public int describeContents() {
        return  0;
    }

}
