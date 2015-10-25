package ch.epfl.sweng.evento.tabsFragment;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

import ch.epfl.sweng.evento.Event;
import ch.epfl.sweng.evento.R;


/**
 * Created by Gautier on 21/10/2015.
 *
 * Fragment that hold the Google map.
 */
public class MapsFragment extends SupportMapFragment implements
        OnMapReadyCallback,
        ConnectionCallbacks,
        OnConnectionFailedListener,
        OnMyLocationButtonClickListener,
        InfoWindowAdapter
{
    private static final String TAG = MapsFragment.class.getSimpleName();   // LogCat tag
    private static final int NUMBER_OF_MARKERS = 100;                       // Number of marker that will be displayed
    private static final float ZOOM_LEVEL = 15.0f;                          // Zoom level of the map at the beginning

    private GoogleMap           mMap;
    private Location            mLastLocation;
    private Event               mEvent;             // a mock event that would be replicated all over the map
    private Collection<LatLng>  mMarkersLocations;  // the positions of the mock events
    private ClusterManager<Event> mClusterManager;  // Manage the clustering of the marker

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    private Activity mActivity;                     // not really useful but I think it's more efficient

    /**
     * Constructor by default mandatory for fragment class
     */
    public MapsFragment()
    {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        getMapAsync(this);

        mEvent = new Event(1,"Event1","This is a first event",1.1,1.1,"1 long street","alfredo", new HashSet<String>());

        mGoogleApiClient = new GoogleApiClient.Builder(view.getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if (mGoogleApiClient.isConnected())
        {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setInfoWindowAdapter(this);

        // Initialize the manager with the context and the map.
        mClusterManager = new ClusterManager<Event>(mActivity.getApplicationContext(), mMap);

        // Point the map's listeners at the listeners implemented by the cluster manager.
        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        if (mGoogleApiClient.isConnected())
        {
            zoomOnUser();
            addEventsMarker();
        }
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        // TODO do something
        if (mMap != null)
        {
            zoomOnUser();
            addEventsMarker();
        }
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        if (!connectionResult.hasResolution())
        {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(),
                    mActivity, 0).show();
            Log.e(TAG, "" + connectionResult.getErrorCode());
            return;
        }
    }

    /**
     * Callback for the MyLocation button
     * @return null to conserve the normal behavior (zoom on the user)
     */
    @Override
    public boolean onMyLocationButtonClick()
    {
        if (mGoogleApiClient.isConnected())
        {
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
    private void zoomOnUser()
    {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null)
        {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                    .zoom(ZOOM_LEVEL)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    private void addEventsMarker()
    {
        if (mLastLocation == null)
        {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient); //may return null in case of non connected device
        }

        // introduction of randomness
        Random random = new Random();

        // conversion of the location into a LatLng
        double latitude = 0.;
        double longitude = 0.;
        if (mLastLocation != null){
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
        }
        double zoomScale = 1.0 / 60.0;

        mMap.clear();
        mClusterManager.clearItems();

        for (int i=0; i < NUMBER_OF_MARKERS; i++)
        {
            double tempLatitude = latitude + random.nextDouble() * zoomScale - 0.5 * zoomScale;
            double tempLongitude = longitude + random.nextDouble() * zoomScale - 0.5 * zoomScale;

            mClusterManager.addItem(new Event(mEvent.ID(), mEvent.Title(),mEvent.Description(),
                    tempLatitude, tempLongitude, mEvent.Address(), mEvent.Creator(), mEvent.Tags()));
        }
    }

    @Override
    public View getInfoWindow(Marker marker)
    {
        // return null to let's the default view display or a view that will be used instead
        return null;
    }

    /**
     * Personalise the info pop-up of the marker
     * @param marker the marker where the user have click
     * @return The view that represent the info bubble
     */
    @Override
    public View getInfoContents(Marker marker)
    {
        // return null to let's the default view display or a view that will be display inside the
        // default view

        View view = getLayoutInflater(null).inflate(R.layout.infomarker_event, null);

        TextView tvTitle = (TextView) view.findViewById(R.id.info_title);
        tvTitle.setText(mEvent.Title());

        TextView tvDescription = (TextView) view.findViewById(R.id.info_description);
        tvDescription.setText(mEvent.Description());

        return view;
    }
}