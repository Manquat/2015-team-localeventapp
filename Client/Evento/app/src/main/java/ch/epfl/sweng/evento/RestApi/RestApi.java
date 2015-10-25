package ch.epfl.sweng.evento.RestApi;

/**
 * Created by jmuth on 15/10/2015.
 */


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ch.epfl.sweng.evento.Events.Event;
import ch.epfl.sweng.evento.NetworkProvider;

/**
 * Entry point into the API. Joachim
 *
 * ONLY GETTASK IS WORKING =D
 * TODO: know the server protocol to provide correct String to GetTask and PostTask
 */
public class RestApi{
    NetworkProvider networkProvider;
    String restUrl = "http://exemple.server.com";

    public RestApi(NetworkProvider networkProvider){
        this.networkProvider = networkProvider;
    }

//    public static RestApi getInstance(){
//        //Choose an appropriate creation strategy.
//    }

    /**
     * Request new events to display in the app, according with the position and the filter
     * preferences of the user
     *
     * @param eventArrayList the ArrayList where you want to push the loaded event.
     */
    public void getEvent(final ArrayList<Event> eventArrayList){

        new GetTask(restUrl, networkProvider, new RestTaskCallback (){
            @Override
            public void onTaskComplete(String response) {
                JSONObject JSONresponse = null;
                Event event;
                try {
                    JSONresponse = new JSONObject(response);
                    event = Parser.parseFromJSON(JSONresponse);
                    eventArrayList.add(event);
                } catch (JSONException e) {
                    // TODO: Manage the exception
                    e.printStackTrace();
                }
            }
        }).execute();
    }

    /**
     * Submit a Event to the server.
     * @param event the new event to post
     * @param callback The callback to execute when submission status is available.
     */
    public void postEvent(Event event, final PostCallback callback){
        String restUrl = Serializer.event(event);
        String requestBody = Serializer.event(event);
        new PostTask(networkProvider, restUrl, requestBody, new RestTaskCallback(){
            public void onTaskComplete(String response){
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
        String restUrl = Serializer.event(event);
        String requestBody = Serializer.event(event);
        new PostTask(networkProvider, restUrl, requestBody, new RestTaskCallback(){
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
        String restUrl = Serializer.event(event);
        String requestBody = Serializer.event(event);
        new PostTask(networkProvider, restUrl, requestBody, new RestTaskCallback (){
            @Override
            public void onTaskComplete(String response){
                callback.onDeleteSucced();
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




