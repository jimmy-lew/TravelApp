package sg.edu.np.mad.travelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Group;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


public class BusTimingCardActivity extends AppCompatActivity {
    CardView cardView;
    Group hiddenGroup;
    ImageView favouriteImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_timing_card);
        cardView = findViewById(R.id.busTimingCardView);
        hiddenGroup = findViewById(R.id.card_group);
        favouriteImageView = findViewById(R.id.favouriteImageView);
        DAOUser dao = new DAOUser();

        favouriteImageView.setOnClickListener(view -> {
            User user = new User("1");
            dao.add(user).addOnSuccessListener(suc -> {
                Toast.makeText(this, "Record is inserted", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(er -> {
                Toast.makeText(this, ""+er.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });

        cardView.setOnClickListener(view -> {
            if (hiddenGroup.getVisibility() == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                hiddenGroup.setVisibility(View.GONE);
                favouriteImageView.setVisibility(View.VISIBLE);
            } else {
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                hiddenGroup.setVisibility(View.VISIBLE);
                favouriteImageView.setVisibility(View.GONE);
            }
        });

    }


}
