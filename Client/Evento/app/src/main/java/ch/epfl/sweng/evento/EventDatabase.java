package ch.epfl.sweng.evento;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.evento.Events.Event;
import ch.epfl.sweng.evento.Events.EventSet;

/**
 * Created by Val on 28.10.2015.
 */
public enum EventDatabase {
    INSTANCE;

    EventDatabase() {
        eventSet = new EventSet();

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

        events[0] = new Event(1, "Event1", "This is a first event", 1.1, 1.1, "1 long street", "alfredo", tags, start, end);
        events[1] = new Event(2, "Event2", "This is a second event", 2.2, 2.2, "2 long street", "bob", tags, start, end);
        events[2] = new Event(3, "Event3", "This is a third event", 3.3, 3.3, "3 long street", "charlie", tags, start, end);
        events[3] = new Event(4, "Event4", "This is a fourth event", 4.4, 4.4, "4 long street", "dinesh", tags, start, end);
        events[4] = new Event(5, "Event5", "This is a fifth event", 5.5, 5.5, "5 long street", "ethan", tags, start, end);

        eventSet.addEvent(events[0]);
        eventSet.addEvent(events[1]);
        eventSet.addEvent(events[2]);
        eventSet.addEvent(events[3]);
        eventSet.addEvent(events[4]);



        /*RestApi mRestAPI = new RestApi(new DefaultNetworkProvider(),"http://128.179.179.51:8000/");// R.string.url_server);
        ArrayList<Event> eventList = new ArrayList<>();
        mRestAPI.getEvent(eventList);
        eventSet.addEvent((eventList.get(0)));
        */
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

    /*public Event getNextEvent(int ID)
    {
        return eventSet.getNext(ID);
    }*/

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
