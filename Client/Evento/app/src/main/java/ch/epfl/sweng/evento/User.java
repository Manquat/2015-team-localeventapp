package ch.epfl.sweng.evento;

import android.os.Build;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import ch.epfl.sweng.evento.event.Event;


/**
 * Created by Gaffinet on 30/11/2015.
 */
public class User {

    private static final String TAG = "User";

    //private Event.CustomDate mDateOfBirth;
    //A unique Id for each Google Account

    //A unique Id for each Google Account
    private String mUserId;
    private String mUsername;
    private String mEmail;
 	private Set<Event> mMatchedEvent;
    private Set<Event> mHostedEvent;
 	private int mID;

	public User(int id, String username, String email){
	    mID = id;
	    mUsername = username;
	    mEmail = email;
	    mMatchedEvent = new HashSet<>();
	    mHostedEvent = new HashSet<>();
	}
	
	public User(String username,Set<Event> matchedEvent, Set<Event> hostedEvent){
	    mUsername = username;
	    mMatchedEvent = new HashSet<>(matchedEvent);
	    mHostedEvent = new HashSet<>(hostedEvent);
    }



    public User(String mUserId, String mUsername, String mEmail) {
        this.mUserId = mUserId;
        this.mUsername = mUsername;
        this.mEmail = mEmail;
    }

    public Set<Event> getMatchedEvent() { return mMatchedEvent;}

    public Set<Event> getHostedEvent() { return mHostedEvent; }

    public int getID(){
        return mID;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getGoogleID() {
        return mUserId;
    }



    public void addHostedEvent(Event event) {
        final String message = "Cannot add a null event as a hosted event";
        if (event != null){
            mHostedEvent.add(event);
        } else {
            throw new NullPointerException(message);
        }
    }

    public void addMatchedEvent(Event event) {
        final String message = "Cannot add a null event as a matched event";
        if (event != null){
            mMatchedEvent.add(event);
        } else {
            throw new NullPointerException(message);
        }
    }


}