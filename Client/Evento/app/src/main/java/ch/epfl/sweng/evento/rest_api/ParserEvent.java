package ch.epfl.sweng.evento.rest_api;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.evento.event.Event;

/**
 * Created by thomas on 28/11/15.
 */
public class ParserEvent
{
    private static final String TAG = "ParserEvent";

    public static ArrayList<Event> events(String s)
    {
        return null;
    }

    public static Event parseFromJSON(JSONObject jsonObject) throws JSONException
    {


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

        return null;
    }

    //new HashSet<String>(){{ add(json.getString("tags"));}});

    public static List<Event> parseFromJSONMultiple(String response) throws JSONException
    {

        return null;
    }
}
