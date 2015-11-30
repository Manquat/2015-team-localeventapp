package ch.epfl.sweng.evento.RestApi;

/**
 * Created by jmuth on 15/10/2015.
 */


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.GregorianCalendar;
import java.util.List;

import ch.epfl.sweng.evento.Comment;
import ch.epfl.sweng.evento.Events.Event;
import ch.epfl.sweng.evento.NetworkProvider;

/**
 * RestAPI
 * This allow main thread to send four basic action to Django server: GET, POST, PUT, DELETE
 * <p/>
 * Each of the method ask for a Callback allowing the main thread to make an action when the task ends
 * The method return a RESULT string that can be an event (in case of Get) or an HTTP code (others)
 * In case of FAILURE, response is null and main thread has to deal with it in his callback.
 * (exceptions are logged in Log.e with tag RestException)
 */
public class RestApi {
    private static final String TAG = "RestApi";
    private NetworkProvider mNetworkProvider;
    private String mUrlServer;
    private int mNoEvent = 5;


    public RestApi(NetworkProvider networkProvider, String urlServer) {
        this.mNetworkProvider = networkProvider;
        this.mUrlServer = urlServer;
    }

    /**
     * get one event each call
     *
     * @param callback : receive an event as parameter and typically put it in a set
     */
    public void getEvent(final GetEventCallback callback) {
        //mNoEvent += 1;
        String restUrl = UrlMaker.get(mUrlServer, mNoEvent);
        new GetTask(restUrl, mNetworkProvider, new RestTaskCallback() {
            @Override
            public void onTaskComplete(String response) {
                Event event = null;
                if (response != null) {
                    try {
                        JSONObject JsonResponse = new JSONObject(response);
                        event = Parser.parseFromJSON(JsonResponse);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception thrown in getEvent", e);
                    }

                }
                callback.onEventReceived(event);
            }
        }).execute();
    }

    public void getAll(final GetEventListCallback callback) {
        String restUrl = UrlMaker.getAll(mUrlServer);
        new GetTask(restUrl, mNetworkProvider, new RestTaskCallback() {
            @Override
            public void onTaskComplete(String response) {
                List<Event> eventArrayList = null;
                if (response != null) {
                    try {
                        eventArrayList = Parser.parseFromJSONMultiple(response);
                    } catch (JSONException e) {
                        Log.e(TAG, "exception in JSON parser");
                    }
                }
                callback.onEventListReceived(eventArrayList);
            }
        }).execute();
    }

    public void getMultiplesEventByDate(GregorianCalendar startDate,
                                        GregorianCalendar endDate,
                                        final GetEventListCallback callback) {
        String restUrl = UrlMaker.getByDate(mUrlServer, startDate, endDate);
        new GetTask(restUrl, mNetworkProvider, new RestTaskCallback() {
            @Override
            public void onTaskComplete(String result) {
                List<Event> eventArrayList = null;
                if (result != null) {
                    try {
                        eventArrayList = Parser.parseFromJSONMultiple(result);
                    } catch (JSONException e) {
                        Log.e(TAG, "exception in JSON parser");
                    }

                }
                callback.onEventListReceived(eventArrayList);
            }
        }).execute();
    }

    public void getWithFilter(GregorianCalendar startTime,
                              GregorianCalendar endTime,
                              double latitude,
                              double longitude,
                              double radius,
                              final GetEventListCallback callback) {
        String restUrl = UrlMaker.getWithFilter(mUrlServer, startTime, endTime, latitude, longitude, radius);
        new GetTask(restUrl, mNetworkProvider, new RestTaskCallback() {
            @Override
            public void onTaskComplete(String result) {
                List<Event> eventArrayList = null;
                if (result != null) {
                    try {
                        eventArrayList = Parser.parseFromJSONMultiple(result);
                    } catch (JSONException e) {
                        Log.e(TAG, "exception in JSON parser");
                    }

                }
                callback.onEventListReceived(eventArrayList);
            }
        }).execute();
    }

    public void getComment(int EventId, final GetCommentListCallback callback) {
        final String restUrl = UrlMaker.getComment(mUrlServer, EventId);
        new GetTask(restUrl, mNetworkProvider, new RestTaskCallback() {
            @Override
            public void onTaskComplete(String result) {
                List<Comment> commentList = null;
                if (result != null) {
                    try {
                        commentList = Parser.parseFromJsonListOfComment(result);
                    } catch (JSONException e) {
                        Log.e(TAG, "exception in JSON parser");
                    }
                }
                callback.onCommentListReceived(commentList);
            }
        }).execute();
    }

    public void postComment(int UserId, int EventId, String commentBody,
                            final HttpResponseCodeCallback callback){
        String restUrl = UrlMaker.postComment(mUrlServer);
        String requestBody = Serializer.comment(UserId, EventId, commentBody);
        new PostTask(restUrl, mNetworkProvider, requestBody, new RestTaskCallback() {
            @Override
            public void onTaskComplete(String result) {
                callback.onSuccess(result);
            }
        }).execute();
    }

    /**
     * post event WITHOUT choosing id (typically, set it to 0, the method clear it anyway)
     *
     * @param event
     * @param callback : manage null response (failure case) and success with, typically, a
     *                 message for the user
     */
    public void postEvent(Event event, final HttpResponseCodeCallback callback) {
        String restUrl = UrlMaker.post(mUrlServer);
        String requestBody = Serializer.event(event);
        new PostTask(restUrl, mNetworkProvider, requestBody, new RestTaskCallback() {
            public void onTaskComplete(String response) {
                callback.onSuccess(response);
            }
        }).execute();
    }


    /**
     * update an event based on its ID
     *  @param event
     * @param callback : manage failure and success case
     */
    public void updateEvent(Event event, final HttpResponseCodeCallback callback) {
        String restUrl = UrlMaker.put(mUrlServer, event.getID());
        String requestBody = Serializer.event(event);
        new PutTask(restUrl, mNetworkProvider, requestBody, new RestTaskCallback() {
            public void onTaskComplete(String response) {
                callback.onSuccess(response);
            }
        }).execute();
    }

    /**
     * delete event base on its ID
     *  @param id
     * @param callback : manage failure and success
     */
    public void deleteEvent(int id, final HttpResponseCodeCallback callback) {
        String restUrl = UrlMaker.delete(mUrlServer, id);
        new DeleteTask(restUrl, mNetworkProvider, new RestTaskCallback() {
            public void onTaskComplete(String response) {
                callback.onSuccess(response);
            }
        }).execute();
    }


}




