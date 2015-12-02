package ch.epfl.sweng.evento;

import android.os.Build;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import ch.epfl.sweng.evento.Events.Event;

/**
 * Created by Gaffinet on 23/11/2015.
 */
public class User {

//---------------------------------------------------------------------------------------------
//----Attributes-------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------

    private static final String TAG = "User";

    private int mID;//needed for getting the user's information from server
    private String mUsername;
    private String mEmail;
    //private Event.CustomDate mDateOfBirth;
    //A unique Id for each Google Account

    private Set<Event> mMatchedEvent;
    private Set<Event> mHostedEvent;
    //private LatLng mHomeAddress;
    //private Event.CustomDate mStartOfMembership;

    public User(int id, String username, String email){
        mID = id;
        mUsername = username;
        mEmail = email;
        mMatchedEvent = new HashSet<>();
        mHostedEvent = new HashSet<>();
    }
//---------------------------------------------------------------------------------------------
//----Methods----------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------


    public String getmUsername() {
        return mUsername;
    }

    public Set<Event> getMatchedEvent() { return mMatchedEvent;}

    public Set<Event> getHostedEvent() { return mHostedEvent; }

    public String getmEmail() {
        return mEmail;
    }

    /*public Event.CustomDate getmDateOfBirth() {
        return mDateOfBirth;
    }*/


    /*
    public LatLng getmHomeAddress() {
        return mHomeAddress;
    }

    public Event.CustomDate getmStartOfMembership() {
        return mStartOfMembership;
    }
    */

    public void addHostedEvent(Event event) {
        final String message = "Cannot add a null event as a hosted event";
        if(Build.VERSION.SDK_INT >= 19) {
            mHostedEvent.add(Objects.requireNonNull(event, message));
        } else {
            if (event != null){
                mHostedEvent.add(event);
            } else {
                throw new NullPointerException(message);
            }
        }
    }

    public void addMatchedEvent(Event event) {
        final String message = "Cannot add a null event as a matched event";
        if(Build.VERSION.SDK_INT >= 19) {
            mMatchedEvent.add(Objects.requireNonNull(event, message));
        } else {
            if (event != null){
                mMatchedEvent.add(event);
            } else {
                throw new NullPointerException(message);
            }
        }
    }

    /*public String getMatchedEventString(String separator) {
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

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

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
