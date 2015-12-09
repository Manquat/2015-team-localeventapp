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
import ch.epfl.sweng.evento.User;

import static junit.framework.Assert.assertEquals;

/**
 * Test the Event class
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserTest {

    private static String TAG = "UserTest";
    private static User user;
    private static Event eventhosted, eventjoined;
    private static GregorianCalendar startDate = new GregorianCalendar(2000, 2, 3, 4, 5, 0);
    private static GregorianCalendar endDate = new GregorianCalendar(2000, 2, 3, 6, 5, 0);

    @Before
    public void init() {
        startDate.setTimeZone(TimeZone.getTimeZone("Europe/Zurich"));
        endDate.setTimeZone(TimeZone.getTimeZone("Europe/Zurich"));
        user = new User(24, "Mock User", "mockuser@hotmail.com");
        //eventhosted = new Event(3, "Mock Hosted Event", "Mock Hosted Event description", 20.5, 150.7, "Cher les poissons", 24 , new HashSet<String>(), startDate, endDate);
        eventjoined = new Event(25, "Mock Joined Event", "Mock Joined Event description", 42.8, 78.0, "Cher les dauphins", 2 , new HashSet<String>(), startDate, endDate);
    }

    @Test
    public void testGetUserid() {
        assertEquals(24, user.getUserId());
    }
    @Test
    public void testGetUsername() {
        assertEquals("Mock User", user.getUsername());
    }
    @Test
    public void testGetEmail() {
        assertEquals("mockuser@hotmail.com", user.getEmail());
    }
    @Test
    public void testAddMatchedEvent() {
        user.addMatchedEvent(eventjoined);
        assertEquals(eventjoined, user.getMatchedEvent());
    }
}
