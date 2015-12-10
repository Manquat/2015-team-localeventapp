package ch.epfl.sweng.evento.rest_api;

/**
 * Created by jmuth on 15/10/2015.
 */


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.GregorianCalendar;
import java.util.List;

import ch.epfl.sweng.evento.Comment;
import ch.epfl.sweng.evento.Settings;
import ch.epfl.sweng.evento.User;
import ch.epfl.sweng.evento.event.Event;
import ch.epfl.sweng.evento.rest_api.callback.GetCommentListCallback;
import ch.epfl.sweng.evento.rest_api.callback.GetEventCallback;
import ch.epfl.sweng.evento.rest_api.callback.GetEventListCallback;
import ch.epfl.sweng.evento.rest_api.callback.GetUserCallback;
import ch.epfl.sweng.evento.rest_api.callback.GetUserListCallback;
import ch.epfl.sweng.evento.rest_api.callback.HttpResponseCodeCallback;
import ch.epfl.sweng.evento.rest_api.callback.RestTaskCallback;
import ch.epfl.sweng.evento.rest_api.network_provider.NetworkProvider;
import ch.epfl.sweng.evento.rest_api.task.DeleteTask;
import ch.epfl.sweng.evento.rest_api.task.GetTask;
import ch.epfl.sweng.evento.rest_api.task.PostAndGetUserWithIDTask;
import ch.epfl.sweng.evento.rest_api.task.PostTask;
import ch.epfl.sweng.evento.rest_api.task.PutTask;

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
                        event = Parser.toEvent(JsonResponse);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception thrown in getEvent", e);
                    }

                }
                callback.onEventReceived(event);
            }
        }).execute();
    }

    /**
     * Gets a String containing the data of ALL Events stored on the server, parses them and forwards
     * them to the Callback function passed in argument
     * @param callback the callback function supposed to actually add the Events to the EventDatabase
     */
    public void getAll(final GetEventListCallback callback) {
        String restUrl = UrlMaker.getAll(mUrlServer);
        new GetTask(restUrl, mNetworkProvider, new RestTaskCallback() {
            @Override
            public void onTaskComplete(String response) {
                List<Event> eventArrayList = null;
                if (response != null) {
                    try {
                        eventArrayList = Parser.toEventList(response);
                    } catch (JSONException e) {
                        Log.e(TAG, "exception in JSON parser");
                    }
                }
                callback.onEventListReceived(eventArrayList);
            }
        }).execute();
    }


    public void getParticipant(final GetUserListCallback callback, int idEvent) {
        String restUrl = UrlMaker.getUsers(mUrlServer, idEvent);
        new GetTask(restUrl, mNetworkProvider, new RestTaskCallback() {
            @Override
            public void onTaskComplete(String response) {
                List<User> user = null;
                if (response != null) {
                    Log.d(TAG, response);
                    try {
                        user = Parser.toUserList(response);
                    } catch (JSONException e) {
                        Log.e(TAG, "exception in JSON parser");
                    }

                } else {
                    Log.e(TAG, "getParticipant, the response is null");
                }
                callback.onUserListReceived(user);
            }
        }).execute();
    }

    public void getUser(final GetUserCallback callback, int idUser) {
        String restUrl = UrlMaker.getUser(mUrlServer, idUser);
        new GetTask(restUrl, mNetworkProvider, new RestTaskCallback() {
            @Override
            public void onTaskComplete(String response) {
                Log.d(TAG, response);
                User user = null;
                if (response != null) {
                    try {
                        JSONObject JsonResponse = new JSONObject(response);
                        user = Parser.toUser(JsonResponse);
                    } catch (JSONException e) {
                        Log.e(TAG, "exception in JSON parser");
                    }

                }
                callback.onDataReceived(user);
            }
        }).execute();
    }

    public void getUserByName(final GetUserCallback callback, String username) {
        String restUrl = UrlMaker.getUserByName(mUrlServer, username);
        Log.d(TAG, restUrl);
        new GetTask(restUrl, mNetworkProvider, new RestTaskCallback() {
            @Override
            public void onTaskComplete(String response) {
                Log.d(TAG, response);
                User user = null;
                if (response != null) {
                    try {
                        JSONObject JsonResponse = new JSONObject(response);
                        user = Parser.toUser(JsonResponse);
                    } catch (JSONException e) {
                        Log.e(TAG, "exception in JSON parser");
                    }

                }
                callback.onDataReceived(user);
            }
        }).execute();
    }


    public void getHostedEvent(final GetEventListCallback callback, int idUser) {
        String restUrl = UrlMaker.getCreator(mUrlServer, idUser);
        new GetTask(restUrl, mNetworkProvider, new RestTaskCallback() {
            @Override
            public void onTaskComplete(String response) {
                List<Event> event = null;
                if (response != null) {
                    try {
                        event = Parser.toEventList(response);
                    } catch (JSONException e) {
                        Log.e(TAG, "exception in JSON parser");
                    }

                }
                callback.onEventListReceived(event);
            }
        }).execute();
    }

    public void getMatchedEvent(final GetEventListCallback callback, int idUser) {
        String restUrl = UrlMaker.getParticipant(mUrlServer, idUser);
        Log.d(TAG, "toto2 " + restUrl);
        new GetTask(restUrl, mNetworkProvider, new RestTaskCallback() {
            @Override
            public void onTaskComplete(String response) {
                List<Event> event = null;
                if (response != null) {
                    try {
                        event = Parser.toEventList(response);
                    } catch (JSONException e) {
                        Log.e(TAG, "exception in JSON parser");
                    }

                }
                callback.onEventListReceived(event);
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
                        eventArrayList = Parser.toEventList(result);
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
                        eventArrayList = Parser.toEventList(result);
                    } catch (JSONException e) {
                        Log.e(TAG, "exception in JSON parser");
                    }

                }
                callback.onEventListReceived(eventArrayList);
            }
        }).execute();
    }

    public void getComment(int EventId, final GetCommentListCallback callback) {
        String restUrl = UrlMaker.getComment(mUrlServer, EventId);
        Log.d(TAG, "getComment : " + restUrl);
        new GetTask(restUrl, mNetworkProvider, new RestTaskCallback() {
            @Override
            public void onTaskComplete(String result) {
                List<Comment> commentList = null;
                if (result != null) {
                    try {
                        commentList = Parser.toCommentList(result);
                    } catch (JSONException e) {
                        Log.e(TAG, "getComment exception in JSON parser");
                    }
                } else {
                    Log.d(TAG, "getComment null response");
                }
                callback.onCommentListReceived(commentList);
            }
        }).execute();
    }

    public void postComment(int EventId, String commentBody,
                            final HttpResponseCodeCallback callback) {
        String restUrl = UrlMaker.postComment(mUrlServer);
        String requestBody = Serializer.comment(Settings.getUser().getUserId(),
                Settings.getUser().getUsername(),
                EventId, commentBody);
        Log.d(TAG, "postComment : url : " + restUrl);
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

    // Post a user
    public void postUser(String name, String email, String googleid, final GetUserCallback callback) {
        String restUrl = UrlMaker.postUser(mUrlServer);
        Log.d(TAG, "restURL: " + restUrl);
        String requestBody = Serializer.user(name, email, googleid);

        /*new PostUserTask(restUrl, mNetworkProvider, requestBody, new RestTaskCallback() {
            public void onTaskComplete(String response) {
                callback.onSuccess(response);
            }
        }).execute();*/

        new PostAndGetUserWithIDTask(restUrl, mNetworkProvider, requestBody, new RestTaskCallback() {
            @Override
            public void onTaskComplete(String response) {
                Log.d(TAG, response);
                User user = null;
                if (response != null) {
                    try {
                        JSONObject JsonResponse = new JSONObject(response);
                        user = Parser.toUser(JsonResponse);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception thrown in getEvent", e);
                    }

                }
                callback.onDataReceived(user);
            }
        }).execute();
    }

    /**
     * update an event based on its ID
     *
     * @param event
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

    public void addParticipant(int idEvent, int idUser, final HttpResponseCodeCallback callback) {
        String restUrl = UrlMaker.putParticipant(mUrlServer, idEvent, idUser);
        String requestBody = "empty";

        new PutTask(restUrl, mNetworkProvider, requestBody, new RestTaskCallback() {
            public void onTaskComplete(String response) {
                callback.onSuccess(response);
            }
        }).execute();
    }

    public void removeParticipant(int idEvent, int idUser, final HttpResponseCodeCallback callback) {
        String restUrl = UrlMaker.deleteParticipant(mUrlServer, idEvent, idUser);

        new DeleteTask(restUrl, mNetworkProvider, new RestTaskCallback() {
            public void onTaskComplete(String response) {
                callback.onSuccess(response);
            }
        }).execute();
    }

    /**
     * delete event base on its ID
     *
     * @param id
     * @param callback : manage failure and success
     */
    public void deleteEvent(int id, final HttpResponseCodeCallback callback) {
        String restUrl = UrlMaker.delete(mUrlServer, id);
        Log.d(TAG, "deleteEvent : url : " + restUrl);
        Log.d(TAG, "deleteEvent : get :" + UrlMaker.get(mUrlServer, id));
        Log.d(TAG, "deleteEvent : post :" + UrlMaker.post(mUrlServer));
        Log.d(TAG, "deleteEvent : comment :" + UrlMaker.postComment(mUrlServer));
        new DeleteTask(restUrl, mNetworkProvider, new RestTaskCallback() {
            public void onTaskComplete(String response) {
                callback.onSuccess(response);
            }
        }).execute();
    }


}




