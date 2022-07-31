package sg.edu.np.mad.travelapp.data.model;

import android.os.Parcelable;

import java.util.ArrayList;

/**
 * User model class
 */
// TODO: Implement parcelable
public class User implements Parcelable {

    private String UserID;
    private ArrayList<String> FavouritesList;
    private ArrayList<Reminder> ReminderList = new ArrayList<Reminder>();

    // Constructor
    public User(){ }

    public User(String userID, ArrayList<String> favouritesList) {
        UserID = userID;
        FavouritesList = favouritesList;
    }

    public User(ArrayList<Reminder> reminderList) {
        ReminderList = reminderList;
    }

    /* Parcelable constructor */
    public final static Creator<User> CREATOR = new Creator<User>() {
        @SuppressWarnings({
                "unchecked"
        })
        public User createFromParcel(android.os.Parcel in) {
            return new User(in);
        }
        public User[] newArray(int size) {
            return (new User[size]);
        }
    };

    /* Parcelable object declarations */
    protected User(android.os.Parcel in) {
        this.UserID = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.FavouritesList, (String.class.getClassLoader()));
        in.readList(this.ReminderList, (Reminder.class.getClassLoader()));
    }

    // Getter & Setter
    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public ArrayList<String> getFavouritesList() {
        return FavouritesList;
    }

    public void setFavouritesList(ArrayList<String> favouritesList) { this.FavouritesList = favouritesList; }

    public ArrayList<Reminder> getReminderList() { return ReminderList; }

    public void setReminderList(ArrayList<Reminder> reminderList) { this.ReminderList = reminderList; }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(UserID);
        dest.writeList(FavouritesList);
        dest.writeList(ReminderList);
    }

    public int describeContents() {
        return  0;
    }
}