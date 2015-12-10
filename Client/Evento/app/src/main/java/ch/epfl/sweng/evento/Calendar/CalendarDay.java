package ch.epfl.sweng.evento.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import ch.epfl.sweng.evento.R;

/**
 * Extension of the base button class to add some custom features, new stats that are specify for
 * the day of the calendar : is the day the current day, is it in the displayed month, is there
 * some event at that date
 */
public class CalendarDay extends Button
{
    public static final int[] STATE_CURRENT_DAY = {R.attr.state_current_day};
    public static final int[] STATE_CURRENT_MONTH = {R.attr.state_current_month};
    public static final int[] STATE_HAVE_EVENTS = {R.attr.state_have_events};

    private static final int NUMBER_OF_STATES = 3;

    private boolean mIsCurrentDay = false;      // is it the current day
    private boolean mIsCurrentMonth = false;    // is it a day of the current month
    private boolean mHaveEvents = false;        // have this day some events


    /**
     * Constructor that just call the one of button
     *
     * @param context context where the button is used
     * @param attrs   attributes of the button
     */
    public CalendarDay(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }


    /**
     * Getter for the CurrentDay state
     *
     * @return the state of the CurrentDay state
     */
    public boolean getStateCurrentDay()
    {
        return mIsCurrentDay;
    }

    /**
     * Setter for the CurrentDay state
     *
     * @param isCurrentDay the state wanted for the CurrentDay state
     */
    public void setStateCurrentDay(boolean isCurrentDay)
    {
        mIsCurrentDay = isCurrentDay;
        refreshDrawableState();
    }

    /**
     * Getter for the CurrentMonth state
     *
     * @return the state of the CurrentMonth state
     */
    public boolean getStateCurrentMonth()
    {
        return mIsCurrentMonth;
    }

    /**
     * Setter for the CurrentMonth state
     *
     * @param isCurrentMonth the state wanted for the CurrentMonth state
     */
    public void setStateCurrentMonth(boolean isCurrentMonth)
    {
        mIsCurrentMonth = isCurrentMonth;
        refreshDrawableState();
    }

    /**
     * Getter for the HaveEvents state
     *
     * @return the state of the HaveEvents state
     */
    public boolean getStateHaveEvents()
    {
        return mHaveEvents;
    }

    /**
     * Setter for the HaveEvents state
     *
     * @param haveEvents the state wanted for the HaveEvents state
     */
    public void setStateHaveEvents(boolean haveEvents)
    {
        mHaveEvents = haveEvents;
        refreshDrawableState();
    }


    /**
     * Generate the new Drawable state for this view. This is called by the view system when the
     * cached Drawable state is determined to be invalid. To retrieve the current state, you should use getDrawableState().
     * @param extraSpace if non-zero, this is the number of extra entries you would like in the
     *                   returned array in which you can place your own states.
     * @return Returns an array holding the current Drawable state of the view.
     */
    @Override
    protected int[] onCreateDrawableState(int extraSpace)
    {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + NUMBER_OF_STATES);

        if (mIsCurrentDay)
        {
            mergeDrawableStates(drawableState, STATE_CURRENT_DAY);
        }
        if (mIsCurrentMonth)
        {
            mergeDrawableStates(drawableState, STATE_CURRENT_MONTH);
        }
        if (mHaveEvents)
        {
            mergeDrawableStates(drawableState, STATE_HAVE_EVENTS);
        }

        return drawableState;
    }
}
