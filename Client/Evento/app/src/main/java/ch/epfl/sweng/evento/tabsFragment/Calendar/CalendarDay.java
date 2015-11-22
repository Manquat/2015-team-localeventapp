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
    public static final int[] STATE_HAVE_EVENTS = {R.attr.state_have_events};

//---------------------------------------------------------------------------------------------
//----Members----------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------
    private static final int NUMBER_OF_STATES = 3;

    private boolean mIsCurrentDay = false;
    private boolean mIsCurrentMonth = false;
    private boolean mHaveEvents = false;

//---------------------------------------------------------------------------------------------
//----Constructor------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------

    public CalendarDay(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//---------------------------------------------------------------------------------------------
//----Set--------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------

    public boolean getStateCurrentDay() {
        return mIsCurrentDay;
    }

    public boolean getStateCurrentMonth() {
        return mIsCurrentMonth;
    }

    public boolean getStateHaveEvents() {
        return mHaveEvents;
    }

//---------------------------------------------------------------------------------------------
//----Set--------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------

    public void setStateCurrentDay(boolean isCurrentDay) {
        mIsCurrentDay = isCurrentDay;
        refreshDrawableState();
    }

    public void setStateCurrentMonth(boolean isCurrentMonth) {
        mIsCurrentMonth = isCurrentMonth;
        refreshDrawableState();
    }

    public void setStateHaveEvents(boolean haveEvents) {
        mHaveEvents = haveEvents;
        refreshDrawableState();
    }

//---------------------------------------------------------------------------------------------
//----Callback---------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + NUMBER_OF_STATES);

        if (mIsCurrentDay) {
            mergeDrawableStates(drawableState, STATE_CURRENT_DAY);
        }
        if (mIsCurrentMonth) {
            mergeDrawableStates(drawableState, STATE_CURRENT_MONTH);
        }
        if (mHaveEvents) {
            mergeDrawableStates(drawableState, STATE_HAVE_EVENTS);
        }

        return drawableState;
    }
}
