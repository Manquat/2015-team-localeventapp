package ch.epfl.sweng.evento.RestApi;

import ch.epfl.sweng.evento.Events.Event;
import ch.epfl.sweng.evento.User;

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
                + "  \"description\": \n"
                + "    \"" + e.getDescription() + "\" ,\n"
                + "  \"latitude\": " + e.getLatitude() + ",\n"
                + "  \"longitude\": " + e.getLongitude() + ",\n"
                + "  \"address\": \"" + e.getAddress() + "\", \n "
                + " \"date\" : \"" + e.getProperDateString() + "\", \n "
                + "  \"creator\": \"" + e.getCreator() + "\" \n"
                // + "  \"tags\":" + "Basketball" + "\n"
                + "}\n";
        return res;
    }

    public static String user(User u){

        String res = "{\n"
                + "  \"id\": \"" + u.getUserId() + "\",\n"
                + "  \"name\": \"" + u.getUsername() + "\",\n"
                + "  \"email\": \"" + u.getEmail() + "\",\n"
                + "}\n";

        return res;
    }

}
