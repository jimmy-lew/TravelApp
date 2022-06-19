package sg.edu.np.mad.travelapp;

public class User {

    private String UserID;

    // Constructor;
    public User(){ }

    public User(String userID) {
        UserID = userID;
    }

    // Getter & Setter
    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

}
