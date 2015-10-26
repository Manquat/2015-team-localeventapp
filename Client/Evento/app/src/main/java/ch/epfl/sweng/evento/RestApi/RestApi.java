package ch.epfl.sweng.evento.RestApi;

/**
 * Created by jmuth on 15/10/2015.
 */


import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import ch.epfl.sweng.evento.Events.Event;
import ch.epfl.sweng.evento.NetworkProvider;

/**
 * Entry point into the API. Joachim
 *
 * ONLY GETTASK IS WORKING =D
 * TODO: know the server protocol to provide correct String to GetTask and PostTask
 */
public class RestApi{
    private NetworkProvider networkProvider;
    private String urlServer;
    private int onWork = 0;  // ugly trick to wait for REST terminates, while testing
    private int noEvent = 0;
    // TODO: find a better way

    public RestApi(NetworkProvider networkProvider, String urlServer){
        this.networkProvider = networkProvider;
        this. urlServer = urlServer;
    }

//    public static RestApi getInstance() {
//
//    }

    public void waitUntilFinish() throws RestException {
        while (onWork > 0){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RestException(e);
            }

        }
    }


    /**
     * Default getEvent method, without callback.
     * Request new events to display in the app, according with the position and the filter
     * preferences of the user
     *
     * @param eventArrayList the ArrayList where you want to push the loaded event.
     */
    public void getEvent(final ArrayList<Event> eventArrayList){
        getEvent(eventArrayList, new GetResponseCallback() {
            @Override
            void onDataReceived() {
                // do nothing
            }
        });
    }

    /**
     * getEvent method, given an array of events and a specified callback
     * @param eventArrayList
     * @param callback
     */
    public void getEvent(final ArrayList<Event> eventArrayList, final GetResponseCallback callback){
        onWork += 1;
        noEvent += 1;
        String restUrl = UrlMaker.get(urlServer, noEvent);
        new GetTask(restUrl, networkProvider, new RestTaskCallback (){
            @Override
            public void onTaskComplete(String response){
                JSONObject JsonResponse = null;
                Event event;
                try {
                    JsonResponse = new JSONObject(response);
                    event = Parser.parseFromJSON(JsonResponse);
                    eventArrayList.add(event);
                    onWork -= 1;
                } catch (JSONException e) {
                    onWork -= 1;
                    try {
                        throw new RestException(e);
                    } catch (RestException e1) {
                        e1.printStackTrace();
                    }
                    // TODO: Manage the exception
                    e.printStackTrace();
                }
                callback.onDataReceived();
            }
        }).execute();
    }


    /**
     * Submit a Event to the server.
     * @param event the new event to post
     * @param callback The callback to execute when submission status is available.
     */
    public void postEvent(Event event, final PostCallback callback){
        String restUrl = UrlMaker.put(urlServer);
        String requestBody = Serializer.event(event);
        new PutTask(restUrl, networkProvider, requestBody, new RestTaskCallback(){
            public void onTaskComplete(String response){
                onWork -= 1;
                callback.onPostSuccess();
            }
        }).execute();
    }

    /**
     * Update a Event to the server.
     * @param event the event to update on the server
     * @param callback The callback to execute when submission status is available.
     *
     *
     */
    public void updateEvent(Event event, final PutCallback callback){
        String restUrl = UrlMaker.post(urlServer);
        String requestBody = Serializer.event(event);
        new PostTask(restUrl, networkProvider, requestBody, new RestTaskCallback(){
            public void onTaskComplete(String response){
                callback.onPostSuccess();
            }
        }).execute();
    }
    /**
     * Delete a Event of the server.
     * @param event the event to delete
     * @param callback The callback to execute when submission status is available.
     */
    public void deleteEvent(Event event, final DeleteResponseCallback callback){
        String restUrl = UrlMaker.delete(urlServer);
        String requestBody = Serializer.event(event);
        new DeleteTask(restUrl, networkProvider, requestBody, new RestTaskCallback(){
            public void onTaskComplete(String response){
                callback.onDeleteSuccess();
            }
        }).execute();
    }

    /**
     * TODO: the interested and participation options
     *
     * @param event
     * @param callback
     */

    public void setInterestedInEvent(Event event, final PostCallback callback){

    }

    public void setNotInterestedInEvent(Event event, final PostCallback callback){

    }

    public void setParticipateToEvent(Event event, final PostCallback callback){

    }

    public void setNotParticipateToEvent(Event event, final PostCallback callback){

    }

}




