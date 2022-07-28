package sg.edu.np.mad.travelapp.data.model.step;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import sg.edu.np.mad.travelapp.R;

public enum LineEnum {
    NE       (R.color.north_east,   "North East"),
    NS       (R.color.north_south, "North South"),
    EW       (R.color.east_west,     "East West"),
    BP       (R.color.lrt,       "Bukit Panjang"),
    SK       (R.color.lrt,           "Seng Kang"),
    PG       (R.color.lrt,             "Punggol"),
    TE       (R.color.thompson,  "Thompson East"),
    DT       (R.color.downtown,       "Downtown"),
    CC       (R.color.circle,           "Circle");

    private final int colorCode;
    private final String lineName;
    private static final Map<String, LineEnum> MAP;

    public String getLineName() {
        return lineName;
    }

    public int getColorCode() {
        return colorCode;
    }

    LineEnum(int colorCode, String lineName) {
        this.colorCode = colorCode;
        this.lineName = lineName;
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
