package ch.epfl.sweng.evento.tabsFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.google.android.gms.maps.CameraUpdate;
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

import java.util.Random;

import ch.epfl.sweng.evento.Event;
import ch.epfl.sweng.evento.R;


/**
 * Created by Gautier on 21/10/2015.
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

    private GoogleMap   mMap;
    private Event       mEvent;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    private Activity mActivity;

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

        mEvent = new Event(1,"Event1","This is a first event",1.1,1.1,"1 long street","alfredo");

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

        if (mGoogleApiClient.isConnected())
        {
            zoomOnUser();
        }
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        // TODO do something
        if (mMap != null)
        {
            zoomOnUser();
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

        /*if (!mIntentInProgress)
        {
            mConnectionResult = connectionResult;
        }*/
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
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (lastLocation != null)
        {
            // introduction of randomness
            Random random = new Random();

            // conversion of the location into a LatLng
            double latitude = lastLocation.getLatitude();
            double longitude = lastLocation.getLongitude();
            double zoomScale = 1.0 / 60.0;

            mMap.clear();

            for (int i=0; i < NUMBER_OF_MARKERS; i++)
            {
                LatLng markerLatLng = new LatLng(latitude + random.nextDouble() * zoomScale - 0.5 * zoomScale,
                        longitude + random.nextDouble() * zoomScale - 0.5 * zoomScale);

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(markerLatLng);
                markerOptions.title(mEvent.Title());
                markerOptions.snippet(mEvent.Description());

                mMap.addMarker(markerOptions);
            }

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(latitude, longitude))
                    .zoom(ZOOM_LEVEL)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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