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

    public static ArrayList<Event> events(String s){
        ArrayList<Event> events = null;
        return events;
    }

    public static Event parseFromJSON(JSONObject jsonObject) throws JSONException {

        // Check that jsonObject have requiered field
        // TODO: choose whether other fields are requiered or optional
        if (!(jsonObject.get("id") instanceof Integer)
                || !(jsonObject.get("title") instanceof String)
                ) {
            throw new JSONException("Invalid question structure");
        }


        // TODO: when the tag to Event will be added ;)
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
                    jsonObject.getString("title"),
                    jsonObject.getString("description"),
                    jsonObject.getDouble("xLocation"),
                    jsonObject.getDouble("yLocation"),
                    jsonObject.getString("address"),
                    jsonObject.getString("creator"),
                    new HashSet<String>());
        } catch (IllegalArgumentException e) {
            throw new JSONException("Invalid question structure");
        } catch (NullPointerException e) {
            throw new JSONException("Invalid question structure");
        }
    }
}
