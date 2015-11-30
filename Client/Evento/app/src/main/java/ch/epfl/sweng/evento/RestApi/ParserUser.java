package ch.epfl.sweng.evento.RestApi;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import ch.epfl.sweng.evento.Events.Event;
import ch.epfl.sweng.evento.User;

/**
 * Created by thomas on 28/11/15.
 */
public class ParserUser extends Parser {
    private static final String TAG = "ParserUser";
    public static ArrayList<User> users(String s) {
        return null;
    }

    public static User parseFromJSON(JSONObject jsonObject) throws JSONException {

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

    public static List<User> parseFromJSONMultiple(String response) throws JSONException {
        Log.d(TAG, response);
        ArrayList<User> userArrayList = new ArrayList<>();

        // split received string into multiple JSONable string
        response = response.replace("},{", "}\n{");
        response = response.substring(1);
        String[] responseLines = response.split("\n");
        int i;
        for (String line : responseLines) {
            JSONObject jsonObject = new JSONObject(line);
            userArrayList.add(parseFromJSON(jsonObject));
        }
        return userArrayList;
    }

}
