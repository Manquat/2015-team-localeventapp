package ch.epfl.sweng.evento.RestApi;

/**
 * Created by jmuth on 15/10/2015.
 */

/**
 * Class definition for a callback to be invoked when the response data for the
 * GET call is available.
 */
public abstract class DeleteResponseCallback {

    /**
     * Called when the response data for the REST call is ready. <br/>
     * This method is guaranteed to execute on the UI thread.
     */


    public abstract void onDeleteSuccess();

    /*
     * Additional methods like onPreGet() or onFailure() can be added with default implementations.
     * This is why this has been made and abstract class rather than Interface.
     */
}
