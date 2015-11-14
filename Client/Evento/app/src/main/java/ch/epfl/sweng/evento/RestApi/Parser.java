package ch.epfl.sweng.evento.RestApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;

import ch.epfl.sweng.evento.Events.Event;

/**
 * Created by joachimmuth on 16.10.15.
 */
public class Parser {

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


        try {
            return new Event(jsonObject.getInt("id"),
                    jsonObject.getString("Event_name"),
                    jsonObject.getString("description"),
                    jsonObject.getDouble("latitude"),
                    jsonObject.getDouble("longitude"),
                    jsonObject.getString("address"),
                    jsonObject.getString("creator"),
                    new HashSet<String>());
        } catch (IllegalArgumentException e) {
            throw new JSONException("Invalid question structure");
        }
    }
}
