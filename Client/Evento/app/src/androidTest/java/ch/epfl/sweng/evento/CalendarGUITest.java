package ch.epfl.sweng.evento;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import ch.epfl.sweng.evento.gui.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Test the calendar tab at the UI level (black box approach)
 */

@RunWith(AndroidJUnit4.class)
public class CalendarGUITest {
    private GregorianCalendar mActualDate;
    private DateFormat mDateFormat;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        onView(isRoot()).perform(swipeLeft());
        mActualDate = new GregorianCalendar();
        mDateFormat = DateFormat.getDateInstance(DateFormat.FULL);
    }

    @Test
    public void beginFocusedOnTheActualDay() {

        onView(withId(R.id.dayTitle)).check(matches(withText(mDateFormat.format(mActualDate.getTime()))));
    }

    @Test
    public void nextPrevMonthButton() {
        onView(withId(R.id.nextButton)).perform(click());

        mActualDate.add(Calendar.MONTH, 1);
        onView(withId(R.id.dayTitle)).check(matches(withText(mDateFormat.format(mActualDate.getTime()))));

        onView(withId(R.id.prevButton)).perform(click());

        mActualDate.add(Calendar.MONTH, -1);
        onView(withId(R.id.dayTitle)).check(matches(withText(mDateFormat.format(mActualDate.getTime()))));
    }
}