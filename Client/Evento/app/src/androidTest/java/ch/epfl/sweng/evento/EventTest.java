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
import static junit.framework.Assert.fail;

/**
 * Test the Event class
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EventTest {

    private final int MOCK_USER_ID = 23;
    private final int MOCK_USER_ID2 = 26;
    private String TAG = "EventTest";
    private GregorianCalendar mStartDate = new GregorianCalendar(2000, 2, 3, 4, 5, 0);
    private GregorianCalendar mEndDate = new GregorianCalendar(2000, 2, 3, 6, 5, 0);
    private Event mEvent;
    private User mUser, mUser2;
    private HashSet<String> mTags;
    private HashSet<User> mParticipants;
    private String mTitle = "Mock Event";
    private String mDescription = "Mock Event description";
    private String mTag = "Football";
    private String mAdress = "Cher les poissons";
    private String mImage = "Here should be an image but there is none, so sad";

    @Before
    public void init() {
        mTags = new HashSet<>();
        mParticipants = new HashSet<>();
        mTags.add(mTag);
        mStartDate.setTimeZone(TimeZone.getTimeZone("Europe/Zurich"));
        mEndDate.setTimeZone(TimeZone.getTimeZone("Europe/Zurich"));
        mUser = new User(MOCK_USER_ID, "Mock User", "mockuser@plop.ch");
        mUser2 = new User(MOCK_USER_ID2, "Mock User 2", "mockuser2@plop.ch");
        mParticipants.add(mUser);
        mEvent = new Event(3, mTitle, mDescription, 20.5, 150.7, mAdress, 5, mTags, mStartDate, mEndDate, mImage, mParticipants);
    }

    @Test
    public void testGetID() {
        assertEquals(3, mEvent.getID());
    }

    @Test
    public void testGetTitle() {
        assertEquals(mTitle, mEvent.getTitle());
    }

    @Test
    public void testGetDescription() {
        assertEquals(mDescription, mEvent.getDescription());
    }

    @Test
    public void testGetLatitude() {
        assertEquals(20.5, mEvent.getLatitude());
    }

    @Test
    public void testGetLongitude() {
        assertEquals(150.7, mEvent.getLongitude());
    }

    @Test
    public void testGetAddress() {
        assertEquals(mAdress, mEvent.getAddress());
    }

    @Test
    public void testGetCreator() {
        assertEquals(5, mEvent.getCreator());
    }

    @Test
    public void testGetTags() {
        assertEquals(mTags, mEvent.getTags());
    }

    @Test
    public void testGetTagsString() {
        assertEquals(mTag, mEvent.getTagsString());
    }

    @Test
    public void testStartDate() {
        assertEquals(mStartDate, mEvent.getStartDate());
    }

    @Test
    public void testGetEndDate() {
        assertEquals(mEndDate, mEvent.getEndDate());
    }

    @Test
    public void testGetPictureAsString() {
        assertEquals(mImage, mEvent.getPictureAsString());
    }

    @Test
    public void testGetParticipant() {
        assertEquals(mParticipants, mEvent.getAllParticipant());
    }

    @Test
    public void testAddParticipant() {
        mEvent.addParticipant(mUser2);
        mParticipants.add(mUser);
        assertEquals(mParticipants, mEvent.getAllParticipant());
    }

    @Test
    public void testAddNullParticipant() {
        try {
            mEvent.addParticipant(null);
            fail("Did not raise NullPointerException");
        } catch (NullPointerException e) {
        }
    }

    @Test
    public void testRemoveNullParticipant() {
        try {
            mEvent.removeParticipant(null);
            fail("Did not raise NullPointerException");
        } catch (NullPointerException e) {
        }
    }

    @Test
    public void testRemoveParticipant() {
        mEvent.removeParticipant(mUser2);
        mParticipants.remove(mUser2);
        assertEquals(mParticipants, mEvent.getAllParticipant());
    }

    @Test
    public void testCheckParticipant() {
        assertEquals(false, mEvent.checkIfParticipantIsIn(mUser2));
    }
}


