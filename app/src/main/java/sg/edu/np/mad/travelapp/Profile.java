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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sg.edu.np.mad.travelapp.data.model.Reminder;
import sg.edu.np.mad.travelapp.data.model.User;

public class Profile extends AppCompatActivity implements FragmentToActivity {

    Button reminderBtn, settingsBtn;
    ImageView backBtn;
    TextView confirmBtn;
    List<String> details;
    FirebaseAuth mAuth;
    FirebaseUser fUser;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        fUser = mAuth.getCurrentUser();

        backBtn = findViewById(R.id.backArrowimageView);
        confirmBtn = findViewById(R.id.confirmTextView);

        reminderBtn = findViewById(R.id.reminderBtn);
        settingsBtn = findViewById(R.id.settingsBtn);

        try {
            user = getIntent().getParcelableExtra("USER");
        }
        catch (Exception e) {

        }
        finally {
            if (user == null){
                user = new User(new ArrayList<>());
                ArrayList<Reminder> reminderList = new ArrayList<>();
                reminderList.add(new Reminder(new Date(), "A", "A"));
                reminderList.add(new Reminder(new Date(), "B", "B"));
                reminderList.add(new Reminder(new Date(), "C", "C"));
                user.setReminderList(reminderList);
            }
        }


        //Default Fragment
        replaceFragment(new ReminderFragment());
        reminderBtn.setTextColor(Color.parseColor("#45BBDA"));

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
                            .getCredential(fUser.getEmail(), details.get(1));
                    fUser.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.getException() != null){
                                        Log.v("REAUTH", task.getException().toString());
                                    }
                                    if (task.isSuccessful()) {
                                        Log.v("CONFIRMATION", "1");
                                        //update email code but cannot integrate with update password at the same time
                                        /*
                                        if (user.getEmail() != details.get(0)){
                                            user.updateEmail(details.get(0));
                                        }
                                        Log.v("CURRENTEMAIL",user.getEmail().toString());
                                        AuthCredential credential = EmailAuthProvider
                                                .getCredential(user.getEmail(), details.get(1));
                                        Log.v("CREDENTIAL",credential.toString());
                                        mAuth.signOut();
                                        mAuth = FirebaseAuth.getInstance();
                                         */
                                        mAuth.signInWithEmailAndPassword(details.get(0), details.get(1))
                                                .addOnCompleteListener(Profile.this, new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {
                                                            // Sign in success, update UI with the signed-in user's information
                                                            Log.d("RESIGN", "signInWithEmail:success");
                                                            fUser = mAuth.getCurrentUser();
                                                            fUser.updatePassword(details.get(2))
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.getException() != null) {
                                                                                Log.v("UPDATE", task.getException().toString());
                                                                            }
                                                                            if (task.isSuccessful()) {
                                                                                Toast.makeText(getApplicationContext(), "Successfully updated password", Toast.LENGTH_SHORT).show();
                                                                                Log.v("SUCCESSFULUPDATE", "1");
                                                                            }
                                                                        }
                                                                    });
                                                        } else {
                                                            // If sign in fails, display a message to the user.
                                                            Log.w("RESIGN", "signInWithEmail:failure", task.getException());
                                                        }
                                                    }
                                                });
                                                        /*
                                                        user.reauthenticate(credential)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.getException() != null) {
                                                                            Log.v("REAUTH", task.getException().toString());
                                                                        }


                                                                    }
                                                                });



                                                         */
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
        reminderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new ReminderFragment());
                settingsBtn.setTextColor(Color.parseColor("#8E8E8E"));
                reminderBtn.setTextColor(Color.parseColor("#45BBDA"));
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new SettingsFragment());
                settingsBtn.setTextColor(Color.parseColor("#45BBDA"));
                reminderBtn.setTextColor(Color.parseColor("#8E8E8E"));
            }
        });
    }

    private void replaceFragment(Fragment frag) {
        FragmentManager fragManager = getSupportFragmentManager();
        FragmentTransaction fragTransaction = fragManager.beginTransaction();
        fragTransaction.replace(R.id.frameLayout,frag);
        fragTransaction.commit();
    }

    public User passUser() {
        return user;
    }

    @Override
    public void communicate(List<String> detail) {
        details = detail;
        Log.v("SETTINGSFRAGMENT", details.get(2));
    }
}