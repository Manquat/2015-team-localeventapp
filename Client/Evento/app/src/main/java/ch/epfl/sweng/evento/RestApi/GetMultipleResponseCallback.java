package ch.epfl.sweng.evento.RestApi;

import java.util.ArrayList;

import ch.epfl.sweng.evento.Events.Event;

/**
 * Created by joachimmuth on 13.11.15.
 */
public abstract class GetMultipleResponseCallback {

    public abstract void onDataReceived(ArrayList<Event> eventArrayList);
}
