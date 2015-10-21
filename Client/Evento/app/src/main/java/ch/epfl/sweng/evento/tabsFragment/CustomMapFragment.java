package ch.epfl.sweng.evento.tabsFragment;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;

import ch.epfl.sweng.evento.Event;
import ch.epfl.sweng.evento.R;

import static com.google.android.gms.common.api.GoogleApiClient.*;

/**
 * Created by Gautier on 22/10/2015.
 */
public class CustomMapFragment extends SupportMapFragment implements
        ConnectionCallbacks,
        OnConnectionFailedListener,
        OnMyLocationButtonClickListener
{
    // LogCat tag
    private static final String TAG = MapsFragment.class.getSimpleName();

    private GoogleMap mMap;
    private Event mEvent;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        //TODO get the event(s) from the database
        mEvent = new Event(1,"Event1","This is a first event",1.1,1.1,"1 long street","alfredo");

        // First we need to check availability of play services
        if (checkPlayServices())
        {
            // Building the GoogleApi client
            buildGoogleApiClient();
        }

        mMap = getMap();

        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
    }

    /**
     * Create the Google API client object
     */
    protected synchronized void buildGoogleApiClient()
    {
        mGoogleApiClient = new Builder(getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    /**
     * Method that verify the availability of the google play services
     */
    protected boolean checkPlayServices()
    {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getContext());
        if (resultCode != ConnectionResult.SUCCESS)
        {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
            {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            else
            {
                Toast.makeText(getContext(), "This device is not supported",
                        Toast.LENGTH_LONG).show();
                try
                {
                    throw new Exception();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean onMyLocationButtonClick()
    {
        if (mGoogleApiClient.isConnected()) {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (location != null)
            {
                // introduction of randomness
                Random random = new Random();

                // conversion of the location into a LatLng
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                double zoomScale = 1.0 / 60.0;
                LatLng markerLatLng = new LatLng(latitude + random.nextDouble() * zoomScale - 0.5 * zoomScale,
                        longitude + random.nextDouble() * zoomScale - 0.5 * zoomScale);

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(markerLatLng);
                markerOptions.title(mEvent.Title());
                markerOptions.snippet(mEvent.Description());

                mMap.clear();
                mMap.addMarker(markerOptions);
            }
        }

        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onConnected(Bundle bundle) {}

    @Override
    public void onConnectionSuspended(int i)
    {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }
}
