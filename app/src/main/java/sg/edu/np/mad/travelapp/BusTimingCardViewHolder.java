package sg.edu.np.mad.travelapp;

import android.media.Image;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class BusTimingCardViewHolder extends RecyclerView.ViewHolder {

    CardView rootView;

    TextView stopNameTextView, stopIDTextView;
    ImageView lateImageView, weatherImageView, favouriteImageView1, favouriteImageView2, refreshImageView;
    Group hiddenGroup;

    RecyclerView busRecycler;

//    OnRefreshHandler onRefreshHandler;

    public BusTimingCardViewHolder(@NonNull View itemView) {
        super(itemView);

        stopNameTextView = itemView.findViewById(R.id.locationTextView);
        stopIDTextView = itemView.findViewById(R.id.addressTextView);

        lateImageView = itemView.findViewById(R.id.isLateImageView);
        weatherImageView = itemView.findViewById(R.id.isRainingImageView);
        favouriteImageView1 = itemView.findViewById(R.id.isFavouriteImageView);
        favouriteImageView2 = itemView.findViewById(R.id.favouriteImgView);
        refreshImageView = itemView.findViewById(R.id.busTimingRefresh);

        rootView = itemView.findViewById(R.id.busTimingCardView);
        hiddenGroup = itemView.findViewById(R.id.card_group);
        busRecycler = itemView.findViewById(R.id.busTimingRecyclerView);

//        refreshImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (onRefreshHandler != null) {
//                    onRefreshHandler.onRefresh();
//                }
//            }
//        });
    }

//    public BusTimingCardViewHolder setRefreshHandler(OnRefreshHandler onRefreshHandler){
//        this.onRefreshHandler = onRefreshHandler;
//        return this;
//    }
//
//    public interface OnRefreshHandler {
//        void onRefresh();
//    }
}
