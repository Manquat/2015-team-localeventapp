package ch.epfl.sweng.evento;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ActionMenuView;
import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static junit.framework.Assert.assertEquals;

import ch.epfl.sweng.evento.Events.Event;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by joachimmuth on 23.11.15.
 */

/** Tests the GUI against the real server */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class GuiServerTest {
    private String TAG = "LoadAndPostTest";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);


    /**
     * Test if event are correctly posted and getted from the GUI.
     * Check the number of events, create a new one, refresh, check if there is a new event
     * @throws InterruptedException
     */
    //@Ignore("Waiting for jenkins solution regarding google play")
    @Test
    public void testRefreshPostRefresh() throws InterruptedException {
        int numOfEvent;

        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
        onView(withText("Refresh")).perform(click());

        // save the number of events
        numOfEvent = EventDatabase.INSTANCE.getSize();

        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
        onView(withText("Create an event")).perform(click());

        Thread.sleep(1000);
        Espresso.pressBack();

        Thread.sleep(1000);
        //pressBack(); // to hide the keyboard
        onView(withId(R.id.submitEvent)).perform(click());

        Thread.sleep(1000); // wait for event to be sent

        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
        onView(withText("Refresh")).perform(click());

        Thread.sleep(1000); // wait for the app to be refreshed

        assertEquals("After creating one event, we get one more event refreshing the app",
                numOfEvent+1, EventDatabase.INSTANCE.getSize());


    }




}