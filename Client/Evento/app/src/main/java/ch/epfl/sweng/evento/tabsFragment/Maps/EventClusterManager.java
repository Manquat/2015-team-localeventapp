package ch.epfl.sweng.evento.tabsFragment.Maps;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.Collection;

import ch.epfl.sweng.evento.Events.Event;

/**
 * Created by Gautier on 02/11/2015.
 */
public class EventClusterManager extends ClusterManager<Event> implements
        ClusterManager.OnClusterClickListener<Event>,
        ClusterManager.OnClusterInfoWindowClickListener<Event>,
        ClusterManager.OnClusterItemClickListener<Event>,
        ClusterManager.OnClusterItemInfoWindowClickListener<Event> {
    final static String TAG = "EventClusterManager";

    private Context mContext;
    private Collection<Event> mEventsClick;

    public EventClusterManager(Context context, GoogleMap map) {
        super(context, map);
        mContext = context;
        init();
    }

    public EventClusterManager(Context context, GoogleMap map, MarkerManager markerManager) {
        super(context, map, markerManager);
        mContext = context;
        init();
    }

    /**
     * Initialize the class
     */
    private void init() {
        setOnClusterClickListener(this);
        setOnClusterInfoWindowClickListener(this);
        setOnClusterItemClickListener(this);
        setOnClusterItemInfoWindowClickListener(this);
    }

    /**
     * Callback for the OnClusterClickListener interface
     *
     * @param cluster the cluster that have been click
     * @return false to let the normal callback function for a marker execute
     */
    @Override
    public boolean onClusterClick(Cluster<Event> cluster) {
        Collection<Event> events = new ArrayList<>(cluster.getItems()); //defensive copy to avoid the border effect
        if (events.size() == 0) {
            Log.d(TAG, "this cluster is empty");
            throw new OutOfMemoryError();
        } else {
            // Show a toast with some info when the cluster is clicked.
            Event event = events.iterator().next();
            String firstName = event.getTitle();
            Toast.makeText(mContext, cluster.getSize() + " (including " + firstName + ")", Toast.LENGTH_SHORT).show();
        }

        // store the actual events in the member
        mEventsClick = events;

        return false;
    }

    /**
     * Callback for the onClusterInfoWindowClickListener interface
     *
     * @param cluster the cluster of whom the infoWindows have been click
     */
    @Override
    public void onClusterInfoWindowClick(Cluster<Event> cluster) {
        // TODO Does nothing, but you could go to a list of the events.
    }

    /**
     * Callback for the onClusterItemClickListener interface
     *
     * @param event the event that have been click
     * @return false to let the normal callback function for a marker execute
     */
    @Override
    public boolean onClusterItemClick(Event event) {
        if (mEventsClick == null) {
            mEventsClick = new ArrayList<Event>();
        }

        mEventsClick.clear();
        mEventsClick.add(event);
        // TODO Does nothing, but you could go into the event's page, for example.
        return false;
    }

    /**
     * Callback for the onClusterItemInfoWindowClick interface
     *
     * @param event the event of which the infoWindows have been click
     */
    @Override
    public void onClusterItemInfoWindowClick(Event event) {
        // TODO Does nothing, but you could go into the user's profile page, for example.
    }

    /**
     * Getter of the current (or last previous) clicked events
     *
     * @return a new collection containing the events currently (or last previous) click
     */
    public Collection<Event> getEventsClick() {
        // defensive copy
        return new ArrayList<Event>(mEventsClick);
    }
}
