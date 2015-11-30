package ch.epfl.sweng.evento;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.evento.gui.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;

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
    @Ignore("Waiting for jenkins solution regarding google play")
    @Test
    public void testRefreshPostRefresh() throws InterruptedException {
        int numOfEvent;

        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText("Refresh")).perform(click());

        // save the number of events
        numOfEvent = EventDatabase.INSTANCE.getSize();

        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText("Create an event")).perform(click());

        Thread.sleep(1000);
        Espresso.pressBack();

        Thread.sleep(1000);
        //pressBack(); // to hide the keyboard
        onView(withId(R.id.submitEvent)).perform(click());

        Thread.sleep(1000); // wait for event to be sent

        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText("Refresh")).perform(click());

        Thread.sleep(1000); // wait for the app to be refreshed

        assertEquals("After creating one event, we get one more event refreshing the app",
                numOfEvent+1, EventDatabase.INSTANCE.getSize());


    }




}