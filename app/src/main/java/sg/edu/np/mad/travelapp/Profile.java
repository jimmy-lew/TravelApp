package sg.edu.np.mad.travelapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class Profile extends AppCompatActivity implements FragmentToActivity {

    Button analyticsBtn, settingsBtn;
    ImageView backBtn;
    TextView confirmBtn;
    List<String> details;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

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
                if (details.size() == 0){
                    Toast.makeText(getApplicationContext(), "There is no updates to be made", Toast.LENGTH_SHORT).show();
                }
                //Save Settings
                else if (details.size() > 0){
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(details.get(0), details.get(1));
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.getException() != null){
                                        Log.v("REAUTH", task.getException().toString());
                                    }

                                    if (task.isSuccessful()) {
                                        Log.v("CONFIRMATION", "1");
                                        user.updatePassword(details.get(2))
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getApplicationContext(), "Successfully updated password", Toast.LENGTH_SHORT).show();
                                                            Log.v("SUCCESSFULUPDATE", "1");
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });
                }
                /*
                Intent MainActivity = new Intent(Profile.this, MainActivity.class);
                startActivity(MainActivity);

                 */
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

    @Override
    public void communicate(List<String> detail) {
        details = detail;
        Log.v("SETTINGSFRAGMENT", details.get(2));
    }
}