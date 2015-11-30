package ch.epfl.sweng.evento.RestApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import ch.epfl.sweng.evento.Comment;
import ch.epfl.sweng.evento.Events.Event;

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

        for(String line : responseLines){
            JSONObject jsonObject = new JSONObject(line);
            commentList.add(parseJsonToComment(jsonObject));
        }
        return commentList;
    }



    private static String[] splitJson(String response){
        response = response.replace("},{", "}\n{");
        response = response.substring(1);
        String[]  responseLines = response.split("\n");
        return responseLines;
    }
}
