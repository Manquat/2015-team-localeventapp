package ch.epfl.sweng.evento;

/**
 * Created by cerschae on 15/10/2015.
 */


import java.util.ArrayList;

/**
 * Entry point into the API. Joachim
 *
 * TODO: know the server protocol to provide correct String to GetTask and PostTask
 */
public class RestApi{
    NetworkProvider networkProvider;

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
     * @param filter the filters of which events we want to get (inculding position and tag)
     * @param callback Callback to execute when the profile is available.
     */
    public void getEvents(EventFilter filter, final GetResponseCallback callback){
        String restUrl = Serializer.filter(filter);
        new GetTask(restUrl, networkProvider, new RestTaskCallback (){
            @Override
            public void onTaskComplete(String response){
                ArrayList<Event> events = Parser.events(response);
                callback.onDataReceived(events);
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




