package ch.epfl.sweng.evento;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.TimeZone;

import ch.epfl.sweng.evento.Events.Event;

import static junit.framework.Assert.assertEquals;

/**
 * Test the Event class
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EventTest {

    private  final Calendar eventDate = new GregorianCalendar(TimeZone.getTimeZone("Europe/Zurich"), Locale.FRANCE);
    private static final String stringDate = "2012-11-02T05:05:00Z";

    @Test
    public void testDateToProperString() {
        eventDate.set(2012, 10, 2, 5, 5, 0);
        Event e = new Event(0, "foo", "foo", 0, 0, "foo", "foo", new HashSet<String>(), eventDate, eventDate);
        assertEquals(stringDate, e.getProperDateString());

    }
}
