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


    //A unique Id for each Google Account
    private int mUserId;
    private String mGoogleId;
    private String mUsername;
    private String mEmail;
 	private Set<Event> mMatchedEvent;
    private Set<Event> mHostedEvent;


	public User(int id, String username, String email){
	    mUserId = id;
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

    public User(String mGoogleId, String mUsername, String mEmail) {
        this.mGoogleId = mGoogleId;
        this.mUsername = mUsername;
        this.mEmail = mEmail;
    }


    public Set<Event> getMatchedEvent() { return mMatchedEvent;}

    public Set<Event> getHostedEvent() { return mHostedEvent; }

    public int getUserId(){
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

    public void setUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public void setUserId(int id){
        this.mUserId = id;
    }

    public void setGoogleId(String mUserId) {
        this.mGoogleId = mUserId;
    }

    public boolean addHostedEvent(Event event) {
        if (event != null) {
            mHostedEvent.add(event);
        } else {
            throw new NullPointerException(message);
        }
    }

    public void addMatchedEvent(Event event) {
        final String message = "Cannot add a null event as a matched event";
        if (event != null){
            return mMatchedEvent.add(event);
        } else {
            throw new NullPointerException(message);
        }
    }

    public boolean removeMatchedEvent(Event event) {
        if (event != null) {
            return mMatchedEvent.remove(event);
        }
        return false;
    }

    public String getMatchedEventString(String separator) {
        String res = "";
        if(!mMatchedEvent.isEmpty()){
            for(Event event: mMatchedEvent){
                res += event.getTitle() + separator;
            }
        }
        return res;
    }

    public String getMatchedEventString() {
        return getMatchedEventString("\n");
    }

    public String getHostedEventString(String separator) {
        String res = "";
        if(!mHostedEvent.isEmpty()){
            for(Event event: mHostedEvent){
                res += event.getTitle() + separator;
            }
        }
        return res;
    }

    public String getHostedEventString() {
        return getHostedEventString("\n");
    }*/

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    /*public void setmDateOfBirth(Event.CustomDate mDateOfBirth) {
        this.mDateOfBirth = mDateOfBirth;
    }*/

    /*
    public void setmHomeAddress(LatLng mHomeAddress) {
        this.mHomeAddress = mHomeAddress;
    }

    public void setmStartOfMembership(Event.CustomDate mStartOfMembership) {
        this.mStartOfMembership = mStartOfMembership;
    }
    */
}
