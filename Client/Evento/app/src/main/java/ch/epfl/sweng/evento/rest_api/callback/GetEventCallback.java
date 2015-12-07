package ch.epfl.sweng.evento.rest_api.callback;

/**
 * Created by cerschae on 15/10/2015.
 */

import ch.epfl.sweng.evento.event.Event;
import ch.epfl.sweng.evento.User;

/**
 * Class definition for a callback to be invoked when the response data for the
 * GET call is available.
 */
public abstract class GetEventCallback {

    /**
     * Called when the response data for the REST call is ready. <br/>
     * This method is guaranteed to execute on the UI thread.
     */
    public abstract void onEventReceived(Event event);
    public abstract void onDataReceived(User user);


    /*
     * Additional methods like onPreGet() or onFailure() can be added with default implementations.
     * This is why this has been made and abstract class rather than Interface.
     */
}
