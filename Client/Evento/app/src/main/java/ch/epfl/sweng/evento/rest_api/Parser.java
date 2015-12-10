package ch.epfl.sweng.evento.rest_api;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import ch.epfl.sweng.evento.Comment;
import ch.epfl.sweng.evento.User;
import ch.epfl.sweng.evento.event.Event;

/**
 * Created by joachimmuth on 16.10.15.
 */
public class Parser {
    private static final String TAG = "Parser";

    public static ArrayList<Event> events(String s) {
        return null;
    }

    /**
     * converts a JSONObject into an Event
     * @param jsonObject the jsonObject to be converted
     * @return an Event corresponding to the jsonObject passed in argument
     * @throws JSONException
     */
    public static Event toEvent(JSONObject jsonObject) throws JSONException {

        final JSONObject json = jsonObject;

        GregorianCalendar startDate = new GregorianCalendar(0, 0, 0);
        GregorianCalendar endDate = new GregorianCalendar(0, 0, 0);

        try {
            Calendar cal = fromStringToCalendar(jsonObject.getString("date"));
            startDate.setTime(cal.getTime());
            endDate.setTime(startDate.getTime());
            addTime(endDate, jsonObject.getString("duration"));
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
                    jsonObject.getInt("owner"),
                    new HashSet<String>(Arrays.asList(json.getString("tags").split(";"))),
                    startDate,
                    endDate,
                    jsonObject.getString("image"),
                    new HashSet<User>());

        } catch (IllegalArgumentException e) {
            throw new JSONException("Invalid question structure");
        }
    }

    public static User parseUserFromJSON(JSONObject jsonObject) throws JSONException {

        final JSONObject json = jsonObject;

        try {
            return new User(jsonObject.getInt("id"),
                    jsonObject.getString("name"),
                    jsonObject.getString("email")
            );

        } catch (IllegalArgumentException e) {
            throw new JSONException("Invalid question structure");
        }
    }

    /**
     * converts a JSONObject into an Comment
     * @param jsonObject the jsonObject to be converted
     * @return the comment corresponding to the jsonObject passed in argument
     * @throws JSONException
     */
    private static Comment toComment(JSONObject jsonObject) throws JSONException {
        Comment c = new Comment(
                jsonObject.getInt("creator"),
                jsonObject.getString("creator_name"),
                jsonObject.getString("body"),
                jsonObject.getInt("id"));
        return c;
    }

    /**
     * Converts a String containing data for a list of Event into an actual List of Events
     * @param response the json-formatted string containing the data
     * @return a List of Events created from the data passed in argument
     * @throws JSONException
     */
    public static List<Event> toEventList(String response) throws JSONException {
        ArrayList<Event> eventArrayList = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(response);

        for (int i = 0; i < jsonArray.length(); i++) {
            eventArrayList.add(toEvent(jsonArray.getJSONObject(i)));
        }
        return eventArrayList;
    }

    /**
     * Converts a jsonObject containing the data of a User into an actual User
     * @param jsonObject the jsonObject containing the data
     * @return a User based on the data contained in the jsonObject
     * @throws JSONException
     */
    public static User toUser(JSONObject jsonObject) throws JSONException {
        final JSONObject json = jsonObject;

        try {
            return new User(jsonObject.getInt("id"),
                    jsonObject.getString("name"),
                    jsonObject.getString("email")
            );

        } catch (IllegalArgumentException e) {
            throw new JSONException("Invalid question structure");
        }
    }

    /**
     * converts a JSONObject into a List of Comment
     * @param result the jsonObject to be converted
     * @return a List of Comments corresponding to the data contained in the jsonObject
     * @throws JSONException
     */
    public static List<Comment> toCommentList(String result) throws JSONException {
        JSONArray jsonArray = new JSONArray(result);
        List<Comment> commentList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            commentList.add(toComment(jsonArray.getJSONObject(i)));
        }
        return commentList;
    }

    /**
     * converts a JSONObject into a List of Users
     * @param response the String containing the data of the Users
     * @return a List of Users corresponding to the data contained in the String passed in argument
     * @throws JSONException
     */
    public static List<User> toUserList(String response) throws JSONException {
        JSONArray jsonArray = new JSONArray(response);
        List<User> userList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            userList.add(toUser(jsonArray.getJSONObject(i)));
        }

        return userList;
    }


    /**
     * interprets a formatted String into a Calendar
     * @param s the String containing the Calendar data
     * @return a Calendar with the same data as the String passed in argument
     * @throws ParseException
     */
    public static Calendar fromStringToCalendar(String s) throws ParseException {
        Calendar cal = new GregorianCalendar();
        cal.setTimeZone(TimeZone.getTimeZone("Europe/Zurich"));
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.FRANCE);
        SimpleDateFormat timeFormatWithMillis = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.FRANCE);
        if (s.contains(".")) {
            timeFormatWithMillis.setTimeZone(TimeZone.getTimeZone("Europe/Zurich"));
            cal.setTime(timeFormatWithMillis.parse(s));
        } else {
            timeFormat.setTimeZone(TimeZone.getTimeZone("Europe/Zurich"));
            cal.setTime(timeFormat.parse(s));
        }
        return cal;
    }


    public static void addTime(Calendar c, String s) {
        c.add(Calendar.HOUR_OF_DAY, getHour(s));
        c.add(Calendar.MINUTE, getMinutes(s));
        c.add(Calendar.DAY_OF_YEAR, getDays(s));
    }

    private static int getMinutes(String s) {
        int m = Integer.parseInt(s.split(":")[1]);
        return m;
    }

    private static int getHour(String s) {
        //Log.d(TAG, "getHour : " + s);
        String res = s;
        if (res.contains(" ")) {
            res = res.split(" |:")[1];
        } else {
            res = res.split(":")[0];
        }
        int h = Integer.parseInt(res);
        return h;
    }

    private static int getDays(String s) {
        String res = s;
        if (res.contains(" ")) {
            res = res.split(" ")[0];
        } else {
            res = "0";
        }
        int d = Integer.parseInt(res);
        return d;
    }


}
