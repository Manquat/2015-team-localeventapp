package ch.epfl.sweng.evento.RestApi;

import ch.epfl.sweng.evento.Events.Event;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.evento.Events.Event;

/**
 * Created by joachimmuth on 13.11.15.
 */
public abstract class GetMultipleResponseCallback {

    public abstract void onDataReceived(List<Event> eventArrayList);
}
