package ch.epfl.sweng.evento.rest_api;

import android.util.Log;

import org.json.JSONArray;
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
        JSONArray jsonArray = new JSONArray(response);

        for (int i = 0; i < jsonArray.length(); i++) {
            eventArrayList.add(parseFromJSON(jsonArray.getJSONObject(i)));
        }
        return eventArrayList;
    }

    public static List<Comment> parseFromJsonListOfComment(String result) throws JSONException {
        JSONArray jsonArray = new JSONArray(result);
        List<Comment> commentList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            commentList.add(parseJsonToComment(jsonArray.getJSONObject(i)));
        }
        return commentList;
    }


    private static Calendar fromStringToCalendar(String s) throws ParseException {
        Calendar cal = new GregorianCalendar();
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.FRANCE);
        SimpleDateFormat timeFormatWithMillis = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.FRANCE);
        if (s.contains(".")){
            timeFormatWithMillis.setTimeZone(TimeZone.getTimeZone("Europe/Zurich"));
            cal.setTime(timeFormatWithMillis.parse(s));
        } else {
            timeFormat.setTimeZone(TimeZone.getTimeZone("Europe/Zurich"));
            cal.setTime(timeFormat.parse(s));
        }
        return cal;
    }
}
