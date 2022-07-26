package sg.edu.np.mad.travelapp.data.model;

import java.util.ArrayList;

/**
 * User model class
 */
// TODO: Implement parcelable
public class User {

    private String UserID;
    private ArrayList<String> FavouritesList;

    // Constructor
    public User(){ }

    public User(String userID, ArrayList<String> favouritesList) {
        UserID = userID;
        FavouritesList = favouritesList;
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

    public void setFavouritesList(ArrayList<String> favouritesList) {
        this.FavouritesList = favouritesList;
    }
}