package ch.epfl.sweng.evento.rest_api;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import ch.epfl.sweng.evento.Comment;
import ch.epfl.sweng.evento.event.Event;

/**
 * Created by joachimmuth on 16.10.15.
 */
public class Parser {
    private static final String TAG = "Parser";

    public static ArrayList<Event> events(String s) {
        return null;
    }

    public static Event parseFromJSON(JSONObject jsonObject) throws JSONException {

        final JSONObject json = jsonObject;

        GregorianCalendar startDate = new GregorianCalendar(0, 0, 0);
        GregorianCalendar endDate = new GregorianCalendar(0, 0, 0);

        try {
            Calendar cal = fromStringToCalendar(jsonObject.getString("date"));
            startDate.setTime(cal.getTime());
            endDate = startDate;
            Log.d(TAG, startDate.toString());
        } catch (ParseException e) {
            Log.e(TAG, "Date not correctly parsed", e);
        }

        try {
            return new Event(jsonObject.getInt("id"),
                    jsonObject.getString("Event_name"),
                    jsonObject.getString("description"),
                    jsonObject.getDouble("latitude"),
                    jsonObject.getDouble("longitude"),
                    jsonObject.getString("address"),
                    jsonObject.getString("creator"),
                    new HashSet<String>(),
                    startDate,
                    endDate);

        } catch (IllegalArgumentException e) {
            throw new JSONException("Invalid question structure");
        }
    }

    private static Comment parseJsonToComment(JSONObject jsonObject) {
        final JSONObject json = jsonObject;

        return new Comment();
    }

    public static List<Event> parseFromJSONMultiple(String response) throws JSONException {
        ArrayList<Event> eventArrayList = new ArrayList<>();
        String[] responseLines = splitJson(response);

        for (String line : responseLines) {
            JSONObject jsonObject = new JSONObject(line);
            eventArrayList.add(parseFromJSON(jsonObject));
        }
        return eventArrayList;
    }

    public static List<Comment> parseFromJsonListOfComment(String result) throws JSONException {
        List<Comment> commentList = new ArrayList<>();
        String[] responseLines = splitJson(result);

        for (String line : responseLines) {
            JSONObject jsonObject = new JSONObject(line);
            commentList.add(parseJsonToComment(jsonObject));
        }
        return commentList;
    }


    private static String[] splitJson(String response) {
        response = response.replace("},{", "}\n{");
        response = response.substring(1);
        String[] responseLines = response.split("\n");
        return responseLines;
    }

    private static Calendar fromStringToCalendar(String s) throws ParseException {
        Calendar cal = new GregorianCalendar();
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.FRANCE);
        timeFormat.setTimeZone(TimeZone.getTimeZone("Europe/Zurich"));
        cal.setTime(timeFormat.parse(s));
        return cal;
    }
}
