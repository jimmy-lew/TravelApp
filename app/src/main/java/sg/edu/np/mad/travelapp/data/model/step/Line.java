package sg.edu.np.mad.travelapp.data.model.step;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import sg.edu.np.mad.travelapp.ExpandedStepAdapter;
import sg.edu.np.mad.travelapp.MinifiedStepAdapter;
import sg.edu.np.mad.travelapp.R;
import sg.edu.np.mad.travelapp.data.model.Route;

/**
 * A Line Enum describes data pertaining to the MRT / LRT {@link Route} {@link Step}
 * This includes the color code, lineName and display name of the Route Step to be
 * displayed by the {@link RecyclerView.ViewHolder}s of {@link ExpandedStepAdapter} and {@link MinifiedStepAdapter}
 */
public enum Line {
    NE       (R.color.north_east,   "North East", "NE"),
    NS       (R.color.north_south, "North South", "NS"),
    EW       (R.color.east_west,     "East West", "EW"),
    BP       (R.color.lrt,       "Bukit Panjang", "BP"),
    SK       (R.color.lrt,           "Seng Kang", "SK"),
    PG       (R.color.lrt,             "Punggol", "PG"),
    TE       (R.color.thompson,  "Thompson East", "TE"),
    DT       (R.color.downtown,       "Downtown", "DT"),
    CC       (R.color.circle,           "Circle", "CC");

    private final int colorCode;
    private final String lineName;
    private final String displayName;
    private static final Map<String, Line> MAP;

    public String getLineName() {
        return lineName;
    }

    public int getColorCode() {
        return colorCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    Line(int colorCode, String lineName, String displayName) {
        this.colorCode = colorCode;
        this.lineName = lineName;
        this.displayName = displayName;
    }

    /**
     * Creates HashMap of Line Enums and their respective lineName values whenever a static method is called
     */
    static {
        Map<String, Line> map = new ConcurrentHashMap<String, Line>();
        for (Line instance : Line.values()) {
            map.put(instance.getLineName(), instance);
        }
        MAP = Collections.unmodifiableMap(map);
    }

    /**
     * Returns a Line Enum associated with the specified line name
     * @param lineName The desired line name
     * @return A single Line Enum matching the given line name
     */
    public static Line get(String lineName) {
        return MAP.get(lineName);
    }
}
