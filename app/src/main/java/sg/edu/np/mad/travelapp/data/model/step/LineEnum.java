package sg.edu.np.mad.travelapp.data.model.step;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import sg.edu.np.mad.travelapp.R;

public enum LineEnum {
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
    private static final Map<String, LineEnum> MAP;

    public String getLineName() {
        return lineName;
    }

    public int getColorCode() {
        return colorCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    LineEnum(int colorCode, String lineName, String displayName) {
        this.colorCode = colorCode;
        this.lineName = lineName;
        this.displayName = displayName;
    }

    static {
        Map<String, LineEnum> map = new ConcurrentHashMap<String, LineEnum>();
        for (LineEnum instance : LineEnum.values()) {
            map.put(instance.getLineName(), instance);
        }
        MAP = Collections.unmodifiableMap(map);
    }

    public static LineEnum get(String lineName) {
        return MAP.get(lineName);
    }
}
