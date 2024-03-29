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

public class MinifiedStepAdapter extends RecyclerView.Adapter<MinifiedStepAdapter.ViewHolder>{

    private final ArrayList<Step> stepList;
    private final Context context;

    public MinifiedStepAdapter(ArrayList<Step> stepList, Context context) {
        this.stepList = stepList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.step_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) { holder.onBind(position); }

    @Override
    public int getItemCount() { return stepList.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView legIcon, arrow;
        public TextView legDesc;
        public GradientDrawable legDescBg;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            legIcon = itemView.findViewById(R.id.bus);
            arrow = itemView.findViewById(R.id.arrow);
            legDesc = itemView.findViewById(R.id.code);
            legDescBg = (GradientDrawable) legDesc.getBackground();
        }

        private void onBind(int position) {
            boolean isLastItem = position == stepList.size() - 1;
            arrow.setVisibility(isLastItem ? View.GONE : View.VISIBLE);

            Step currentStep = stepList.get(position);
            currentStep.bind(this, context);
        }
    }
}