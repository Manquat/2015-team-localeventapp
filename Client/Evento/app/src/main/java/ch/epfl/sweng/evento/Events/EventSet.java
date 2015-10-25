package ch.epfl.sweng.evento.Events;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.Map;
import java.util.Set;

/**
 * Created by Val on 24.10.2015.
 */
public class EventSet {

    //The container of Events. For now, it's a Map with the ID as key values
    private Map<Integer,Event> mEvents;

    /**
     * Default constructor
     */
    public EventSet(){}

    /**
     *
     * @param ID the ID of the Event to be returned
     * @return the Event corresponding to the ID. NULL if it's not in the Map
     */
    public Event get(int ID)
    {
        return mEvents.get(ID);
    }

    /**
     * Adds an event to the Map
     * @param event the Event to be added
     */
    public void addEvent(Event event)
    {
        if(!mEvents.containsKey(event.ID()))
        {
            mEvents.put(event.ID(), event);
        }
    }


    /**
     * Returns a set of Events that are at most at 'distance' from 'latLng'
     * @param latLng  The reference latitude and longitude
     * @param distance The maximum distance in meters from this LatLng
     * @return The new EventSet based on this filtered EventSet
     */
    public EventSet filter(LatLng latLng, double distance) {
        EventSet newEventSet = new EventSet();

        for(Map.Entry<Integer, Event> entry : mEvents.entrySet())
        {
            Event event = entry.getValue();
            float result[] = new float[1];
            //stores the distance (in meters) between those two points in result[0]
            Location.distanceBetween(event.Latitude(),event.Longitude(),latLng.latitude,latLng.longitude,result);
            if(result[0]<distance)
            {
                newEventSet.addEvent(event);
            }
        }
        return newEventSet;
    }

    /**
     * Returns a set of Events that have ALL the tags in the List 'tags'
     * @param tags the List of tags used as a filter
     * @return The new EventSet based on this filtered EventSet
     */
    public EventSet filter(Set<String> tags) {
        EventSet newEventSet = new EventSet();

        for(Map.Entry<Integer, Event> entry : mEvents.entrySet())
        {
            if(entry.getValue().Tags().containsAll(tags))
            {
                newEventSet.addEvent(entry.getValue()); //or mEvents.put(entry)...
            }
        }
        return newEventSet;
    }
}
