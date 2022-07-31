package sg.edu.np.mad.travelapp;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;

import sg.edu.np.mad.travelapp.data.model.Route;
import sg.edu.np.mad.travelapp.data.model.step.Step;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder> {

    private final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private ArrayList<Route> routeList = new ArrayList<Route>();


    public void setRouteList(ArrayList<Route> routeList) {
        this.routeList = routeList;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.route_card,
                parent,
                false
        );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView rootView;
        public RecyclerView minifiedStepRecycler, expandedStepRecycler;
        public TextView duration;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.route_card);
            minifiedStepRecycler = itemView.findViewById(R.id.stepRecycler);
            expandedStepRecycler = itemView.findViewById(R.id.expandedStepRecycler);
            duration = itemView.findViewById(R.id.durationTextView);
        }

        protected void onBind(int position)
        {
            Route route = routeList.get(position);
            ArrayList<Step> stepList = route.getStepList();
            MinifiedStepAdapter mAdapter = new MinifiedStepAdapter(stepList, minifiedStepRecycler.getContext());
            ExpandedStepAdapter eAdapter = new ExpandedStepAdapter(stepList, expandedStepRecycler.getContext());

            duration.setText(route.getDuration());

            rootView.setOnClickListener(view -> {
                boolean isVisible = expandedStepRecycler.getVisibility() == View.VISIBLE;
                TransitionManager.beginDelayedTransition(rootView, new AutoTransition());
                expandedStepRecycler.setVisibility(isVisible ? View.GONE : View.VISIBLE);
            });

            FlexboxLayoutManager minifiedLayoutManager = new FlexboxLayoutManager(
                    minifiedStepRecycler.getContext(),
                    FlexDirection.ROW,
                    FlexWrap.WRAP
            );
            minifiedLayoutManager.setJustifyContent(JustifyContent.FLEX_START);

            LinearLayoutManager expandedLayoutManager = new LinearLayoutManager(
                    expandedStepRecycler.getContext(),
                    LinearLayoutManager.VERTICAL,
                    false
            );

            expandedLayoutManager.setInitialPrefetchItemCount(stepList.size());

            expandedStepRecycler.setLayoutManager(expandedLayoutManager);
            expandedStepRecycler.setAdapter(eAdapter);
            expandedStepRecycler.setRecycledViewPool(viewPool);

            minifiedStepRecycler.setLayoutManager(minifiedLayoutManager);
            minifiedStepRecycler.setAdapter(mAdapter);
            minifiedStepRecycler.setRecycledViewPool(viewPool);
        }
    }
}