package ch.epfl.sweng.evento;


import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.evento.event.Event;


/**
 * Created by Gaffinet on 30/11/2015.
 */
public class User {

    private static final String TAG = "User";

    private int mUserId;
    private String mUsername;
    private String mEmail;

    private List<Event> mMatchedEvent;
    private List<Event> mHostedEvent;


    public User(int id, String username, String email) {
        mUserId = id;
        mUsername = username;
        mEmail = email;
        mMatchedEvent = new ArrayList<>();
        mHostedEvent = new ArrayList<>();
    }

    public List<Event> getMatchedEvent() {
        return mMatchedEvent;
    }

    public void setMatchedEvent(List<Event> mMatchedEvent) {
        this.mMatchedEvent = mMatchedEvent;
    }

    public List<Event> getHostedEvent() {
        return mHostedEvent;
    }

    public void setHostedEvent(List<Event> mHostedEvent) {
        this.mHostedEvent = mHostedEvent;
    }

    public int getUserId() {
        return mUserId;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getEmail() {
        return mEmail;
    }

    public boolean addMatchedEvent(Event event) {
        final String message = "Cannot add a null event as a matched event";
        if (event != null) {
            return mMatchedEvent.add(event);
        } else {
            throw new NullPointerException(message);
        }
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


}
