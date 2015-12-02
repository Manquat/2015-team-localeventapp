package ch.epfl.sweng.evento.Events;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

/**
 * Container of multiple events
 */
public class EventSet {

    private static final String TAG = "EventSet";

    //The container of Events. For now, it's a Map with the ID as key values
    private TreeMap<Signature, Event> mEvents;
    private HashMap<Integer, Signature> mIDs;

    /**
     * Default constructor
     */
    public EventSet() {
        //we use a TreeMap so it's sorted and we can use navigableKeySet in the methods
        // GetNext and GetPrevious
        mEvents = new TreeMap<>();
        mIDs = new HashMap<>();
    }


    public void clear() {
        mEvents.clear();
        mIDs.clear();
    }

    /**
     * @param id the ID of the Event to be returned
     * @return the Event corresponding to the Signature or the special ERROR Event if it's not in the Map
     */
    public Event get(int id) {
        if (mIDs.containsKey(id)) {
            Signature signature = mIDs.get(id);
            return mEvents.get(signature);
        } else {
            return getErrorEvent();
        }
    }

    /**
     * This method returns the first Event in the TreeMap. Since the Events are sorted by their
     * StartDate and then by their ID, the first Event is the closest in time from when the call
     * is made
     *
     * @return The first Event
     */
    public Event getFirst() {
        if (mEvents.size() > 0) {
            Iterator<Signature> iterator = mEvents.keySet().iterator();
            return mEvents.get(iterator.next());
        } else {
            return getErrorEvent();
        }
    }

    public Event getNext(Event current) {
        if (mEvents.size() > 1) {
            return getNext(current.getID());
        } else {
            return current;
        }

    }

    /**
     * Returns the Event with the closest higher signature from the one passed in argument
     *
     * @param id the reference ID to define which Event is the next one
     * @return the Event with the closest signature
     */
    public Event getNext(int id) {
        Event event = mEvents.higherEntry(mIDs.get(id)).getValue();
        if (event != null) {
            return event;
        } else {
            return getErrorEvent();
        }
    }

    public Event getPrevious(Event current) {
        if (mEvents.size() > 1) {
            return getPrevious(current.getID());
        } else {
            return current;
        }
    }


    /**
     * Returns the Event with the closest lower signature from the one passed in argument
     *
     * @param ID the reference ID
     * @return the Event with the closest lower signature
     */
    public Event getPrevious(int ID) {
        Event event = mEvents.lowerEntry(mIDs.get(ID)).getValue();
        if (event != null) {
            return event;
        } else {
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
        if (event != null && !mIDs.containsKey(event.getID())) {
            //mEvents.put(event.getID(), event);
            Signature signature = new Signature(event.getID(), event.getStartDate());
            mIDs.put(event.getID(), signature);
            mEvents.put(signature, event);
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
     *
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


    /**
     * Returns a set of all the Events that start after the date passed in argument
     *
     * @param startDate Calendar at the date wanted for the filter
     * @return An EventSet containing the event corresponding to the filter
     */
    public EventSet filter(Calendar startDate) {
        EventSet newEventSet = new EventSet();
        for (Event event : mEvents.values()) {
            if (event.getStartDate().getTimeInMillis() >= startDate.getTimeInMillis()) {
                newEventSet.addEvent(event);
            }
        }
        return newEventSet;
    }


    /**
     * Returns a set of all the Events that start on the same day as the date passed in argument
     *
     * @param day Calendar at the date wanted (ignoring the time)
     * @return An EventSet containing the event corresponding to the filter
     */
    public EventSet filterOnDay(Calendar day) {
        EventSet newEventSet = new EventSet();
        for (Event event : mEvents.values()) {
            if (event.getStartDate().get(Calendar.DAY_OF_MONTH) == day.get(Calendar.DAY_OF_MONTH) &&
                    event.getStartDate().get(Calendar.MONTH) == day.get(Calendar.MONTH) &&
                    event.getStartDate().get(Calendar.YEAR) == day.get(Calendar.YEAR)) {
                newEventSet.addEvent(event);
            }
        }
        return newEventSet;
    }

    /**
     * @return the number of Events stored in the set
     */
    public int size() {
        return mEvents.size();
    }

    /**
     * Calculates the number of Events left after the one passed in argument
     * This is useful to know when to get new Events
     *
     * @param event the reference Event
     * @return the number of Events left in the set
     */
    public int eventsLeftAfter(Event event) {
        int numberOfEvents = 0;

        if (event != null && mIDs.containsKey(event.getID())) {
            Iterator<Signature> iterator = mEvents.navigableKeySet().iterator();
            while (iterator.hasNext() && mEvents.get(iterator.next()).getID() != event.getID()) {
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
     * @return an error event
     */
    public static Event getErrorEvent() {
        return new Event(0,
                "ERROR",
                "The Event doesn't exist or wasn't there",
                0.0, 0.0,
                "",
                "",
                new HashSet<String>(),
                new GregorianCalendar(),
                new GregorianCalendar());
    }

    public int getPosition(int id) {
        int position = 0;
        Iterator<Signature> iterator = mEvents.keySet().iterator();
        boolean stop = false;

        while (mEvents.get(iterator.next()).getID() != id) {
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

    public List<Event> toArrayList() {
        return new ArrayList<>(mEvents.values());
    }
}
