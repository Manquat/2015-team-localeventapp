package ch.epfl.sweng.evento.Events;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.util.Set;

/**
 * Created by Val on 15.10.2015.
 */

public class Event implements ClusterItem {
    private final int _ID;
    private final String _title;
    private final String _description;
    private final LatLng _location;
    private final String _address;
    private final String _creator;//might be replaced by some kind of User class
    private final Set<String> mTags;

    public Event(int id, String title, String description, double latitude, double longitude, String address, String creator, Set<String> tags)
    {
        _ID = id;
        _title = title;
        _description = description;
        _location = new LatLng(latitude, longitude);
        _address = address;
        _creator = creator;
        mTags = tags;
    }

    public int ID()
    {
        return _ID;
    }

    public String Title()
    {
        return _title;
    }

    public String Description()
    {
        return _description;
    }

    public double Latitude()
    {
        return _location.latitude;
    }

    public double Longitude()
    {
        return _location.longitude;
    }

    public LatLng Location() {
        return _location;
    }

    public String Address()
    {
        return _address;
    }

    public String Creator()
    {
        return _creator;
    }

    public Set<String> Tags() { return mTags; }


    @Override
    public LatLng getPosition() {
        return _location;
    }
}


