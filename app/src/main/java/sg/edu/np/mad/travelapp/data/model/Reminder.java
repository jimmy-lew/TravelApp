package sg.edu.np.mad.travelapp.data.model;

import android.os.Parcelable;

import java.util.Date;

/**
 * Analytics model class to store User's analytics
 */
public class Reminder implements Parcelable {

    private Date RemindDate;
    private String Title;
    private String Description;

    /* Parcelable constructor */
    public final static Creator<Reminder> CREATOR = new Creator<Reminder>() {
        @SuppressWarnings({
                "unchecked"
        })
        public Reminder createFromParcel(android.os.Parcel in) {
            return new Reminder(in);
        }
        public Reminder[] newArray(int size) {
            return (new Reminder[size]);
        }
    };

    /* Parcelable object declarations */
    protected Reminder(android.os.Parcel in) {
        this.RemindDate = ((Date) in.readValue((Date.class.getClassLoader())));
        this.Title = ((String) in.readValue((String.class.getClassLoader())));
        this.Description = ((String) in.readValue((String.class.getClassLoader())));
    }

    // Constructor
    public Reminder() { }

    public Reminder(Date remindDate, String title, String description) {
        RemindDate = remindDate;
        Title = title;
        Description = description;
    }

    //Getter and Setter
    public Date getRemindDate() { return RemindDate; }
    public void setRemindDate(Date remindDate) { RemindDate = remindDate; }

    public String getTitle() { return Title; }
    public void setTitle(String title) { Title = title; }

    public String getDescription() { return Description; }
    public void setDescription(String description) { Description = description; }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(RemindDate);
        dest.writeValue(Title);
        dest.writeValue(Description);
    }

    public int describeContents() {
        return  0;
    }
}
