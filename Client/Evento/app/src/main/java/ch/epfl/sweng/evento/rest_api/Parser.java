package ch.epfl.sweng.evento.rest_api;

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

    public static Event toEvent(JSONObject jsonObject) throws JSONException, ParseException {

        final JSONObject json = jsonObject;

        GregorianCalendar startDate = new GregorianCalendar(0, 0, 0);
        GregorianCalendar endDate = new GregorianCalendar(0, 0, 0);

        Calendar cal = fromStringToCalendar(jsonObject.getString("date"));
        startDate.setTime(cal.getTime());
        endDate.setTime(startDate.getTime());
        addTime(endDate, jsonObject.getString("duration"));


        Event event = new Event(jsonObject.getInt("id"),
                jsonObject.getString("Event_name"),
                jsonObject.getString("description"),
                jsonObject.getDouble("latitude"),
                jsonObject.getDouble("longitude"),
                jsonObject.getString("address"),
                jsonObject.getInt("owner"),
                new HashSet<String>() {{
                    add(json.getString("tags"));
                }},
                startDate,
                endDate,
                jsonObject.getString("image"),
                new HashSet<User>());

        return event;
    }

    private static Comment toComment(JSONObject jsonObject) throws JSONException {
        Comment c = new Comment(
                jsonObject.getInt("creator"),
                jsonObject.getString("creator_name"),
                jsonObject.getString("body"),
                jsonObject.getInt("id"));
        return c;
    }

    public static List<Event> toEventList(String response) throws JSONException, ParseException {
        ArrayList<Event> eventArrayList = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(response);

        for (int i = 0; i < jsonArray.length(); i++) {
            eventArrayList.add(toEvent(jsonArray.getJSONObject(i)));
        }
        return eventArrayList;
    }

    public static User toUser(JSONObject jsonObject) throws JSONException {
        final JSONObject json = jsonObject;
        User user = new User(jsonObject.getInt("id"),
                jsonObject.getString("name"),
                jsonObject.getString("email"));
        return user;
    }

    public static List<Comment> toCommentList(String result) throws JSONException {
        JSONArray jsonArray = new JSONArray(result);
        List<Comment> commentList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            commentList.add(toComment(jsonArray.getJSONObject(i)));
        }
        return commentList;
    }

    public static List<User> toUserList(String response) throws JSONException {
        JSONArray jsonArray = new JSONArray(response);
        List<User> userList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            userList.add(toUser(jsonArray.getJSONObject(i)));
        }

        return userList;
    }


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
    }

    private static int getMinutes(String s) {
        return 0;
    }

    private static int getHour(String s) {
        int h = Integer.parseInt(s.split(":")[0]);
        return h;
    }


}
