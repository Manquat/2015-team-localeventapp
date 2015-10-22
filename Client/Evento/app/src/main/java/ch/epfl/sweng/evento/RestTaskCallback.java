package ch.epfl.sweng.evento;

/**
 * Created by cerschae on 15/10/2015.
 */
public abstract class RestTaskCallback{
    /**
     * Called when the HTTP request completes.
     *
     * @param result The result of the HTTP request.
     */
    public abstract void onTaskComplete(String result);
}