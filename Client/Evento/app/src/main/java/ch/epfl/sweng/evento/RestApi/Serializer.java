package ch.epfl.sweng.evento.RestApi;

import java.text.SimpleDateFormat;
import java.util.Date;

import ch.epfl.sweng.evento.Events.Event;

/**
 * Created by joachimmuth on 16.10.15.
 * <p>
 * Provide the serialization method to transform android class into string understandable by the server
 */
public final class Serializer {


    public static String event(Event e) {


        String res;
        res = "{\n"
                + "  \"Event_name\": \"" + e.getTitle() + "\",\n"
                + "  \"description\": \n"
                + "    \"" + e.getDescription() + "\" ,\n"
                + "  \"latitude\": " + e.getLatitude() + ",\n"
                + "  \"longitude\": " + e.getLongitude() + ",\n"
                + "  \"address\": \"" + e.getAddress() + "\", \n "
                + " \"date\" : \"" + e.getCalendarStart().getTimeInMillis() + "\", \n "
                + "  \"creator\": \"" + e.getCreator() + "\"\n"
                + "}\n";
        return res;
    }


}
