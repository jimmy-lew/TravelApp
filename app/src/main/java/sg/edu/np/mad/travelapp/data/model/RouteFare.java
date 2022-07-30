package sg.edu.np.mad.travelapp.data.model;

import android.util.Log;

import sg.edu.np.mad.travelapp.data.model.step.Step;

public class RouteFare {
    private static final String TAG = "RouteFare";
    public Route route;
    public String fare;
    //----------
    public int noSteps;
    public String totalDuration;
    public double walkingDistance;

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare =  fare;
    }

    public String getNoSteps() {
        noSteps = route.getLegs().get(0).getSteps().size();
        return String.valueOf(noSteps);
    }

    public String getTotalDuration() {
        totalDuration = route.getLegs().get(0).getDuration();
        return totalDuration;
    }

    public Double getRawTotalDuration() {
        totalDuration = route.getLegs().get(0).getDuration();
        if (totalDuration.contains("h")) {
            String[] duration = totalDuration.split(" ");
            return (Double.valueOf(duration[0]) * 60) + Double.valueOf(duration[2]);
        }
        return 0.0;
    }

    public String getWalkingDistance() {
        double total = 0;
        String walkingDist = "undefined";

        for (Leg leg : route.getLegs()) {
            for (Step step : leg.getSteps()) {
                if (step.getMode().equals("WALKING")) {
                    total += Double.parseDouble(step.getDistance().split(" ")[0]);
                }
            }
        }
        if (total != 0) {
            walkingDist = String.valueOf(total);
        }
        return walkingDist;
    }
}
