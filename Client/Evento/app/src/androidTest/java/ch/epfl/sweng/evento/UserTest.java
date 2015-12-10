package ch.epfl.sweng.evento;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;

import ch.epfl.sweng.evento.event.Event;

import static junit.framework.Assert.assertEquals;

/**
 * Test the User class
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserTest {

    private String TAG = "UserTest";
    private User mUser;
    private Event mEventjoined;
    private GregorianCalendar mStartDate = new GregorianCalendar(2000, 2, 3, 4, 5, 0);
    private GregorianCalendar mEndDate = new GregorianCalendar(2000, 2, 3, 6, 5, 0);

    @Before
    public void init() {
        mStartDate.setTimeZone(TimeZone.getTimeZone("Europe/Zurich"));
        mEndDate.setTimeZone(TimeZone.getTimeZone("Europe/Zurich"));
        mUser = new User(24, "Mock User", "mockuser@hotmail.com");
        mEventjoined = new Event(25, "Mock Joined Event", "Mock Joined Event description", 42.8, 78.0, "Cher les dauphins", 2, new HashSet<String>(), mStartDate, mEndDate);
    }

    @Test
    public void testGetUserid() {
        assertEquals(24, mUser.getUserId());
    }

    @Test
    public void testGetUsername() {
        assertEquals("Mock User", mUser.getUsername());
    }

    @Test
    public void testGetEmail() {
        assertEquals("mockuser@hotmail.com", mUser.getEmail());
    }

    @Test
    public void testAddMatchedEvent() {
        mUser.addMatchedEvent(mEventjoined);
        List<Event> events = new ArrayList<>();
        events.add(mEventjoined);
        assertEquals(events, mUser.getMatchedEvent());
    }
}
