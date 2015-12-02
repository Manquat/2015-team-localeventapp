package ch.epfl.sweng.evento;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import ch.epfl.sweng.evento.calendar.CalendarGrid;

import static java.util.Locale.setDefault;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

/**
 * Test the CalendarGrid class (white box approach)
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class CalendarGridTest {
    private CalendarGrid mGrid;
    private GregorianCalendar mCalendar;

    @Before
    public void setUp() {
        setDefault(Locale.FRANCE);
        mCalendar = new GregorianCalendar(2015, Calendar.JUNE, 10);

        mGrid = new CalendarGrid(mCalendar);
    }

    @Test
    public void creationTest() {
        GregorianCalendar actualDate = new GregorianCalendar();

        CalendarGrid grid1 = new CalendarGrid(actualDate);
        CalendarGrid grid2 = new CalendarGrid(actualDate.get(Calendar.DAY_OF_MONTH),
                actualDate.get(Calendar.MONTH), actualDate.get(Calendar.YEAR));

        assertEquals(actualDate.get(Calendar.DAY_OF_MONTH), grid1.getFocusedDate().get(Calendar.DAY_OF_MONTH));
        assertEquals(actualDate.get(Calendar.MONTH), grid1.getCurrentMonth());
        assertEquals(actualDate.get(Calendar.MONTH), grid2.getCurrentMonth());
        assertEquals(actualDate.get(Calendar.YEAR), grid1.getCurrentYear());
        assertEquals(actualDate.get(Calendar.YEAR), grid2.getCurrentYear());
    }

    @Test
    public void gridDispositionTest() {

        assertEquals(25, mGrid.getDay(0));
        assertEquals(1, mGrid.getDay(7));
        assertEquals(5, mGrid.getDay(41));

        assertFalse(mGrid.isCurrentMonth(0));
        assertTrue(mGrid.isCurrentMonth(10));
        assertFalse(mGrid.isCurrentMonth(41));

        assertTrue(mGrid.isCurrentDay(16));
        assertFalse(mGrid.isCurrentDay(20));
    }

    @Test
    public void getDayException() {
        try {
            mGrid.getDay(42);
            fail();
        } catch (IndexOutOfBoundsException e) {
            //pass the test;
        }

        try {
            mGrid.getDay(-1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            //pass the test
        }
    }

    @Test
    public void isCurrentMonthException() {
        try {
            mGrid.isCurrentMonth(-1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            //pass the test
        }

        try {
            mGrid.isCurrentMonth(42);
            fail();
        } catch (IndexOutOfBoundsException e) {
            //pass the test
        }
    }

    @Test
    public void setFocusedDayException() {
        try {
            mGrid.setFocusedDay(-1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            //pass the test
        }

        try {
            mGrid.setFocusedDay(42);
            fail();
        } catch (IndexOutOfBoundsException e) {
            //pass the test
        }
    }

    @Test
    public void setFocusedDayTest() {
        mGrid.setFocusedDay(10);

        assertEquals(4, mGrid.getFocusedDate().get(Calendar.DAY_OF_MONTH));
        assertEquals(mCalendar.get(Calendar.MONTH), mGrid.getCurrentMonth());
        assertEquals(mCalendar.get(Calendar.YEAR), mGrid.getCurrentYear());

        mGrid.setFocusedDay(mCalendar);

        assertEquals(mCalendar.get(Calendar.DAY_OF_MONTH), mGrid.getFocusedDate().get(Calendar.DAY_OF_MONTH));
        assertEquals(mCalendar.get(Calendar.MONTH), mGrid.getCurrentMonth());
        assertEquals(mCalendar.get(Calendar.YEAR), mGrid.getCurrentYear());

        mGrid.setFocusedDay(32, Calendar.DECEMBER, mCalendar.get(Calendar.YEAR));

        assertEquals(Calendar.DECEMBER, mGrid.getCurrentMonth());
        assertEquals(mCalendar.get(Calendar.YEAR), mGrid.getCurrentYear());
        assertEquals(31, mGrid.getFocusedDate().get(Calendar.DAY_OF_MONTH));
    }
}
