package ch.epfl.sweng.evento.tabsFragment.Maps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.Collection;

import ch.epfl.sweng.evento.EventActivity;
import ch.epfl.sweng.evento.Events.Event;
import ch.epfl.sweng.evento.R;

/**
 * Extension of the cluster manager class to centralize all the management of the markers of the
 * map in one place
 */
public class EventClusterManager extends ClusterManager<Event> implements
        ClusterManager.OnClusterClickListener<Event>,
        ClusterManager.OnClusterInfoWindowClickListener<Event>,
        ClusterManager.OnClusterItemClickListener<Event>,
        ClusterManager.OnClusterItemInfoWindowClickListener<Event>,
        GoogleMap.InfoWindowAdapter,
        GoogleMap.OnInfoWindowClickListener {
    final static String TAG = "EventClusterManager";

//---------------------------------------------------------------------------------------------
//----Members----------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------

    private Context mContext;
    private Collection<Event> mEventsClick;
    private Activity mActivity;
    private GoogleMap mMap;

//---------------------------------------------------------------------------------------------
//----Constructor------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------

    public EventClusterManager(Context context, GoogleMap map, Activity parentActivity) {
        super(context, map);
        mContext = context;
        mMap = map;
        mActivity = parentActivity;
        init();
    }

    public EventClusterManager(Context context, GoogleMap map, Activity parentActivity, MarkerManager markerManager) {
        super(context, map, markerManager);
        mContext = context;
        mMap = map;
        mActivity = parentActivity;
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
            Log.e(TAG, "this cluster is empty");
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

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(cluster.getPosition())
                .zoom(mMap.getCameraPosition().zoom + 1)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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
        // TODO Does nothing, but you could go into the event's page, for example.

        Intent intent = new Intent(mContext, EventActivity.class);
        intent.putExtra(EventActivity.KEYCURRENTEVENT, event.getSignature());
        mActivity.startActivity(intent);
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

    /**
     * Call before creating the marker of the event
     *
     * @param marker the marker that will be displayed
     * @return null to let's the default view display or a view that will be used instead
     */
    @Override
    public View getInfoWindow(Marker marker) {
        // return null to let's the default view display or a view that will be used instead
        return null;
    }

    /**
     * Personalise the info pop-up of the marker
     *
     * @param marker the marker where the user have click
     * @return The view that represent the info bubble
     */
    @Override
    public View getInfoContents(Marker marker) {
        // return null to let's the default view display or a view that will be display inside the
        // default view

        View view;

        switch (mEventsClick.size()) {
            case 0:
                view = null;
                Log.e(TAG, "No actual event clicked");
                break;
            case 1:
                view = ViewGroup.inflate(mContext, R.layout.infomarker_event, null);
                Event event = mEventsClick.iterator().next();
                TextView tvTitle = (TextView) view.findViewById(R.id.info_title);
                tvTitle.setText(event.getTitle());
                tvTitle.setTextColor(ContextCompat.getColor(mContext, R.color.defaultTextColor));

                TextView tvDescription = (TextView) view.findViewById(R.id.info_description);
                tvDescription.setText(event.getDescription());
                tvDescription.setTextColor(ContextCompat.getColor(mContext, R.color.defaultTextColor));
                break;
            default:
                view = ViewGroup.inflate(mContext, R.layout.infomarker_cluster, null);

                LinearLayout layout = (LinearLayout) view.findViewById(R.id.list_event);
                for (Event iEvent : mEventsClick) {
                    TextView textView = new TextView(mContext);
                    textView.setText(iEvent.getTitle());
                    textView.setTextColor(ContextCompat.getColor(mContext, R.color.defaultTextColor));
                    layout.addView(textView);
                }
        }

        return view;
    }
}
