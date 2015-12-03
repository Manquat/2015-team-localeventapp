package ch.epfl.sweng.evento.rest_api;

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
                + "  \"participants\": \"" + "Alfred" + "\",\n"
                + "  \"image\": \n"
                + "    \"" + e.getDescription() + "\" ,\n"
                + "  \"description\": \n"
                + "    \"" + e.getDescription() + "\" ,\n"
                + "  \"latitude\": " + e.getLatitude() + ",\n"
                + "  \"longitude\": " + e.getLongitude() + ",\n"
                + "  \"address\": \"" + e.getAddress() + "\", \n "
                + " \"date\" : \"" + e.getProperDateString() + "\", \n "
                + "  \"creator\": \"" + e.getCreator() + "\" \n"
                + "}\n";
        return res;
    }


    public static String comment(int userId, int eventId, String commentBody) {
        String res = "{\n"
                + "\"comment\": \"" + commentBody + "\",\n"
                + "\"user\": \"" + userId + "\",\n"
                + "\"event\": \"" + eventId + "\",\n"
                + "}\n";
        return res;
    }
}
