package ch.epfl.sweng.evento;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;


import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.epfl.sweng.evento.Events.Event;
import ch.epfl.sweng.evento.Events.EventSet;
import ch.epfl.sweng.evento.RestApi.GetMultipleResponseCallback;
import ch.epfl.sweng.evento.RestApi.RestApi;

/**
 * Created by Val on 28.10.2015.
 */
public enum EventDatabase {
    INSTANCE;

    private static final String TAG = "EventDatabase";

    private RestApi mRestAPI;

    /**
     * This EventSet represents all the Events currently stored on the device
     */
    private EventSet mEventSet;

    EventDatabase() {
        mEventSet = new EventSet();
        mRestAPI = new RestApi(new DefaultNetworkProvider(), Settings.getServerUrl());

        loadNewEvents();
    }



    public void loadNewEvents() {
        mRestAPI.getAll(new GetMultipleResponseCallback() {
            @Override
            public void onDataReceived(List<Event> events) {
                addAll(events);
            }
        });
    }


    public void addAll(List<Event> events) {
        if(events == null) {
            return;
        }
        for (Event e : events) {
            mEventSet.addEvent(e);
                Log.i(TAG, "EVENT LOADED " + e.getTitle());
            }
        }
    }

    public void addOne(Event e) {
        if(e== null){
            return;
        }
        mEventSet.addEvent(e);
    }


    public void loadByDate(GregorianCalendar start, GregorianCalendar end) {
        mRestAPI.getMultiplesEventByDate(start, end, new GetMultipleResponseCallback() {
            @Override
            public void onDataReceived(List<Event> eventArrayList) {
                mEventSet.clear();
                addAll(eventArrayList);
            }
        });
    }

    /**
     * Returns the Event corresponding to the ID passed in argument
     *
     * @param signature the Signature of the desired Event
     * @return the Event corresponding to the Signature.
     */
    public Event getEvent(long signature) {
        return mEventSet.get(signature);
    }

    public Event getFirstEvent() {
        return mEventSet.getFirst();
    }

    public List<Event> getAllEvents() {
        return mEventSet.toArrayList();
    }

    /**
     * This method returns the next Event after the one passed in argument, in the order of starting
     * CustomDate and ID. If 'current' is the last one, it will return it instead.
     *
     * @param current the current Event which is the reference to get the next Event
     * @return the Event that is right after the 'current' Event in the starting CustomDate order
     */
    public Event getNextEvent(Event current) {
        return mEventSet.getNext(current);
    }

    public Event get(int position) {
        Event currentEvent = getFirstEvent();
        for (int i = 0; i <= position; i++) {
            currentEvent = getNextEvent(currentEvent);
        }
        return currentEvent;
    }

    public int getPosition(long signature) {
        return mEventSet.getPosition(signature);
    }

    //public Event getNextEvent(long signature) { return mEventSet.getNext(signature);}

    /**
     * This method returns the previous Event before the one passed in argument, in the order of starting
     * CustomDate and ID. If 'current' is the first one, it will return it instead.
     *
     * @param current the current Event which is the reference to get the previous Event
     * @return the Event that is right before the 'current' Event in the starting CustomDate order
     */

    public Event getPreviousEvent(Event current) {
        return mEventSet.getPrevious(current);
    }

    public EventSet filter(LatLng latLng, double distance) {
        return mEventSet.filter(latLng, distance);
    }

    public EventSet filter(Set<String> tags) {
        return mEventSet.filter(tags);
    }

    public EventSet filter(String tag) {
        return mEventSet.filter(tag);
    }

    public EventSet filter(Event.CustomDate startDate) {
        return mEventSet.filter(startDate);
    }


    public void refresh() {
        mEventSet.clear();
        loadNewEvents();
    }

    public void clear() {
        mEventSet.clear();
    }


}
