package sg.edu.np.mad.travelapp;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.mad.travelapp.data.model.step.Step;

public class ExpandedStepAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<Step> stepList;
    private final Context context;
    private final int WALK_STEP = 0;
    private final int RIDE_STEP = 1;

    public ExpandedStepAdapter(ArrayList<Step> stepList, Context context) {
        this.stepList = stepList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == RIDE_STEP) {
            View view = inflater.inflate(R.layout.vehicle_step_card, parent, false);
            return new RideViewHolder(view);
        }

        View view = inflater.inflate(R.layout.walk_step_card, parent, false);
        return new WalkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == RIDE_STEP)
        {
            ((RideViewHolder) holder).onBind(position);
        }
        else if (holder.getItemViewType() == WALK_STEP)
        {
            ((WalkViewHolder) holder).onBind(position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Step currentStep = stepList.get(position);
        if (currentStep == null) return WALK_STEP;
        return currentStep.getMode().equals("WALKING") ? WALK_STEP : RIDE_STEP;
    }

    @Override
    public int getItemCount() { return stepList.size(); }

    public class WalkViewHolder extends RecyclerView.ViewHolder{

        public TextView destination, duration, time;

        public WalkViewHolder(@NonNull View itemView) {
            super(itemView);

            destination = itemView.findViewById(R.id.walkOriginTextView);
            duration = itemView.findViewById(R.id.walkDurationTextView);
            time = itemView.findViewById(R.id.walkTimeTextView);
        }

        private void onBind(int position) {
            Step currentStep = stepList.get(position);
            currentStep.bind(this, context);
        }
    }

    public class RideViewHolder extends RecyclerView.ViewHolder{

        public TextView rideCode, rideDuration, rideTime, rideOrigin, rideDestination;
        public GradientDrawable rideCodeBg;
        public ImageView rideIcon;

        public RideViewHolder(@NonNull View itemView) {
            super(itemView);

            rideCode = itemView.findViewById(R.id.rideCode);
            rideDuration = itemView.findViewById(R.id.rideDuration);
            rideTime = itemView.findViewById(R.id.rideTime);
            rideDestination = itemView.findViewById(R.id.rideDestination);
            rideOrigin = itemView.findViewById(R.id.rideOrigin);

            rideIcon = itemView.findViewById(R.id.rideIcon);
            rideCodeBg = (GradientDrawable) rideCode.getBackground();
        }

        private void onBind(int position) {
            Step currentStep = stepList.get(position);
            currentStep.bind(this, context);
        }
    }
}