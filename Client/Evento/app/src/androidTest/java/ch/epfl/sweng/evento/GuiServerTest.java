package ch.epfl.sweng.evento;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.widget.DatePicker;

import org.hamcrest.Matcher;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.evento.gui.MainActivity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by joachimmuth on 23.11.15.
 */

/**
 * Tests the GUI against the real server
 */

//@Ignore("Jenkins")
@RunWith(AndroidJUnit4.class)
@LargeTest
public class GuiServerTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private String TAG = "GuiServerTest";

    @Rule
    public ActivityTestRule<MainActivity> startActivity() {
        Settings.setUser(new User(15, "mockName", "mockEmail"));
        Settings.setIdToken("");
        return new ActivityTestRule<>(MainActivity.class);
    }

    public GuiServerTest() {
        super(MainActivity.class);
    }


    /**
     * Test if event are correctly posted and getted from the GUI.
     * Check the number of events, create a new one, refresh, check if there is a new event
     *
     * @throws InterruptedException
     */

    @Ignore("jenkins")
    @Test
    public void testRefreshPostRefresh() throws InterruptedException {
        int numOfEvent;

        //openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withId(R.id.action_refresh)).perform(click());

        // save the number of events
        numOfEvent = EventDatabase.INSTANCE.getSize();

        //openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withId(R.id.action_createAnEvent)).perform(click());

        // wait for the keyboard to be up
        Thread.sleep(1000);
        // close it
        Espresso.pressBack();

        Thread.sleep(1000);
        //pressBack(); // to hide the keyboard
        onView(withText("Validate")).perform(click());

        Thread.sleep(5000); // wait for event to be sent

        //openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withId(R.id.action_refresh)).perform(click());

        Thread.sleep(1000); // wait for the app to be refreshed

        assertEquals("After creating one event, we get one more event refreshing the app",
                numOfEvent + 1, EventDatabase.INSTANCE.getSize());

    }

}