package ch.epfl.sweng.evento.Events;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Val on 24.10.2015.
 */
public class EventSet {

    //The container of Events. For now, it's a Map with the ID as key values
    private Map<Integer, Event> mEvents;

    /**
     * Default constructor
     */
    public EventSet() {
        mEvents = new HashMap<>();
    }

    /**
     * @param ID the ID of the Event to be returned
     * @return the Event corresponding to the ID or the special ERROR Event if it's not in the Map
     */
    public Event get(int ID) {
        if (mEvents.containsKey(ID)) {
            return mEvents.get(ID);
        } else {
            return getErrorEvent();
        }
    }

    public Event getFirst() {
        int i = 0;
        while (!mEvents.containsKey(i)) {
            i++;
        }
        return mEvents.get(i);

    }
    //not working yet
    /*public Event getNext(int ID)
    {
        int i = ID+1;
        Iterator<Integer> iterator = mEvents.keySet().iterator();

        while(iterator.hasNext() && iterator.next() != i) {
            i++;
        }

        if(i == mEvents.keySet().size()) {
            return getErrorEvent();
        }else{
            return mEvents.get(i);
        }
    }*/

    /**
     * Adds an event to the Map
     * Verifies the Event is not already in the Map
     *
     * @param event the Event to be added
     */
    public void addEvent(Event event) {
        if (!mEvents.containsKey(event.getID())) {
            mEvents.put(event.getID(), event);
        }
    }


    /**
     * Returns a set of Events that are at most at 'distance'
     * from the latitude and longitude stored in 'latLng'
     *
     * @param latLng   The reference latitude and longitude
     * @param distance The maximum distance in meters from this LatLng
     * @return The new EventSet based on this filtered EventSet
     */
    public EventSet filter(LatLng latLng, double distance) {
        EventSet newEventSet = new EventSet();
        for (Event event : mEvents.values()) {
            float result[] = new float[1];
            //stores the distance (in meters) between those two points in result[0]
            Location.distanceBetween(event.getLatitude(), event.getLongitude(), latLng.latitude, latLng.longitude, result);
            if (result[0] < distance) {
                newEventSet.addEvent(event);
            }
        }
        return newEventSet;
    }

    /**
     * Returns a set of Events that have ALL the tags in the List 'tags'
     *
     * @param tags the List of tags used as a filter
     * @return The new EventSet based on this filtered EventSet
     */
    public EventSet filter(Set<String> tags) {
        EventSet newEventSet = new EventSet();

        for (Event event : mEvents.values()) {
            if (event.getTags().containsAll(tags)) {
                newEventSet.addEvent(event); //or mEvents.put(entry)...
            }
        }
        return newEventSet;
    }

    public EventSet filter(String tag) {
        EventSet newEventSet = new EventSet();
        for (Event event : mEvents.values()) {
            if (event.getTags().contains(tag)) {
                newEventSet.addEvent(event); //or mEvents.put(entry)...
            }
        }
        return newEventSet;
    }

    public int size() {
        return mEvents.size();
    }

    /**
     * This method returns an error Event.
     * This is just temporary before implementing good exception handling
     *
     * @return
     */
    private Event getErrorEvent() {
        return new Event(0,
                "ERROR",
                "The Event doesn't exist or wasn't there",
                0.0, 0.0,
                "",
                "",
                new HashSet<String>(),
                new Event.Date(),
                new Event.Date());
    }
}
