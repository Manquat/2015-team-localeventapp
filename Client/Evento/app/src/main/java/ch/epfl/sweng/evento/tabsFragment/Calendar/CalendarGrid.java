package ch.epfl.sweng.evento.tabsFragment.Calendar;

import android.util.Log;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Gautier on 28/10/2015.
 */
public class CalendarGrid
{
    private static final String TAG = "CalendarGrid";
    private static final int NUMBER_OF_CELLS = 6 * 7;

    private enum Current
    {
        CURRENT, NEXT, PREVIOUS
    }

    // Number of the days in the month for the actual calendar display
    private List<Integer> mDays = new ArrayList<>(NUMBER_OF_CELLS);

    // Is the days associate in the current month
    private List<Current> mCurrent = new ArrayList<>(NUMBER_OF_CELLS);
    private int mIndexOfCurrentDay;
    private int mCurrentMonth;
    private int mCurrentYear;

    CalendarGrid(Calendar focusedDay)
    {
        this(focusedDay.get(Calendar.DAY_OF_MONTH), focusedDay.get(Calendar.MONTH),
                focusedDay.get(Calendar.YEAR));
    }

    CalendarGrid(int day, int month, int year)
    {
        // initialize the parameters
        for (int i = 0; i < NUMBER_OF_CELLS; i++)
        {
            mDays.add(0);
            mCurrent.add(Current.CURRENT);
        }

        setFocusedMonth(month, year);

        setFocusedDay(day, month, year);
    }

//---------------------------------------------------------------------------------------------
//----Get--------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------

    public int getCurrentMonth()
    {
        return mCurrentMonth;
    }

    public int getCurrentYear()
    {
        return mCurrentYear;
    }

    public int getDay(int position)
    {
        return mDays.get(position);
    }

    /**
     * Return a tag in the form of CURRENT-DAY-MONTH-YEAR,
     * where CURRENT can have 4 values : CURRENT_MONTH, NEXT_MONTH, PREVIOUS_MONTH and CURRENT_DAY
     * @param position the position of the days in the grid (range 1-42)
     * @return A string containing the tags
     */
    public String getStringTagAt(int position)
    {
        String tag;

        // adding the CURRENT attribute
        switch (mCurrent.get(position))
        {
            case CURRENT:
                tag = "CURRENT_";
                if (position == mIndexOfCurrentDay)
                {
                    tag += "DAY";
                }
                else
                {
                    tag += "MONTH";
                }
                break;
            case PREVIOUS:
                tag = "PREVIOUS_MONTH";
                break;
            case NEXT:
                tag = "NEXT_MONTH";
                break;
            default:
                Log.d(TAG, "No such Current Type!");
                throw new AssertionError(Current.values());
        }

        tag += "-";

        // adding the DAY attribute
        tag += String.valueOf(mDays.get(position));
        tag += "-";

        // adding the MONTH attribute
        tag += String.valueOf(mCurrentMonth);
        tag += "-";

        // adding the YEAR attribute
        tag += String.valueOf(mCurrentYear);

        return tag;
    }

    /**
     * Return a string with the date of the day at this position
     * @param position the position of the day in the grid
     * @return the date format as th default local convention
     */
    public String getStringDate(int position)
    {
        Calendar cal = getDateFromPosition(position);

        DateFormat format = DateFormat.getDateInstance(DateFormat.FULL);

        return format.format(cal.getTime());
    }

    private Calendar getDateFromPosition(int position)
    {
        GregorianCalendar cal = new GregorianCalendar(mCurrentYear,
                mCurrentMonth, mDays.get(position));

        switch (mCurrent.get(position))
        {
            case CURRENT:
                break;
            case NEXT:
                cal.add(Calendar.MONTH, 1);
                break;
            case PREVIOUS:
                cal.add(Calendar.MONTH, -1);
                break;
            default:
                Log.d(TAG, "No such Current enum");
                throw new AssertionError(Current.values());
        }

        return cal;
    }

//---------------------------------------------------------------------------------------------
//----Set--------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------

    public void setFocusedMonth(int month, int year)
    {
        if (month != mCurrentMonth || year != mCurrentYear)
        {
            mCurrentMonth = month;
            mCurrentYear = year;

            Calendar cal = new GregorianCalendar(year, month, 1);

            // backtrack to the beginning of the current week (first week of the month)
            cal.add(Calendar.DAY_OF_YEAR, cal.getFirstDayOfWeek() - cal.get(Calendar.DAY_OF_WEEK));

            for (int i = 0; i < mDays.size(); i++)
            {
                mDays.set(i, cal.get(Calendar.DAY_OF_MONTH));
                if (cal.get(Calendar.MONTH) != mCurrentMonth) //this is a different month
                {
                    if (i < mDays.size() / 2) // we are at the beginning of the grid so it's necessarily the previous
                    {
                        mCurrent.set(i, Current.PREVIOUS);
                    }
                    else // we are at the end of the grid so it's necessarily the next
                    {
                        mCurrent.set(i, Current.NEXT);
                    }
                }

                cal.add(Calendar.DAY_OF_YEAR, 1);
            }
        }
    }

    public void setFocusedDay(Calendar focusedDay)
    {
        setFocusedDay(focusedDay.get(Calendar.DAY_OF_MONTH), focusedDay.get(Calendar.MONTH),
                focusedDay.get(Calendar.YEAR));
    }

    public void setFocusedDay(int day, int month, int year)
    {
        if (month != mCurrentMonth || year != mCurrentYear)
        {
            setFocusedMonth(month, year);
        }

        // finding the index of the current day
        for (int i=0; i<mDays.size(); i++)
        {
            if (mCurrent.get(i) == Current.CURRENT)
            {
                if (mDays.get(i) == day)
                {
                    mIndexOfCurrentDay = i;
                    break; // end the for loop
                }
            }
        }
    }

    public void setFocusedDay(int position)
    {
        setFocusedDay(getDateFromPosition(position));
    }
}
