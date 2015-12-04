package ch.epfl.sweng.evento.rest_api;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import ch.epfl.sweng.evento.User;
import ch.epfl.sweng.evento.event.Event;

/**
 * Created by thomas on 28/11/15.
 */
public class ParserEvent {
    private static final String TAG = "ParserEvent";
    public static ArrayList<Event> events(String s) {
        return null;
    }

    public static Event parseFromJSON(JSONObject jsonObject) throws JSONException {


        // when the tag to Event will be added ;)
//        JSONArray jsonTags = jsonObject.getJSONArray("tags");
//        List<String> tags = new ArrayList<String>();
//        for (int i = 0; i < jsonTags.length(); ++i) {
//            if (!(jsonTags.get(i) instanceof String)) {
//                throw new JSONException("Invalid question structure");
//            }
//            tags.add(jsonTags.getString(i));
//        }

        final JSONObject json = jsonObject;

        try {
            return new Event(jsonObject.getInt("id"),
                    jsonObject.getString("Event_name"),
                    jsonObject.getString("description"),
                    jsonObject.getDouble("latitude"),
                    jsonObject.getDouble("longitude"),
                    jsonObject.getString("address"),
                    jsonObject.getInt("creator"),
                    new HashSet<String>(){{ add(json.getString("tags"));}},
                    jsonObject.getString("image"),
                    new HashSet<User>()
            );

        } catch (IllegalArgumentException e) {
            throw new JSONException("Invalid question structure");
        }
    }

    //new HashSet<String>(){{ add(json.getString("tags"));}});

    public static List<Event> parseFromJSONMultiple(String response) throws JSONException {
        Log.d(TAG, response);
        ArrayList<Event> eventArrayList = new ArrayList<>();

        // split received string into multiple JSONable string
        response = response.replace("},{", "}\n{");
        response = response.substring(1);
        String[] responseLines = response.split("\n");
        int i;
        for (String line : responseLines) {
            JSONObject jsonObject = new JSONObject(line);
            eventArrayList.add(parseFromJSON(jsonObject));
        }
        return eventArrayList;
    }
}
