package sg.edu.np.mad.travelapp.data.model;

import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Line implements Parcelable
{
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private String type;
    public final static Creator<Line> CREATOR = new Creator<Line>() {

        @SuppressWarnings({
                "unchecked"
        })
        public Line createFromParcel(android.os.Parcel in) {
            return new Line(in);
        }

        public Line[] newArray(int size) {
            return (new Line[size]);
        }

    };

    protected Line(android.os.Parcel in) {
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Line() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(name);
        dest.writeValue(type);
    }

    public int describeContents() {
        return  0;
    }

}
