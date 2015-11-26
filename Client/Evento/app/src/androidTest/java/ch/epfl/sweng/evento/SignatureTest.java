package ch.epfl.sweng.evento;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.GregorianCalendar;

import ch.epfl.sweng.evento.Events.Signature;

import static junit.framework.Assert.assertEquals;

/**
 * Testing the Signature class
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class SignatureTest {
    int mID;
    Calendar mCalendar;
    Signature mSignature;

    @Before
    public void setUp() {
        mID = 1010101;
        mCalendar = new GregorianCalendar();

        mSignature = new Signature(mID, mCalendar);
    }

    @Test
    public void GetTest() {
        assertEquals(mID, mSignature.getID());
        assertEquals(mCalendar, mSignature.getCalendar());
    }

    @Test
    public void CalendarDeepCopyTest() {
        Calendar calendar = mSignature.getCalendar();
        calendar.add(Calendar.YEAR, 1);

        assertEquals(mCalendar, mSignature.getCalendar());
    }

    @Test
    public void CompareCalendarTest() {
        Calendar calendar = mCalendar;
        calendar.add(Calendar.MONTH, 1);
        Signature signature2 = new Signature(mID, calendar);

        assertEquals(1, mSignature.compare(mSignature, signature2));

        calendar.add(Calendar.MONTH, -2);
        signature2 = new Signature(mID, calendar);

        assertEquals(-1, mSignature.compare(mSignature, signature2));
    }

    @Test
    public void CompareIDTest() {
        int id = mID +1;
        Signature signature2 = new Signature(id, mCalendar);

        assertEquals(1, mSignature.compare(mSignature, signature2));

        id = id -10;
        signature2 = new Signature(id, mCalendar);

        assertEquals(-1, mSignature.compare(mSignature, signature2));
    }
}
