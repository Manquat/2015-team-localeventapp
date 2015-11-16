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
public class CalendarGrid {
    private static final String TAG = "CalendarGrid";
    private static final int NUMBER_OF_CELLS = 6 * 7; // the minimal size for displaying all the day of a month

    private enum Current {
        CURRENT, NEXT, PREVIOUS
    }

//---------------------------------------------------------------------------------------------
//----Members----------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------

    // Number of the days in the month for the actual calendar display
    private List<Integer> mDays = new ArrayList<>(NUMBER_OF_CELLS);

    // Is the days associate in the current month
    private List<Current> mCurrent = new ArrayList<>(NUMBER_OF_CELLS);
    private int mIndexOfCurrentDay;
    private int mCurrentMonth;
    private int mCurrentYear;

    CalendarGrid(Calendar focusedDay) {
        this(focusedDay.get(Calendar.DAY_OF_MONTH), focusedDay.get(Calendar.MONTH),
                focusedDay.get(Calendar.YEAR));
    }

    CalendarGrid(int day, int month, int year) {
        // initialize the parameters
        for (int i = 0; i < NUMBER_OF_CELLS; i++) {
            mDays.add(0);
            mCurrent.add(Current.CURRENT);
        }

        setFocusedMonth(month, year);

        setFocusedDay(day, month, year);
    }

//---------------------------------------------------------------------------------------------
//----Get--------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------

    public int getCurrentMonth() {
        return mCurrentMonth;
    }

    public int getCurrentYear() {
        return mCurrentYear;
    }

    public int getDay(int position) {
        return mDays.get(position);
    }

    public int getDayOfYear(int position) {
        GregorianCalendar cal;
        switch (mCurrent.get(position)) {
            case CURRENT:
                cal = new GregorianCalendar(mCurrentYear, mCurrentMonth, mDays.get(position));
                break;
            case PREVIOUS:
                cal = new GregorianCalendar(mCurrentYear, mCurrentMonth - 1, mDays.get(position));
                break;
            case NEXT:
                cal = new GregorianCalendar(mCurrentYear, mCurrentMonth + 1, mDays.get(position));
                break;
            default:
                Log.d(TAG, "No such Current Type!");
                throw new AssertionError(Current.values());
        }

        return cal.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Return a tag in the form of CURRENT-DAY-MONTH-YEAR,
     * where CURRENT can have 4 values : CURRENT_MONTH, NEXT_MONTH, PREVIOUS_MONTH and CURRENT_DAY
     *
     * @param position the position of the days in the grid (range 1-42)
     * @return A string containing the tags
     */
    public String getStringTagAt(int position) {
        String tag;

        // adding the CURRENT attribute
        switch (mCurrent.get(position)) {
            case CURRENT:
                tag = "CURRENT_";
                if (position == mIndexOfCurrentDay) {
                    tag += "DAY";
                } else {
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
     *
     * @param position the position of the day in the grid
     * @return the date format as th default local convention
     */
    public String getStringDate(int position) {
        Calendar cal = getDateFromPosition(position);

        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL);

        return dateFormat.format(cal.getTime());
    }

    /**
     * Return a string with the date of the current day
     *
     * @return the date format as th default local convention
     */
    public String getStringDate() {
        return getStringDate(mIndexOfCurrentDay);
    }

    private Calendar getDateFromPosition(int position) {
        int effectiveMonth = mCurrentMonth;

        switch (mCurrent.get(position)) {
            case CURRENT:
                break;
            case NEXT:
                effectiveMonth += 1;
                break;
            case PREVIOUS:
                effectiveMonth -= 1;
                break;
            default:
                Log.d(TAG, "No such Current enum");
                throw new AssertionError(Current.values());
        }

        return new GregorianCalendar(mCurrentYear,
                effectiveMonth, mDays.get(position));
    }

    /**
     * This return the index of the day given in the current month
     * If it's not found return -1
     *
     * @param day the day search in the current month
     * @return
     */
    private int getIndexOfDay(int day) {
        int iFind = -1;
        // finding the index of the current day
        for (int i = 0; i < mDays.size(); i++) {
            if (mCurrent.get(i) == Current.CURRENT) {
                if (mDays.get(i) == day) {
                    iFind = i;
                    break; // end the for loop
                }
            }
        }

        return iFind;
    }

//---------------------------------------------------------------------------------------------
//----Set--------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------

    public void setFocusedMonth(int month, int year) {
        if (month != mCurrentMonth || year != mCurrentYear) {
            Calendar cal = new GregorianCalendar(year, month, 1);

            mCurrentMonth = cal.get(Calendar.MONTH);
            mCurrentYear = cal.get(Calendar.YEAR);

            // backtrack to the beginning of the current week (first week of the month)
            cal.add(Calendar.DAY_OF_YEAR, cal.getFirstDayOfWeek() - cal.get(Calendar.DAY_OF_WEEK));

            // if the first day ot the month is the first day of a week there will be no day of the previous month
            if (cal.get(Calendar.MONTH) == mCurrentMonth) {
                // backtrack of one more week
                cal.add(Calendar.DAY_OF_YEAR, -7);
            }

            for (int i = 0; i < mDays.size(); i++) {
                mDays.set(i, cal.get(Calendar.DAY_OF_MONTH));

                if (cal.get(Calendar.MONTH) != mCurrentMonth) //this is a different month
                {
                    if (i < mDays.size() / 2) // we are at the beginning of the grid so it's necessarily the previous
                    {
                        mCurrent.set(i, Current.PREVIOUS);
                    } else // we are at the end of the grid so it's necessarily the next
                    {
                        mCurrent.set(i, Current.NEXT);
                    }
                } else // this the current month
                {
                    mCurrent.set(i, Current.CURRENT);
                }

                cal.add(Calendar.DAY_OF_YEAR, 1);
            }
        }
    }

    public void setFocusedDay(Calendar focusedDay) {
        setFocusedDay(focusedDay.get(Calendar.DAY_OF_MONTH), focusedDay.get(Calendar.MONTH),
                focusedDay.get(Calendar.YEAR));
    }

    public void setFocusedDay(int day, int month, int year) {
        if (month != mCurrentMonth || year != mCurrentYear) {
            setFocusedMonth(month, year);
        }

        // verify that the day is in the range of the possible day for this month
        mIndexOfCurrentDay = getIndexOfDay(day);

        if (mIndexOfCurrentDay == -1) {
            // the day was out of the scale
            if (day < 1) {
                // fixing it at the first day of the month
                mIndexOfCurrentDay = getIndexOfDay(1);
            } else {
                // create a calendar to now the last day of the current month
                GregorianCalendar cal = new GregorianCalendar(mCurrentYear, mCurrentMonth, 1);
                if (day > cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                    // fixing it at the last day of the current month
                    mIndexOfCurrentDay = getIndexOfDay(cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                } else {
                    throw new AssertionError(day);
                }
            }
        }
    }

    public void setFocusedDay(int position) {
        setFocusedDay(getDateFromPosition(position));
    }

//---------------------------------------------------------------------------------------------
//----Methods----------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------

    public boolean isCurrentMonth(int position) {
        return mCurrent.get(position) == Current.CURRENT;
    }

    public boolean isCurrentDay(int position) {
        return position == mIndexOfCurrentDay;
    }

    public void nextMonth() {
        setFocusedDay(mDays.get(mIndexOfCurrentDay), mCurrentMonth + 1, mCurrentYear);
    }

    public void prevMonth() {
        setFocusedDay(mDays.get(mIndexOfCurrentDay), mCurrentMonth - 1, mCurrentYear);
    }
}
