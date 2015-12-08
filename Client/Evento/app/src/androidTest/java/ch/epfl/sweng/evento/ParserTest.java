package ch.epfl.sweng.evento;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.TimeZone;

import ch.epfl.sweng.evento.event.Event;
import ch.epfl.sweng.evento.rest_api.Parser;
import ch.epfl.sweng.evento.rest_api.Serializer;

import static junit.framework.Assert.assertEquals;

/**
 * Created by gautier on 03/12/2015.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ParserTest {

    private static final int MOCK_USER_ID = 1;
    private static String TAG = "ParserTest";
    private static GregorianCalendar startDate = new GregorianCalendar(2000, 2, 3, 4, 5, 0);
    private static GregorianCalendar endDate = new GregorianCalendar(2000, 2, 3, 6, 5, 0);
    private static Event event;

    private static String eventStringReceived;

    @Before
    public void init() {
        // force TimeZone, for jenkins
        startDate.setTimeZone(TimeZone.getTimeZone("Europe/Zurich"));
        endDate.setTimeZone(TimeZone.getTimeZone("Europe/Zurich"));
        Settings.setUser(new User(MOCK_USER_ID, "MockJo", "mockjo@plop.ch"));
        event = new Event(0, "", "", 0, 0, "", 0, new HashSet<String>(), startDate, endDate);
        eventStringReceived = "{\n"
                + "  \"id\": 0,\n"
                + "  \"Event_name\": \"My football game\",\n"
                + "  \"tags\": \"" + event.getTagsString() + "\",\n"
                + "  \"image\": \n"
                + "    \"" + event.samplePicture() + "\" ,\n"
                + "  \"description\": \n"
                + "    \"Okay guys, let's play a little game this evening at dorigny. Remember: no doping allowed!\" ,\n"
                + "  \"latitude\": 46.519428,\n"
                + "  \"longitude\": 6.580847,\n"
                + "  \"address\": \"Terrain de football de Dorigny\",\n"
                + "  \"date\":\"" + event.getProperDateString() + "\",\n"
                + "  \"duration\":\"02:00:00\",\n"
                + "  \"owner\":\"" + MOCK_USER_ID + "\"\n"
                + "}\n";
    }

    @Test
    public void serializerMilliToHHMMSSTest() {
        long l = 7200000;
        String s = Serializer.fromMillisToHHMMSS(l);
        assertEquals("02:00:00", s);
    }


    @Test
    public void serializerDurationTest() throws JSONException {
        String s = Serializer.event(event);
        JSONObject jsonObject = new JSONObject(s);
        assertEquals("duration : ", "02:00:00", jsonObject.getString("duration"));
    }

    @Test
    public void parserDurationTest() throws JSONException, ParseException {
        JSONObject jsonObject = new JSONObject(eventStringReceived);
        Event e = Parser.toEvent(jsonObject);
        assertEquals(endDate.getTimeInMillis(), e.getEndDate().getTimeInMillis());
    }


    @Test
    public void fromStringToCalendarWithMillisecondTest() throws ParseException {
        String date = "2015-12-01T17:26:13.032000Z";
        Calendar calendar = new GregorianCalendar(2015, 11, 1, 17, 26, 13);
        // set it for jenkins
        calendar.setTimeZone(TimeZone.getTimeZone("Europe/Zurich"));

        Calendar result = Parser.fromStringToCalendar(date);

        assertEquals(calendar.get(Calendar.YEAR), result.get(Calendar.YEAR));
        assertEquals(calendar.get(Calendar.MONTH), result.get(Calendar.MONTH));
        assertEquals(calendar.get(Calendar.DAY_OF_MONTH), result.get(Calendar.DAY_OF_MONTH));
        assertEquals(calendar.get(Calendar.HOUR_OF_DAY), result.get(Calendar.HOUR_OF_DAY));
        assertEquals(calendar.get(Calendar.MINUTE), result.get(Calendar.MINUTE));
    }

    @Test
    public void fromStringToCalendarTest() throws ParseException {
        String date = "2015-12-01T17:26:13Z";
        Calendar calendar = new GregorianCalendar(2015, 11, 1, 17, 26, 13);
        // set it for jenkins
        calendar.setTimeZone(TimeZone.getTimeZone("Europe/Zurich"));

        Calendar result = Parser.fromStringToCalendar(date);

        assertEquals(calendar.get(Calendar.YEAR), result.get(Calendar.YEAR));
        assertEquals(calendar.get(Calendar.MONTH), result.get(Calendar.MONTH));
        assertEquals(calendar.get(Calendar.DAY_OF_MONTH), result.get(Calendar.DAY_OF_MONTH));
        assertEquals(calendar.get(Calendar.HOUR_OF_DAY), result.get(Calendar.HOUR_OF_DAY));
        assertEquals(calendar.get(Calendar.MINUTE), result.get(Calendar.MINUTE));
    }


}
