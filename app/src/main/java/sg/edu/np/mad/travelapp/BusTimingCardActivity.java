package sg.edu.np.mad.travelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Group;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ImageView;


public class BusTimingCardActivity extends AppCompatActivity {
    CardView cardView;
    Group hiddenGroup;
    ImageView favouriteImgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_timing_card);
        cardView = findViewById(R.id.busTimingCardView);
        hiddenGroup = findViewById(R.id.card_group);
        favouriteImgView = findViewById(R.id.favouriteImgView);

        cardView.setOnClickListener(view -> {
            if (hiddenGroup.getVisibility() == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                hiddenGroup.setVisibility(View.GONE);
                favouriteImgView.setVisibility(View.VISIBLE);
            } else {
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                hiddenGroup.setVisibility(View.VISIBLE);
                favouriteImgView.setVisibility(View.GONE);
            }
        });

    }
}
