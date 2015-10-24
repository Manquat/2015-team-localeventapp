package ch.epfl.sweng.evento;

import com.google.android.gms.maps.model.LatLng;

import java.util.Set;

/**
 * This Singleton class allows to get filtered EventSets from other EventSets
 * Created by Val on 24.10.2015.
 */
public class EventFilter {
    private static EventFilter ourInstance = new EventFilter();

    public static EventFilter getInstance() {
        return ourInstance;
    }

    private EventFilter() {
    }

    public static EventSet filter(EventSet eventSet, LatLng latLng, double distance)
    {
        return eventSet.filter(latLng,distance);
    }

    public static EventSet filter(EventSet eventSet, Set<String> tags)
    {
        return eventSet.filter(tags);
    }
}
