package ch.epfl.sweng.evento.rest_api.callback;

/**
 * Created by thomas on 3/12/2015.
 */

import ch.epfl.sweng.evento.User;

/**
 * Class definition for a callback to be invoked when the response data for the
 * GET call is available.
 */
public abstract class GetUserCallback {

    /**
     * Called when the response data for the REST call is ready. <br/>
     * This method is guaranteed to execute on the UI thread.
     */
    public abstract void onDataReceived(User user);


    /*
     * Additional methods like onPreGet() or onFailure() can be added with default implementations.
     * This is why this has been made and abstract class rather than Interface.
     */
}
