package ch.epfl.sweng.evento;


import android.os.Build;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.evento.event.Event;


/**
 * Created by Gaffinet on 30/11/2015.
 */
public class User {

    private static final String TAG = "User";


    //A unique Id for each Google Account
    private int mUserId;
    private String mGoogleId;
    private String mUsername;
    private String mEmail;

 	private List<Event> mMatchedEvent;
    private List<Event> mHostedEvent;


	public User(int id, String username, String email){
	    mUserId = id;
	    mUsername = username;
	    mEmail = email;
	    mMatchedEvent = new ArrayList<>();
	    mHostedEvent = new ArrayList<>();
	}
	
	public User(String username,List<Event> matchedEvent, List<Event> hostedEvent){
	    mUsername = username;
	    mMatchedEvent = (matchedEvent);
	    mHostedEvent = (hostedEvent);
    }


    public User(String mGoogleId, String mUsername, String mEmail) {
        this.mGoogleId = mGoogleId;
        this.mUsername = mUsername;
        this.mEmail = mEmail;
    }

    public List<Event> getMatchedEvent() { return mMatchedEvent;}

    public List<Event> getHostedEvent() { return mHostedEvent; }


    public int getUserId() {
        return mUserId;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getEmail() {
        return mEmail;
    }


    public String getGoogleId() {
        return mGoogleId;
    }




    public void setUserId(int id) {
        this.mUserId = id;
    }

    public void setGoogleId(String mUserId) {
        this.mGoogleId = mUserId;
    }

    public boolean addHostedEvent(Event event) {
        final String message = "Cannot add a null event as a hosted event";
        if (event != null) {
            return mHostedEvent.add(event);
        } else {
            throw new NullPointerException(message);
        }
    }

    public boolean addMatchedEvent(Event event) {
        final String message = "Cannot add a null event as a matched event";
        if (event != null) {
            return mMatchedEvent.add(event);
        } else {
            throw new NullPointerException(message);
        }
    }

    public void setHostedEvent(List<Event> mHostedEvent) {
        this.mHostedEvent = mHostedEvent;
    }

    public void setMatchedEvent(List<Event> mMatchedEvent) {
        this.mMatchedEvent = mMatchedEvent;
    }
    @Override
    public boolean equals(Object object) {
        if (object instanceof User) {
            User user = (User) object;
            return mUserId == user.getUserId();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return mUserId;
    }

    /*public void setmStartOfMembership(Event.CustomDate mStartOfMembership) {
        this.mStartOfMembership = mStartOfMembership;
    }*/
}
