package ch.epfl.sweng.evento.Events;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.util.Set;

/**
 * Created by Val on 15.10.2015.
 */

public class Event implements ClusterItem {
    private final int mID;
    private final String mTitle;
    private final String mDescription;
    private final LatLng mLocation;
    private final String mAddress;
    private final String mCreator;//might be replaced by some kind of User class
    private final Set<String> mTags;

    public Event(int id, String title, String description, double latitude, double longitude, String address, String creator, Set<String> tags)
    {
        mID = id;
        mTitle = title;
        mDescription = description;
        mLocation = new LatLng(latitude, longitude);
        mAddress = address;
        mCreator = creator;
        mTags = tags;
    }

    public int getID()
    {
        return mID;
    }

    public String getTitle()
    {
        return mTitle;
    }

    public String getDescription()
    {
        return mDescription;
    }

    public double getLatitude()
    {
        return mLocation.latitude;
    }

    public double getLongitude()
    {
        return mLocation.longitude;
    }

    public LatLng getLocation() {
        return mLocation;
    }

    public String getAddress()
    {
        return mAddress;
    }

    public String getCreator()
    {
        return mCreator;
    }

    public Set<String> getTags() { return mTags; }


    @Override
    public LatLng getPosition() {
        return mLocation;
    }
}


