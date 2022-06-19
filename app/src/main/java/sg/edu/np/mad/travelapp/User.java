package sg.edu.np.mad.travelapp;

public class User {
    public String ID;

    // Getter & Setter
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    // Constructor;
    public User(){ }

    public User(String ID) {
        this.ID = ID;
    }

}
