package ch.epfl.sweng.evento.Events;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


/**
 * Created by Val on 24.10.2015.
 */
public class EventSet {

    private static final String TAG = "EventSet";

    //The container of Events. For now, it's a Map with the ID as key values
    private TreeMap<Long, Event> mEvents;

    /**
     * Default constructor
     */
    public EventSet() {
        //we use a TreeMap so it's sorted and we can use navigableKeySet in the methods
        // GetNext and GetPrevious
        mEvents = new TreeMap<>();
    }


    public void clear(){
        mEvents.clear();
    }

    /**
     * @param signature the Signature of the Event to be returned
     * @return the Event corresponding to the Signature or the special ERROR Event if it's not in the Map
     */
    public Event get(long signature) {
        if (mEvents.containsKey(signature)) {
            return mEvents.get(signature);
        } else {
            return getErrorEvent();
        }
    }

    /**
     * This method returns the first Event in the TreeMap. Since the Events are sorted by their
     * StartDate and then by their ID, the first Event is the closest in time from when the call
     * is made
     * @return The first Event
     */
    public Event getFirst() {
        if (mEvents.size() > 0) {
            Iterator<Long> iterator = mEvents.keySet().iterator();
            return mEvents.get(iterator.next());
        } else {
            return getErrorEvent();
        }
    }

    public Event getNext(Event current)
    {
		if(mEvents.size() > 1){
			return getNext(current.getSignature());
		}else{
			return current;
		}

    }

    /**
     * Returns the Event with the closest higher signature from the one passed in argument
     * @param signature the reference signature to define which Event is the next one
     * @return the Event with the closest signature
     */
    public Event getNext(long signature) {
        Iterator<Long> iterator = mEvents.navigableKeySet().iterator();
        long currentID = -1;//To be sure it's not in the Map

        do {
            currentID = iterator.next();
        } while (iterator.hasNext() && currentID != signature);

        //If we reached the end of the iterator, it means that 'ID' was the ID of the last Event.
        //In that case, we return the last Event
        if (iterator.hasNext()) {
            currentID = iterator.next();
        }
        if (mEvents.containsKey(currentID)) {
            return mEvents.get(currentID);

        } else {
            return getErrorEvent();
        }
    }

    public Event getPrevious(Event current)
    {
		if(mEvents.size() > 1){
			return getPrevious(current.getSignature());
		}else{
			return current;
		}
    }

    /**
     * Returns the Event with the closest lower signature from the one passed in argument
     * @param signature the reference signature
     * @return the Event with the closest lower signature
     */
    public Event getPrevious(long signature) {
        Iterator<Long> iterator = mEvents.navigableKeySet().iterator();
        long previousID = -2;
        long currentID = -1;//To be sure it's not in the Map

        do {
            previousID = currentID;
            currentID = iterator.next();
        } while (iterator.hasNext() && currentID != signature);

        //if previousID is less than zero, it means the loop has been done only once and that 'ID'
        //is the first Event's ID. In that case, we don't want to go further and we return the
        //first Event
        if (previousID < 1) {
            previousID = currentID;
        }
        if (mEvents.containsKey(previousID)) {
            return mEvents.get(previousID);

        } else {
            return getErrorEvent();
        }
    }

    /**
     * Adds an event to the Map
     * Verifies the Event is not already in the Map
     * @param event the Event to be added
     */
    public void addEvent(Event event) {
        if (event != null && !mEvents.containsKey(event.getSignature())) {
            //mEvents.put(event.getID(), event);
            mEvents.put(event.getSignature(), event);
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

    /**
     * Returns a set of Events that have the tag passed in argument in their own tags
     * @param tag the tag used to filter the set
     * @return a filtered set of Events
     */
    public EventSet filter(String tag) {
        EventSet newEventSet = new EventSet();
        for (Event event : mEvents.values()) {
            if (event.getTags().contains(tag)) {
                newEventSet.addEvent(event); //or mEvents.put(entry)...
            }
        }
        return newEventSet;
    }

    public EventSet filter(Event.CustomDate startDate) {
        EventSet newEventSet = new EventSet();
        for (Event event : mEvents.values()) {
            if (event.getStartDate().toLong() >= startDate.toLong()) {
                newEventSet.addEvent(event);
            }
        }
        return newEventSet;
    }

    /**
     *
     * @return the number of Events stored in the set
     */
    public int size() {
        return mEvents.size();
    }

    /**
     * Calculates the number of Events left after the one passed in argument
     * This is useful to know when to get new Events
     * @param event the reference Event
     * @return the number of Events left in the set
     */
    public int eventsLeftAfter(Event event) {
        int numberOfEvents = 0;

        if (event != null && mEvents.containsKey(event.getSignature())) {
            Iterator<Long> iterator = mEvents.navigableKeySet().iterator();
            while (iterator.hasNext() && iterator.next() != event.getSignature()) {
            }
            while (iterator.hasNext()) {
                numberOfEvents++;
                iterator.next();
            }
        } else {
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
                new Event.CustomDate(),
                new Event.CustomDate());
    }

    public int getPosition(long signature) {
        int position =0;
        Iterator<Long> iterator = mEvents.keySet().iterator();
        boolean stop = false;

        while (iterator.next() != signature) {
            ++position;

            if (!iterator.hasNext()) {
                Log.e(TAG, "No such event return the first event");
                position = 0;
                break;
            }
        }

        --position;//TODO test (I'm afraid that I can pass -1 as result)

        return position;
    }

    public ArrayList<Event> toArrayList(){


       // new ArrayList<Element>(Arrays.asList(array))


        return new ArrayList<Event> (mEvents.values());
    }
}
