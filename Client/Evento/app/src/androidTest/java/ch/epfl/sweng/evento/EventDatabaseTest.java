package ch.epfl.sweng.evento;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import ch.epfl.sweng.evento.event.Event;

import static junit.framework.Assert.assertEquals;

/**
 * Test the User class
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EventDatabaseTest {

    private final int MOCK_USER_ID = 23;
    private final int MOCK_USER_ID2 = 26;
    private String TAG = "EventTest";
    private GregorianCalendar mStartDate = new GregorianCalendar(2000, 2, 3, 4, 5, 0);
    private GregorianCalendar mEndDate = new GregorianCalendar(2000, 2, 3, 6, 5, 0);
    private Event mEvent1, mEvent2, mEvent3;
    private User mUser, mUser2;
    private Set<String> mTags;
    private Set<User> mParticipants;
    private String mTitle = "Mock Event";
    private String mDescription = "Mock Event description";
    private String mTag = "Football";
    private String mAdress = "Cher les poissons";
    private String mImage = "Here should be an image but there is none, so sad";
    private List<Event> mEvents;

    @Before
    public void init() {
        mTags = new HashSet<>();
        mParticipants = new HashSet<>();
        mEvents = new ArrayList<>();
        mTags.add(mTag);
        mStartDate.setTimeZone(TimeZone.getTimeZone("Europe/Zurich"));
        mEndDate.setTimeZone(TimeZone.getTimeZone("Europe/Zurich"));
        Settings.setUser(new User(MOCK_USER_ID, "MockJo", "mockjo@plop.ch"));
        mUser = new User(MOCK_USER_ID, "Mock User", "mockuser@plop.ch");
        mUser2 = new User(MOCK_USER_ID2, "Mock User 2", "mockuser2@plop.ch");
        mParticipants.add(mUser);
        mEvent1 = new Event(1, mTitle + 1, mDescription + 1, 20.5, 150.7, mAdress, 5, mTags, mStartDate, mEndDate, mImage, mParticipants);
        mEvent2 = new Event(2, mTitle + 2, mDescription + 2, 20.5, 150.7, mAdress, 5, mTags, mStartDate, mEndDate, mImage, mParticipants);
        mEvent3 = new Event(3, mTitle + 3, mDescription + 3, 20.5, 150.7, mAdress, 5, mTags, new GregorianCalendar(2002, 2, 3, 6, 5, 0), new GregorianCalendar(2002, 2, 3, 6, 5, 0), mImage, mParticipants);
        mEvents = Arrays.asList(mEvent1, mEvent2, mEvent3);
        EventDatabase.INSTANCE.addAll(mEvents);
    }

    @Test
    public void testGetEvent() {
        assertEquals(mEvent2, EventDatabase.INSTANCE.getEvent(2));
    }

    @Test
    public void testGetFirstEvent() {
        assertEquals(mEvent1, EventDatabase.INSTANCE.getFirstEvent());
    }

    @Test
    public void testGetAllEvents() {
        assertEquals(mEvents, EventDatabase.INSTANCE.getAllEvents());
    }

    @Test
    public void testGetNextEvent() {
        assertEquals(mEvent2, EventDatabase.INSTANCE.getNextEvent(mEvent1));
    }
    @Test
    public void testGetPreviousEvent() {
        assertEquals(mEvent2, EventDatabase.INSTANCE.getPreviousEvent(mEvent3));
    }
    @Test
    public void testGet() {
        assertEquals(mEvent3, EventDatabase.INSTANCE.get(1));
    }
    @Test
    public void testFilterDate() {
        List<Event> events = new ArrayList<>();
        events.add(mEvent3);
        assertEquals(events, EventDatabase.INSTANCE.filter(new GregorianCalendar(2001, 2, 3, 6, 5, 0)).toArrayList());
    }
}
