package ch.epfl.sweng.evento.RestApi;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.evento.Events.Event;
import ch.epfl.sweng.evento.User;

/**
 * Created by joachimmuth on 13.11.15.
 */
public abstract class GetMultipleResponseCallback {

    public abstract void onDataReceived(List<Event> eventArrayList);
    public void onDataReceived(List<User> userArrayList, int i){}
}
