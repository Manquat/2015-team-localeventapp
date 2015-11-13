package ch.epfl.sweng.evento.tabsFragment;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import ch.epfl.sweng.evento.DefaultNetworkProvider;
import ch.epfl.sweng.evento.EventDatabase;
import ch.epfl.sweng.evento.Events.Event;
import ch.epfl.sweng.evento.Events.EventsClusterRenderer;
import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.RestApi.GetResponseCallback;
import ch.epfl.sweng.evento.RestApi.RestApi;
import ch.epfl.sweng.evento.tabsFragment.Maps.EventClusterManager;
import ch.epfl.sweng.evento.ServerUrl;


/**
 * Created by Gautier on 21/10/2015.
 * <p>
 * Fragment that hold the Google map.
 */
public class MapsFragment extends SupportMapFragment implements
        OnMapReadyCallback,
        ConnectionCallbacks,
        OnConnectionFailedListener,
        OnMyLocationButtonClickListener {
    private static final String TAG = MapsFragment.class.getSimpleName();   // LogCat tag
    private static final int NUMBER_OF_MARKERS = 100;                       // Number of marker that will be displayed
    private static final int NUMBER_OF_EVENT = 5;
    private static final float ZOOM_LEVEL = 15.0f;                          // Zoom level of the map at the beginning

    private GoogleMap mMap;
    private Location mLastLocation;
    private EventClusterManager mClusterManager;  // Manage the clustering of the marker and the callback associate

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    private Activity mActivity;                     // not really useful but I think it's more efficient
    private Context mContext;
    private ViewGroup mContainer;

    /**
     * Constructor by default mandatory for fragment class
     */
    public MapsFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContainer = container;
        View view = super.onCreateView(inflater, mContainer, savedInstanceState);

        if (view == null) {
            Log.e(TAG, "The maps view cannot be created");
            throw new NullPointerException();
        }

        getMapAsync(this);

        mContext = view.getContext();

        if (mContext == null)
        {
            Log.e(TAG, "The actual context don't exist");
            throw new NullPointerException();
        }

        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Call when the map is ready to be rendered
     *
     * @param googleMap the map that will be displayed
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);

        // Initialize the manager with the context and the map.
        mClusterManager = new EventClusterManager(mActivity.getApplicationContext(), mMap, getActivity());
        mClusterManager.setRenderer(new EventsClusterRenderer(getContext(), mMap, mClusterManager, null));

        // Point the map's listeners at the listeners implemented by the cluster manager.
        mMap.setOnInfoWindowClickListener(mClusterManager);
        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setInfoWindowAdapter(mClusterManager);

        if (mGoogleApiClient.isConnected()) {
            zoomOnUser();
            addEventsMarker();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (mMap != null) {
            zoomOnUser();
            addEventsMarker();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    /**
     * Callback for the OnConnectionFailed interface
     *
     * @param connectionResult the error return by the google API
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (!connectionResult.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(),
                    mActivity, 0).show();
            Log.e(TAG, "" + connectionResult.getErrorCode());
            return;
        }
    }

    /**
     * Callback for the MyLocation button
     *
     * @return null to conserve the normal behavior (zoom on the user)
     */
    @Override
    public boolean onMyLocationButtonClick() {
        if (mGoogleApiClient.isConnected()) {
            zoomOnUser();
            addEventsMarker();
        }

        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    /**
     * Zoom on the the position of the user and draw some markers
     */
    private void zoomOnUser() {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                    .zoom(ZOOM_LEVEL)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    /**
     * Add the marker of the events to the cluster
     */
    private void addEventsMarker() {
        if (mLastLocation == null) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient); //may return null in case of non connected device
        }

        // introduction of randomness
        Random random = new Random();

        // conversion of the location into a LatLng
        double latitude = 0.;
        double longitude = 0.;
        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
        }
        double zoomScale = 1.0 / 60.0;

        mMap.clear();
        mClusterManager.clearItems();

        Event prevEvent;
        int iTot = 0;

        do {
            prevEvent = EventDatabase.INSTANCE.getFirstEvent();
            for (int j = 0; j < NUMBER_OF_EVENT && iTot < NUMBER_OF_MARKERS; j++, iTot++) {
                double tempLatitude = latitude + random.nextDouble() * zoomScale - 0.5 * zoomScale;
                double tempLongitude = longitude + random.nextDouble() * zoomScale - 0.5 * zoomScale;

                prevEvent = EventDatabase.INSTANCE.getNextEvent(prevEvent);
                mClusterManager.addItem( new Event(
                        prevEvent.getID(),
                        prevEvent.getTitle(),
                        prevEvent.getDescription(),
                        tempLatitude,
                        tempLongitude,
                        prevEvent.getAddress(),
                        prevEvent.getCreator(),
                        prevEvent.getTags(),
                        prevEvent.getStartDate(),
                        prevEvent.getEndDate()
                ));
                mClusterManager.cluster();
            }
        } while (iTot < NUMBER_OF_MARKERS);
    }
}