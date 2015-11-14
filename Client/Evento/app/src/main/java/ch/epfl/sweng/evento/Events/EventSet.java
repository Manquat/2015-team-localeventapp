package ch.epfl.sweng.evento.Events;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by Val on 24.10.2015.
 */
public class EventSet {

    //The container of Events. For now, it's a Map with the ID as key values
    private Map<Long, Event> mEvents;

    /**
     * Default constructor
     */
    public EventSet() {
        //we use a TreeMap so it's sorted and the methods GetNext and GetPrevious make sense
        mEvents = new TreeMap<>();
    }

    /**
     * @param ID the ID of the Event to be returned
     * @return the Event corresponding to the ID or the special ERROR Event if it's not in the Map
     */
    public Event get(long ID) {
        if (mEvents.containsKey(ID)) {
            return mEvents.get(ID);
        } else {
            return getErrorEvent();
        }
    }

    public Event getFirst() {
        Iterator<Long> iterator = mEvents.keySet().iterator();

        return mEvents.get(iterator.next());
    }

    public Event getNext(Event current)
    {
        return getNext(current.getSignature());

    }

    public Event getNext(long signature)
    {
        Iterator<Long> iterator = mEvents.keySet().iterator();
        long currentID = -1;//To be sure it's not in the Map

        do{
            currentID = iterator.next();
        }while(iterator.hasNext() && currentID != signature);

        //If we reached the end of the iterator, it means that 'ID' was the ID of the last Event.
        //In that case, we return the last Event
        if(iterator.hasNext())
        {
            currentID = iterator.next();
        }
        if(mEvents.containsKey(currentID)) {
            return mEvents.get(currentID);

        }else{
            return getErrorEvent();
        }
    }

    public Event getPrevious(Event current)
    {
        return getPrevious(current.getSignature());
    }

    public Event getPrevious(long signature)
    {
        Iterator<Long> iterator = mEvents.keySet().iterator();
        long previousID = -2;
        long currentID = -1;//To be sure it's not in the Map

        do{
            previousID = currentID;
            currentID = iterator.next();
        }while(iterator.hasNext() && currentID != signature);

        //if previousID is less than zero, it means the loop has been done only once and that 'ID'
        //is the first Event's ID. In that case, we don't want to go further and we return the
        //first Event
        if(previousID<1){
            previousID = currentID;
        }
        if(mEvents.containsKey(previousID)) {
            return mEvents.get(previousID);

        }else{
            return getErrorEvent();
        }
    }

    /**
     * Adds an event to the Map
     * Verifies the Event is not already in the Map
     *
     * @param event the Event to be added
     */
    public void addEvent(Event event) {
        if (event != null && !mEvents.containsKey(event.getSignature())) {
            //mEvents.put(event.getID(), event);
            mEvents.put(event.getSignature(),event);
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

    public EventSet filter(Event.Date startDate) {
        EventSet newEventSet = new EventSet();
        for (Event event : mEvents.values()){
            if(event.getStartDate().toLong()>=startDate.toLong()){
                newEventSet.addEvent(event);
            }
        }
        return newEventSet;
    }

    public int size() {
        return mEvents.size();
    }

    public int eventsLeftAfter(Event event)
    {
        int numberOfEvents = 0;

        if(event != null && mEvents.containsKey(event.getSignature()))
        {
            Iterator<Long> iterator = mEvents.keySet().iterator();
            while(iterator.hasNext() && iterator.next() != event.getSignature()){}
            while(iterator.hasNext())
            {
                numberOfEvents++;
                iterator.next();
            }
        }
        else
        {
            numberOfEvents = -1;
        }

        return numberOfEvents;
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
