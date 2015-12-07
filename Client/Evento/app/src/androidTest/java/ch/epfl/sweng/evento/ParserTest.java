package ch.epfl.sweng.evento;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import ch.epfl.sweng.evento.rest_api.Parser;

import static junit.framework.Assert.assertEquals;

/**
 * Created by gautier on 03/12/2015.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ParserTest {

    @Before
    public void init() {

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
