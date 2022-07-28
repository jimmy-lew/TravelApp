package sg.edu.np.mad.travelapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;

import sg.edu.np.mad.travelapp.data.model.Route;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder> {

    private final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private ArrayList<Route> routeList = new ArrayList<Route>();

    public void setRouteList(ArrayList<Route> routeList) {
        this.routeList = routeList;
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

        public RecyclerView stepRecycler;
        public TextView duration;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            stepRecycler = itemView.findViewById(R.id.stepRecycler);
            duration = itemView.findViewById(R.id.durationTextView);
        }

        private void onBind(int position)
        {
            Route route = routeList.get(position);
            MinifiedStepAdapter mAdapter = new MinifiedStepAdapter(route.getStepList(), stepRecycler.getContext());

            duration.setText(route.getDuration());

            FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(
                    stepRecycler.getContext(),
                    FlexDirection.ROW,
                    FlexWrap.WRAP
            );
            layoutManager.setJustifyContent(JustifyContent.FLEX_START);

            stepRecycler.setLayoutManager(layoutManager);
            stepRecycler.setAdapter(mAdapter);
            stepRecycler.setRecycledViewPool(viewPool);
        }
    }
}
