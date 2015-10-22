package ch.epfl.sweng.evento;

/**
 * Created by Val on 15.10.2015.
 */

public class Event {
    private final int _ID;
    private final String _title;
    private final String _description;
    private final double _xLocation;
    private final double _yLocation;
    private final String _address;
    private final String _creator;//might be replaced by some kind of User class


    public Event(int id, String title, String description, double x, double y, String address, String creator)
    {
        _ID = id;
        _title = title;
        _description = description;
        _xLocation = x;
        _yLocation = y;
        _address = address;
        _creator = creator;
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

    public double X()
    {
        return _xLocation;
    }

    public double Y()
    {
        return _yLocation;
    }

    public String Address()
    {
        return _address;
    }

    public String Creator()
    {
        return _creator;
    }


}


