package ch.epfl.sweng.evento;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.TimeZone;

import ch.epfl.sweng.evento.event.Event;

import static junit.framework.Assert.assertEquals;

/**
 * Test the Event class
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EventTest {

    private final Calendar eventDate = new GregorianCalendar(TimeZone.getTimeZone("Europe/Zurich"), Locale.FRANCE);
    private static final String stringDate = "2012-11-02T05:05:00Z";
    private static final int MOCK_USER_ID = 1;
    private static String TAG = "EventTest";
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
    public void testDateToProperString() {
        eventDate.set(2012, 10, 2, 5, 5, 0);
        Event e = new Event(0,
                "foo",
                "foo",
                0,
                0,
                "foo",
                0,
                new HashSet<String>(),
                eventDate,
                eventDate);
        assertEquals(stringDate, e.getProperDateString());

    }
}
