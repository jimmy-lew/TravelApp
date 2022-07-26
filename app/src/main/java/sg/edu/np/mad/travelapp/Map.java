package sg.edu.np.mad.travelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

public class Map extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Initialize fragment
        Fragment fragment=new MapFragment();

        // Open fragment
        getSupportFragmentManager()
                .beginTransaction().replace(R.id.frame_layout,fragment)
                .commit();
    }
}