package ch.epfl.sweng.evento;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.GregorianCalendar;

import ch.epfl.sweng.evento.event.Signature;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertTrue;

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
    public void equalsTest() {
        assertTrue(mSignature.equals(mSignature));

        Signature signature = new Signature(mID, mCalendar);
        assertTrue(mSignature.equals(signature));
        assertTrue(signature.equals(mSignature));

        Signature signature1 = new Signature(mID + 1, mCalendar);
        assertFalse(signature1.equals(mSignature));
        assertFalse(mSignature.equals(signature1));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mCalendar.getTime());
        calendar.add(Calendar.MONTH, 1);

        Signature signature2 = new Signature(mID, calendar);
        assertFalse(mSignature.equals(signature2));
        assertFalse(signature2.equals(mSignature));
    }

    @Test
    public void hashCodeTest() {
        Signature signature = new Signature(mID, mCalendar);
        assertEquals(mSignature.hashCode(), signature.hashCode());

        signature = new Signature(mID + 1, mCalendar);
        assertNotSame(mSignature.hashCode(), signature.hashCode());

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(mCalendar.getTime());
        calendar.add(Calendar.MONTH, 1);
        signature = new Signature(mID, calendar);
        assertNotSame(mSignature.hashCode(), signature.hashCode());
    }

    @Test
    public void compareCalendarTest() {
        Calendar calendar = (Calendar) mCalendar.clone();
        calendar.add(Calendar.MONTH, 1);

        Signature signature2 = new Signature(mID, calendar);

        assertEquals(-1, mSignature.compareTo(signature2));

        calendar.add(Calendar.MONTH, -2);
        signature2 = new Signature(mID, calendar);

        assertEquals(1, mSignature.compareTo(signature2));
    }

    @Test
    public void compareIDTest() {
        int id = mID + 1;
        Signature signature2 = new Signature(id, mCalendar);

        assertEquals(-1, mSignature.compareTo(signature2));

        id = id - 10;
        signature2 = new Signature(id, mCalendar);

        assertEquals(1, mSignature.compareTo(signature2));
    }
}
