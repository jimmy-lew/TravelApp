package sg.edu.np.mad.travelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Profile extends AppCompatActivity {

    Button analyticsBtn, settingsBtn;
    ImageView backBtn;
    TextView confirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        backBtn = findViewById(R.id.backArrowimageView);
        confirmBtn = findViewById(R.id.confirmTextView);

        analyticsBtn = findViewById(R.id.analyticsBtn);
        settingsBtn = findViewById(R.id.settingsBtn);

        //Default Fragment
        replaceFragment(new AnalyticsFragment());
        analyticsBtn.setTextColor(Color.parseColor("#45BBDA"));

        //Intents for Buttons
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent MainActivity = new Intent(Profile.this, MainActivity.class);
                startActivity(MainActivity);
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Save Settings

                Intent MainActivity = new Intent(Profile.this, MainActivity.class);
                startActivity(MainActivity);
            }
        });

        //Fragments
        analyticsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new AnalyticsFragment());
                settingsBtn.setTextColor(Color.parseColor("#8E8E8E"));
                analyticsBtn.setTextColor(Color.parseColor("#45BBDA"));
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new SettingsFragment());
                settingsBtn.setTextColor(Color.parseColor("#45BBDA"));
                analyticsBtn.setTextColor(Color.parseColor("#8E8E8E"));
            }
        });
    }

    private void replaceFragment(Fragment frag) {
        FragmentManager fragManager = getSupportFragmentManager();
        FragmentTransaction fragTransaction = fragManager.beginTransaction();
        fragTransaction.replace(R.id.frameLayout,frag);
        fragTransaction.commit();
    }
}