package ch.epfl.sweng.evento;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.evento.Events.Event;

/**
 * Created by Gaffinet on 23/11/2015.
 */
public class UserProfilInformation {

//---------------------------------------------------------------------------------------------
//----Attributes-------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------

    private static final String TAG = "UserProfilIndormation";

    private String mUsername;
    private String mEmail;
    //private Event.CustomDate mDateOfBirth;
    //A unique Id for each Google Account
    private String mUserId;
    private Set<Event> mMatchedEvent;
    private Set<Event> mHostedEvent;
    //private LatLng mHomeAddress;
    //private Event.CustomDate mStartOfMembership;


    public UserProfilInformation(){
        mUsername = "Alfred";
        mMatchedEvent = new HashSet<>();
        mHostedEvent = new HashSet<>();
    }

    public UserProfilInformation(String username,Set<Event> matchedEvent, Set<Event> hostedEvent){
        mUsername = username;
        mMatchedEvent = new HashSet<>(matchedEvent);
        mHostedEvent = new HashSet<>(hostedEvent);
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

    public String getmUserId() {
        return mUserId;
    }

    /*
    public LatLng getmHomeAddress() {
        return mHomeAddress;
    }

    public Event.CustomDate getmStartOfMembership() {
        return mStartOfMembership;
    }
    */

    public boolean addHostedEvent(Event event) {
        if (event != null) {
            mHostedEvent.add(event);
        }
        return false;
    }

    public boolean addMatchedEvent(Event event) {
        if (event != null) {
            mMatchedEvent.add(event);
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
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    /*public void setmDateOfBirth(Event.CustomDate mDateOfBirth) {
        this.mDateOfBirth = mDateOfBirth;
    }*/

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    /*
    public void setmHomeAddress(LatLng mHomeAddress) {
        this.mHomeAddress = mHomeAddress;
    }

    public void setmStartOfMembership(Event.CustomDate mStartOfMembership) {
        this.mStartOfMembership = mStartOfMembership;
    }
    */
}
