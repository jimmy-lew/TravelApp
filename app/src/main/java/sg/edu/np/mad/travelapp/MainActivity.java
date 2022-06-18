package sg.edu.np.mad.travelapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    private View decorView;
    Weather weather = new Weather();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //CardView favOutCardView = findViewById(R.id.favOutCardView);
        //CardView favInCardView = findViewById(R.id.favInCardView);
        //ImageView favIcon = findViewById(R.id.favIcon);
        CardView homeOutCardView = findViewById(R.id.homeOutCardView);
        CardView homeInCardView = findViewById(R.id.homeInCardView);
        ImageView homeIcon = findViewById(R.id.homeIcon);
        //CardView nearbyOutCardView = findViewById(R.id.nearbyOutCardView);
        //CardView nearbyInCardView = findViewById(R.id.nearbyInCardView);
        //ImageView nearbyIcon = findViewById(R.id.nearbyIcon);

        //TODO: Need add drop shadow for navbar
        homeOutCardView.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"));
        homeInCardView.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"));
        homeIcon.setImageResource(R.drawable.home_active);
        //TODO: Not sure how to remove drop shadow for inactive

        weather.checkWeatherStatus("10", "10");

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });
    }



    // ---- Hide System Default UI Elements (Status Bar & Navigation Bar) ----
    // Documentation : https://developer.android.com/reference/android/app/Activity >> OnWindowFocusChanged ----
    // Called when the activity gains or loses window focus, called true if focused.
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        {
            // If there is focus on the window, hide the status bar and navigation bar.
            if (hasFocus) {
                decorView.setSystemUiVisibility(hideSystemBars());}
        }
    }

    public int hideSystemBars(){
        // Use Bitwise Operators to combine the flags
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    }

}