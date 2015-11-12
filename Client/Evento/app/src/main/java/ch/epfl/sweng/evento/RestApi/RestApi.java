package ch.epfl.sweng.evento.RestApi;

/**
 * Created by jmuth on 15/10/2015.
 */


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import ch.epfl.sweng.evento.Events.Event;
import ch.epfl.sweng.evento.NetworkProvider;

/**
 * RestAPI
 * This allow main thread to send four basic action to Django server: GET, POST, PUT, DELETE
 *
 * Each of the method ask for a Callback allowing the main thread to make an action when the task ends
 * The method return a RESULT string that can be an event (in case of Get) or an HTTPcode (others)
 * In case of FAILURE, response is null and main thread has to deal with it in his callback.
 * (exceptions are logged in Log.e with tag RestException)
 *
 */
public class RestApi{
    private static final String TAG = "RestApi";
    private NetworkProvider mNetworkProvider;
    private String mUrlServer;
    // TODO: as soon as the server provide a better way to get event, change it
    private int mNoEvent = 1;


    public RestApi(NetworkProvider networkProvider, String urlServer){
        this.mNetworkProvider = networkProvider;
        this.mUrlServer = urlServer;
    }

    /**
     * get one event each call
     * @param callback : receive an event as parameter and typically put it in a set
     */
    public void getEvent(final GetResponseCallback callback){
        //mNoEvent += 1;
        String restUrl = UrlMaker.get(mUrlServer, mNoEvent);
        new GetTask(restUrl, mNetworkProvider, new RestTaskCallback (){
            @Override
            public void onTaskComplete(String response){
                Event event = null;
                if (response != null) //TODO treat this problem nicely
                {
                    try
                    {
                        JSONObject JsonResponse = new JSONObject(response);
                        event = Parser.parseFromJSON(JsonResponse);
                    } catch (JSONException e)
                    {
                        Log.e("RestException", "Exception thrown in getEvent", e);
                    }

                }
                callback.onDataReceived(event);
            }
        }).execute();
    }

    /**
     * post event WITHOUT choosing id (typically, set it to 0, the method clear it anyway)
     * @param event
     * @param callback : manage null response (failure case) and success with, typically, a
     *                 message for the user
     */
    public void postEvent(Event event, final PostCallback callback){
        String restUrl = UrlMaker.post(mUrlServer);
        String requestBody = Serializer.event(event);
        new PostTask(restUrl, mNetworkProvider, requestBody, new RestTaskCallback(){
            public void onTaskComplete(String response){
                callback.onPostSuccess(response);
            }
        }).execute();
    }


    /**
     * update an event based on its ID
     * @param event
     * @param callback : manage failure and success case
     */
    public void updateEvent(Event event, final PutCallback callback){
        String restUrl = UrlMaker.put(mUrlServer, event.getID());
        String requestBody = Serializer.event(event);
        new PutTask(restUrl, mNetworkProvider, requestBody, new RestTaskCallback(){
            public void onTaskComplete(String response){
                callback.onPostSuccess(response);
            }
        }).execute();
    }

    /**
     * delete event base on its ID
     * @param id
     * @param callback : manage failure and success
     */
    public void deleteEvent(int id, final DeleteResponseCallback callback){
        String restUrl = UrlMaker.delete(mUrlServer, id);
        new DeleteTask(restUrl, mNetworkProvider, new RestTaskCallback(){
            public void onTaskComplete(String response){
                callback.onDeleteSuccess(response);
            }
        }).execute();
    }


}




