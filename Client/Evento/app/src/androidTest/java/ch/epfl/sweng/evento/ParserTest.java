package ch.epfl.sweng.evento;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.MediumTest;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.evento.Events.Event;
import ch.epfl.sweng.evento.RestApi.Parser;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by joachimmuth on 21.10.15.
 */

@RunWith(AndroidJUnit4.class)
@MediumTest
public class ParserTest{
    private static final Parser parser = new Parser();
    private static final String PROPER_JSON_STRING = "{\n"
            + "  \"id\": 17005,\n"
            + "  \"Event_name\": \"My football game\",\n"
            + "  \"description\": \n"
            + "    \"Okay guys, let's play a little game this evening at dorigny. Remember: no doping allowed!\" ,\n"
            + "  \"latitude\": 46.519428,\n"
            + "  \"longitude\": 6.580847,\n"
            + "  \"adress\": \"Terrain de football de Dorigny\" \n "
      //      + "  \"creator\": \"Micheal Jackson\"\n"
            + "}\n";
    private static final Event event = new Event(17005,
            "My football game",
            "Okay guys, let's play a little game this evening at dorigny. Remember: no doping allowed!",
            46.519428, 6.580847,
            "Terrain de football de Dorigny",
            "Micheal Jackson",
            new HashSet<String>());


    @Test
    public void testParsingJsonToEvent() throws JSONException {
        JSONObject jsonObject = new JSONObject(PROPER_JSON_STRING);
        Event eventFromJson = parser.parseFromJSON(jsonObject);
        assertEquals("id correctly parsed", event.ID(), eventFromJson.ID());
        assertEquals("title correctly parsed", event.Title(), eventFromJson.Title());
        assertEquals("description correctly parsed", event.Description(), eventFromJson.Description());
        assertEquals("xLoc correctly parsed", event.Latitude(), eventFromJson.Latitude());
        assertEquals("yLoc correctly parsed", event.Longitude(), eventFromJson.Longitude());
        assertEquals("address correctly parsed", event.Address(), eventFromJson.Address());
        //assertEquals("creator correctly parsed", event.Creator(), eventFromJson.Creator());

    }

}