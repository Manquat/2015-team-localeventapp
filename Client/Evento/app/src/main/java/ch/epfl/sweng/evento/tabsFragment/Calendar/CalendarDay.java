package ch.epfl.sweng.evento.tabsFragment.Calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import ch.epfl.sweng.evento.R;

/**
 * Extension of the base button class to add some custom features : new stats that are specify for
 * the day of the calendar
 */
public class CalendarDay extends Button {
    public static final int[] STATE_CURRENT_DAY = {R.attr.state_current_day};
    public static final int[] STATE_CURRENT_MONTH = {R.attr.state_current_month};
    public static final int[] NUMBER_OF_EVENTS = {R.attr.number_of_events};

    private static final int NUMBER_OF_STATES = 3;

    private boolean mIsCurrentDay = false;      // is it the current day
    private boolean mIsCurrentMonth = false;    // is it a day of the current month
    private int mNumberOfEvents = 0;        // have this day some events


    /**
     * Constructor that just call the one of button
     *
     * @param context context where the button is used
     * @param attrs   attributes of the button
     */
    public CalendarDay(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    /**
     * Getter for the CurrentDay state
     *
     * @return the state of the CurrentDay state
     */
    public boolean getStateCurrentDay() {
        return mIsCurrentDay;
    }

    /**
     * Getter for the CurrentMonth state
     *
     * @return the state of the CurrentMonth state
     */
    public boolean getStateCurrentMonth() {
        return mIsCurrentMonth;
    }

    /**
     * Getter for the HaveEvents state
     *
     * @return the state of the HaveEvents state
     */
    public int getNumberOfEvents() {
        return mNumberOfEvents;
    }


    /**
     * Setter for the CurrentDay state
     *
     * @param isCurrentDay the state wanted for the CurrentDay state
     */
    public void setStateCurrentDay(boolean isCurrentDay) {
        mIsCurrentDay = isCurrentDay;
        refreshDrawableState();
    }

    /**
     * Setter for the CurrentMonth state
     *
     * @param isCurrentMonth the state wanted for the CurrentMonth state
     */
    public void setStateCurrentMonth(boolean isCurrentMonth) {
        mIsCurrentMonth = isCurrentMonth;
        refreshDrawableState();
    }

    /**
     * Setter for the number of Events state
     *
     * @param numberOfEvents the state wanted for the numberOfEvents state
     */
    public void setNumberOfEvents(int numberOfEvents) {
        mNumberOfEvents = numberOfEvents;
        refreshDrawableState();
    }


    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + NUMBER_OF_STATES);

        if (mIsCurrentDay) {
            mergeDrawableStates(drawableState, STATE_CURRENT_DAY);
        }
        if (mIsCurrentMonth) {
            mergeDrawableStates(drawableState, STATE_CURRENT_MONTH);
        }
        if (mNumberOfEvents != 0) {
            mergeDrawableStates(drawableState, NUMBER_OF_EVENTS);
        }

        return drawableState;
    }
}
