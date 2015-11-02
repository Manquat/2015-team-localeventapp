package ch.epfl.sweng.evento.tabsFragment.Maps;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import ch.epfl.sweng.evento.Events.Event;

/**
 * Created by Tago on 02/11/2015.
 */
public class EventClusterManager extends ClusterManager<Event> implements
        ClusterManager.OnClusterClickListener<Event>,
        ClusterManager.OnClusterInfoWindowClickListener<Event>,
        ClusterManager.OnClusterItemClickListener<Event>,
        ClusterManager.OnClusterItemInfoWindowClickListener<Event> {
    public EventClusterManager(Context context, GoogleMap map) {
        super(context, map);
    }

    public EventClusterManager(Context context, GoogleMap map, MarkerManager markerManager) {
        super(context, map, markerManager);
    }

    @Override
    public boolean onClusterClick(Cluster<Event> cluster) {
        return false;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<Event> cluster) {
        // TODO Does nothing, but you could go to a list of the events.
    }

    @Override
    public boolean onClusterItemClick(Event event) {
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(Event event) {
        // TODO Does nothing, but you could go into the user's profile page, for example.
    }
}
