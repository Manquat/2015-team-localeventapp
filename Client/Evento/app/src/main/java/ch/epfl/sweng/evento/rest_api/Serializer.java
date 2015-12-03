package ch.epfl.sweng.evento.rest_api;

import android.util.Log;

import ch.epfl.sweng.evento.Settings;
import ch.epfl.sweng.evento.User;
import ch.epfl.sweng.evento.event.Event;

/**
 * Created by joachimmuth on 16.10.15.
 * <p/>
 * Provide the serialization method to transform android class into string understandable by the server
 */
public final class Serializer {
    private static final String TAG = "Serializer";

    private Serializer() {
        // private constructor
    }

    public static String event(Event e) {

        String res = "{\n"
                + "  \"Event_name\": \"" + e.getTitle() + "\",\n"
                + "  \"tags\": \"" + e.getTagsString() + "\",\n"
                + "  \"image\": \n"
                + "    \"" + e.getDescription() + "\" ,\n"
                + "  \"description\": \n"
                + "    \"" + e.getDescription() + "\" ,\n"
                + "  \"latitude\": " + e.getLatitude() + ",\n"
                + "  \"longitude\": " + e.getLongitude() + ",\n"
                + "  \"address\": \"" + e.getAddress() + "\", \n "
                + " \"date\" : \"" + e.getProperDateString() + "\", \n "
                + "  \"creator\": \"" + Settings.INSTANCE.getUser().getUserId() + "\" \n"
                + "}\n";
        return res;
    }

    public static String user(User u){
        String res = "{\n"
                //+ "  \"id\": "  + ",\n"
                + "  \"name\": \"" + u.getUsername() + "\", \n"
                + "  \"email\": \"" + u.getEmail() + "\", \n"
                + "  \"googleid\": \"" + u.getUserId() + "\" \n"
                + "}\n";

        Log.d(TAG, "Information sent to Server: " + res);

        return res;
    }

    public static String comment(int userId, int eventId, String commentBody) {
        String res = "{\n"
                + "\"comment\": \"" + commentBody + "\",\n"
                + "\"user\": \"" + userId + "\",\n"
                + "\"event\": \"" + eventId + "\" \n"
                + "}\n";
        return res;
    }
}
