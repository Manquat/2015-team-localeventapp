package ch.epfl.sweng.evento;

import com.google.android.gms.maps.model.LatLng;


import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.evento.Events.Event;
import ch.epfl.sweng.evento.Events.EventSet;
import ch.epfl.sweng.evento.RestApi.GetResponseCallback;
import ch.epfl.sweng.evento.RestApi.RestApi;

/**
 * Created by Val on 28.10.2015.
 */
public enum EventDatabase {
    INSTANCE;

    private RestApi mRestAPI;

    private EventDatabase() {
        eventSet = new EventSet();
        mRestAPI = new RestApi(new DefaultNetworkProvider(), "https://protected-hamlet-4797.herokuapp.com/");

        //temporary mock events
        Event.Date start = new Event.Date(2015, 10, 12, 18, 30);
        Event.Date end = new Event.Date(2015, 10, 12, 20, 30);

        /*Those are mock events before getting them from the database*/
        Event events[];
        events = new Event[5];
        Set<String> tags = new HashSet<String>() {{
            add("tag1");
            add("tag2");
            add("tag3");
            add("tag4");
        }};

        events[0] = new Event(1, "Event1", "This is a first event with ID 1", 1.1, 1.1, "1 long street", "alfredo", tags, start, end);
        events[1] = new Event(3, "Event2", "This is a second event with ID 3", 2.2, 2.2, "2 long street", "bob", tags, start, end);
        events[2] = new Event(123, "Event3", "This is a third event with ID 123", 3.3, 3.3, "3 long street", "charlie", tags, start, end);
        events[3] = new Event(5152, "Event4", "This is a fourth event with ID 5152", 4.4, 4.4, "4 long street", "dinesh", tags, start, end);
        events[4] = new Event(42222, "Event5", "This is a fifth event with ID 42222", 5.5, 5.5, "5 long street", "ethan", tags, start, end);

        eventSet.addEvent(events[0]);
        eventSet.addEvent(events[4]);
        eventSet.addEvent(events[2]);
        eventSet.addEvent(events[3]);
        eventSet.addEvent(events[1]);

        for(int i = 0;i<5;i++)
        {
            mRestAPI.getEvent(new GetResponseCallback() {
                @Override
                public void onDataReceived(Event event) {
                    eventSet.addEvent(event);
                }
            });
        }

    }

    /**
     * Returns the Event corresponding to the ID passed in argument
     *
     * @param id the ID of the desired Event
     * @return the Event corresponding to the ID.
     */
    public Event getEvent(int id) {
        return eventSet.get(id);
    }

    public Event getFirstEvent() {
        return eventSet.getFirst();
    }

    public Event getNextEvent(int ID) { return eventSet.getNext(ID);}

    public Event getPreviousEvent(int ID) { return eventSet.getPrevious(ID);}

    public EventSet filter(LatLng latLng, double distance) {
        return eventSet.filter(latLng, distance);
    }

    public EventSet filter(Set<String> tags) {
        return eventSet.filter(tags);
    }

    public EventSet filter(String tag) {
        return eventSet.filter(tag);
    }

    /**
     * This EventSet represents all the Events currently stored on the device
     */
    private EventSet eventSet;
}
