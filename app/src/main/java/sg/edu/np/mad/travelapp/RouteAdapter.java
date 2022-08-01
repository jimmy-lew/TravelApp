package sg.edu.np.mad.travelapp;

import android.annotation.SuppressLint;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import sg.edu.np.mad.travelapp.data.model.Route;
import sg.edu.np.mad.travelapp.data.model.User;
import sg.edu.np.mad.travelapp.data.model.step.Step;

@SuppressLint("NotifyDataSetChanged")
public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder> {

    private final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private ArrayList<Route> routeList = new ArrayList<Route>();
    private User user = new User("1", new ArrayList<String>(), new ArrayList<Route>());
    private final DatabaseReference REF = FirebaseDatabase.getInstance().getReference("users");

    public void setRouteList(ArrayList<Route> routeList) {
        this.routeList = routeList;
        this.notifyDataSetChanged();
    }

    public void setUser(User user) {
        this.user = user;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.route_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (routeList == null) return 0;
        return routeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView rootView;
        public RecyclerView minifiedStepRecycler, expandedStepRecycler;
        public TextView duration;
        public ImageView favouriteButton;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.route_card);
            minifiedStepRecycler = itemView.findViewById(R.id.stepRecycler);
            expandedStepRecycler = itemView.findViewById(R.id.expandedStepRecycler);
            duration = itemView.findViewById(R.id.durationTextView);
            favouriteButton = itemView.findViewById(R.id.isFavouriteImageView);
        }

        protected void onBind(int position)
        {
            Route route = routeList.get(position);
            ArrayList<Step> stepList = route.getStepList();
            MinifiedStepAdapter mAdapter = new MinifiedStepAdapter(stepList, minifiedStepRecycler.getContext());
            ExpandedStepAdapter eAdapter = new ExpandedStepAdapter(stepList, expandedStepRecycler.getContext());
            ArrayList<Route> favRoutes = user.getFavouriteRoutes();
            final boolean[] isFavourite = {favRoutes != null && favRoutes.contains(route)};

            duration.setText(route.getDuration());

            favouriteButton.setImageResource(isFavourite[0] ? R.drawable.favorite : R.drawable.favorite_inactive);

            // Toggle visibility of expanded step list
            rootView.setOnClickListener(view -> {
                boolean isVisible = expandedStepRecycler.getVisibility() == View.VISIBLE;
                TransitionManager.beginDelayedTransition(rootView, new AutoTransition());
                expandedStepRecycler.setVisibility(isVisible ? View.GONE : View.VISIBLE);
            });

            // Toggle favourite state and update database
            favouriteButton.setOnClickListener(view -> {
                favouriteButton.setImageResource(isFavourite[0] ? R.drawable.favorite_inactive : R.drawable.favorite);
                if (isFavourite[0]) favRoutes.remove(route);
                else favRoutes.add(route);

                isFavourite[0] = !isFavourite[0];

                user.setFavouriteRoutes(favRoutes);
                REF.child(user.getUserID()).setValue(user);
            });

            FlexboxLayoutManager minifiedLayoutManager = new FlexboxLayoutManager(
                    minifiedStepRecycler.getContext(),
                    FlexDirection.ROW, // Sets direction to row
                    FlexWrap.WRAP // Enables wrapping of content
            );
            minifiedLayoutManager.setJustifyContent(JustifyContent.FLEX_START); // Sets content alignment to start

            LinearLayoutManager expandedLayoutManager = new LinearLayoutManager(
                    expandedStepRecycler.getContext(),
                    LinearLayoutManager.VERTICAL,
                    false
            );

            expandedLayoutManager.setInitialPrefetchItemCount(stepList.size());

            // Initialize expandedStepRecycler
            expandedStepRecycler.setLayoutManager(expandedLayoutManager);
            expandedStepRecycler.setAdapter(eAdapter);
            expandedStepRecycler.setRecycledViewPool(viewPool);

            // Initialize minifiedStepRecycler
            minifiedStepRecycler.setLayoutManager(minifiedLayoutManager);
            minifiedStepRecycler.setAdapter(mAdapter);
            minifiedStepRecycler.setRecycledViewPool(viewPool);
        }
    }
}