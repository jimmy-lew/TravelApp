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

    private ArrayList<Step> stepList;
    private Context context;

    public ExpandedStepAdapter(ArrayList<Step> stepList, Context context) {
        this.stepList = stepList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1: {
                View view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.vehicle_step_card,
                        parent,
                        false
                );

                view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));

                return new RideViewHolder(view);
            }

            default: {
                View view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.walk_step_card,
                        parent,
                        false
                );

                view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));

                return new WalkViewHolder(view);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch(holder.getItemViewType()) {
            case 1: {
                RideViewHolder viewHolder = (RideViewHolder) holder;
                viewHolder.onBind(position);
                break;
            }

            default: {
                WalkViewHolder viewHolder = (WalkViewHolder) holder;
                viewHolder.onBind(position);
                break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0;
        Step currentStep = stepList.get(position);
        if (currentStep == null) return viewType;
        return currentStep.getMode().equals("WALKING") ? 0 : 1;
    }

    @Override
    public int getItemCount() {
        return stepList.size();
    }

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
