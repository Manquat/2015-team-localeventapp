package ch.epfl.sweng.evento.rest_api;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import ch.epfl.sweng.evento.Settings;
import ch.epfl.sweng.evento.event.Event;

import static java.lang.Math.max;

/**
 * Created by joachimmuth on 16.10.15.
 * <p/>
 * Provide the serialization method to transform android class into string understandable by the server
 */
public final class Serializer
{
    private static final String TAG = "Serializer";

    private Serializer()
    {
        // private constructor
    }

    public static String event(Event e)
    {
        long duration = e.getEndDate().getTimeInMillis() - e.getStartDate().getTimeInMillis();


        String res = "{\n"
                + "  \"Event_name\": \"" + e.getTitle() + "\",\n"
                + "  \"tags\": \"" + e.getTagsString() + "\",\n"
                + "  \"image\": \n"
                + "    \"" + e.getPictureAsString() + "\" ,\n"
                + "  \"description\": \n"
                + "    \"" + e.getDescription() + "\" ,\n"
                + "  \"latitude\": " + e.getLatitude() + ",\n"
                + "  \"longitude\": " + e.getLongitude() + ",\n"
                + "  \"address\": \"" + e.getAddress() + "\",\n"
                + "  \"date\":\"" + e.getProperDateString() + "\",\n"
                + "  \"duration\":\"" + fromMillisToHHMMSS(duration) + "\",\n"
                + "  \"owner\":\"" + Settings.getUser().getUserId() + "\"\n"
                + "}\n";
        return res;
    }

    public static String user(String name, String email, String googleid)
    {
        String res = "{\n"
                + "  \"name\": \"" + name + "\", \n"
                + "  \"email\": \"" + email + "\", \n"
                + "  \"googleid\": \"" + googleid + "\" \n"
                + "}\n";

        Log.d(TAG, "Information sent to Server: " + res);

        return res;
    }

    public static String comment(int userId, String userName, int eventId, String commentBody)
    {
        String res = "{\n"
                + "\"body\": \"" + commentBody + "\",\n"
                + "\"creator\": " + userId + ",\n"
                + "\"creator_name\": \"" + userName + "\",\n"
                + "\"event\": " + eventId + "\n"
                + "}\n";
        return res;
    }

    public static String fromMillisToHHMMSS(long millis)
    {
        long millisNonNegative = max(millis, 0);
        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisNonNegative),
                TimeUnit.MILLISECONDS.toMinutes(millisNonNegative) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millisNonNegative) % TimeUnit.MINUTES.toSeconds(1));
        return hms;
    }
}
