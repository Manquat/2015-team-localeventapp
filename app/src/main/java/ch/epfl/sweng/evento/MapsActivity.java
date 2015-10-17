package ch.epfl.sweng.evento;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;

import static com.google.android.gms.common.api.GoogleApiClient.*;

public class MapsActivity extends FragmentActivity
        implements
        OnMapReadyCallback,
        ConnectionCallbacks,
        OnConnectionFailedListener,
        GoogleMap.OnMyLocationButtonClickListener
{
    // LogCat tag
    private static final String TAG = MainActivity.class.getSimpleName();

    private GoogleMap mMap;
    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    // These settings are the same as the settings for the map. They will in fact give updates
    // at the maximal rates currently possible.
    private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(5000)         // 5 seconds
            .setFastestInterval(16)    // 16ms = 60fps
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // First we need to check availability of play services
        if (checkPlayServices())
        {
            // Building the GoogleApi client
            buildGoogleApiClient();
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if (mGoogleApiClient != null)
        {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        checkPlayServices();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
    }

    /**
     * Create the Google API client object
     */
    protected synchronized void buildGoogleApiClient()
    {
        mGoogleApiClient = new Builder(this)
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
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS)
        {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
            {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "This device is not supported",
                        Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    //----------------------------------------------------------------------------------------------

    /**
     * Google API callback
     */
    @Override
    public void onConnected(Bundle bundle)
    {

    }

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

    @Override
    public boolean onMyLocationButtonClick()
    {
        if (mGoogleApiClient.isConnected()) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mLastLocation != null)
            {
                // introduction of randomness
                Random random = new Random();

                // conversion of the location into a LatLng
                double latitude = mLastLocation.getLatitude();
                double longitude = mLastLocation.getLongitude();
                double zoomScale = 1.0 / 60.0;
                LatLng markerLatLng = new LatLng(latitude + random.nextDouble() * zoomScale - 0.5 * zoomScale,
                        longitude + random.nextDouble() * zoomScale - 0.5 * zoomScale);

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(markerLatLng);
                markerOptions.title("Marker in somewhere !");

                mMap.clear();
                mMap.addMarker(markerOptions);
            }
        }

        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }
}
