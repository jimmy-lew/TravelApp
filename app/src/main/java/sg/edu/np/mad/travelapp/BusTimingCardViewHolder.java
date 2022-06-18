package sg.edu.np.mad.travelapp;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class BusTimingCardViewHolder extends RecyclerView.ViewHolder {

    CardView rootView;
    TextView stopNameTextView;
    TextView stopIDTextView;

    ImageView lateImageView;
    ImageView weatherImageView;
    ImageView favouriteImageView;
    ImageView refreshImageView;

    RecyclerView busRecycler;

//    OnRefreshHandler onRefreshHandler;

    public BusTimingCardViewHolder(@NonNull View itemView) {
        super(itemView);

        stopNameTextView = itemView.findViewById(R.id.locationTextView);
        stopIDTextView = itemView.findViewById(R.id.addressTextView);

        lateImageView = itemView.findViewById(R.id.isLateImageView);
        weatherImageView = itemView.findViewById(R.id.isRainingImageView);
        favouriteImageView = itemView.findViewById(R.id.isFavouriteImageView);
        refreshImageView = itemView.findViewById(R.id.busTimingRefresh);

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
