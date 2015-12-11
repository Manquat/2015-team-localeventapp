package ch.epfl.sweng.evento.calendar;

import android.util.Log;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Class that's holds the data for the grid view use in the GridCalendarAdapter
 */
public class CalendarGrid {
    private static final String TAG = "CalendarGrid";
    private static final int NUMBER_OF_CELLS = 6 * 7; // the minimal size for displaying all the day of a month
    // Number of the days in the month for the actual calendar display
    private List<Integer> mDays = new ArrayList<>(NUMBER_OF_CELLS);
    // Is the days associate in the current month
    private List<Current> mCurrent = new ArrayList<>(NUMBER_OF_CELLS);
    private int mIndexOfCurrentDay; // the index of the current day in the list of mDays and mCurrent
    private int mCurrentMonth;      // the current month (0 for january, ...)
    private int mCurrentYear;       // the current year

    private enum Current {
        CURRENT, NEXT, PREVIOUS
    }

    /**
     * Create a grid focused on the day given by the actual position of the calendar
     *
     * @param focusedDay a Calendar at the wanted date, he will not be modified
     */
    public CalendarGrid(Calendar focusedDay) {
        this(focusedDay.get(Calendar.DAY_OF_MONTH), focusedDay.get(Calendar.MONTH),
                focusedDay.get(Calendar.YEAR));
    }


    /**
     * Create a grid focused on the given day
     *
     * @param day   the day wanted, if it doesn't exist in the month resolve as the Calendar class do :
     *              the 32th january is transform in 1th february
     * @param month the month wanted
     * @param year  the year wanted
     */
    public CalendarGrid(int day, int month, int year) {
        // initialize the parameters
        for (int i = 0; i < NUMBER_OF_CELLS; i++) {
            mDays.add(0);
            mCurrent.add(Current.CURRENT);
        }

        setFocusedDay(day, month, year);
    }

    /**
     * Getter of the current focused day
     *
     * @return a Calendar at the current focused day
     */
    public Calendar getFocusedDate() {
        return new GregorianCalendar(mCurrentYear, mCurrentMonth, mDays.get(mIndexOfCurrentDay));
    }

    /**
     * Getter of the current month
     *
     * @return the int corresponding of the current month : 0 for january, ..., 11 for december
     */
    public int getCurrentMonth() {
        return mCurrentMonth;
    }

    /**
     * Getter for the current year
     *
     * @return the year as an int, like in a the gregorian calendar.
     */
    public int getCurrentYear() {
        return mCurrentYear;
    }

    /**
     * Getter of the day of the month in function of the position in the grid
     *
     * @param position the position in the grid [0, 41]
     * @return the number of the day in the month
     */
    public int getDay(int position) {
        return mDays.get(position);
    }

    /**
     * Getter of the day as the number since the begging of the year in function of the position
     *
     * @param position the position in the grid [0, 41]
     * @return the number of the day since the begging of the year
     */
    public int getDayOfYear(int position) {
        int effectiveMonth = mCurrentMonth;
        switch (mCurrent.get(position)) {
            case CURRENT:
                break;
            case PREVIOUS:
                ++effectiveMonth;
                break;
            case NEXT:
                --effectiveMonth;
                break;
            default:
                Log.e(TAG, "No such Current Type!");
                throw new AssertionError(Current.values());
        }

        GregorianCalendar calendar = new GregorianCalendar(mCurrentYear, effectiveMonth, mDays.get(position));
        return calendar.get(Calendar.DAY_OF_YEAR);
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

    /**
     * Give the date of the day at the position given as a Calendar
     *
     * @param position the position of the day in the grid
     * @return a calendar initialize at the date of the day pointed
     */
    public GregorianCalendar getDateFromPosition(int position) {
        int effectiveMonth = mCurrentMonth;

        switch (mCurrent.get(position)) {
            case CURRENT:
                break;
            case NEXT:
                ++effectiveMonth;
                break;
            case PREVIOUS:
                --effectiveMonth;
                break;
            default:
                Log.e(TAG, "No such Current enum");
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
     * @return the index in the actual grid
     */
    private int getIndexOfDay(int day) {
        int iFind = -1;
        // finding the index of the current day
        for (int i = 0; i < mDays.size(); i++) {
            //avoiding the day that are not in the current month
            if (mCurrent.get(i) == Current.CURRENT) {
                if (mDays.get(i) == day) {
                    iFind = i;
                    break; // end the for loop
                }
            }
        }

        return iFind;
    }

    /**
     * Set the current month display
     *
     * @param month the month wanted as an int given by the Calendar class
     * @param year  the year of the month wanted
     */
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

    /**
     * Set the day selected
     *
     * @param focusedDay a calendar at the wanted date
     */
    public void setFocusedDay(Calendar focusedDay) {
        setFocusedDay(focusedDay.get(Calendar.DAY_OF_MONTH), focusedDay.get(Calendar.MONTH),
                focusedDay.get(Calendar.YEAR));
    }

    /**
     * Set the day selected
     *
     * @param day   day of the month wanted
     * @param month month of the year wanted (0 for january,...)
     * @param year  year wanted
     */
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

    /**
     * Set the day selected
     *
     * @param position the position in the grid of the day wanted
     */
    public void setFocusedDay(int position) {
        setFocusedDay(getDateFromPosition(position));
    }

    /**
     * Is the position pointed a day in the current month or not
     *
     * @param position position in the grid
     * @return true if yes false otherwise
     */
    public boolean isCurrentMonth(int position) {
        return mCurrent.get(position) == Current.CURRENT;
    }

    /**
     * Is the position pointed the current day (current according to the internal timer of the phone)
     *
     * @param position the position in the grid
     * @return true if yes false otherwise
     */
    public boolean isCurrentDay(int position) {
        return position == mIndexOfCurrentDay;
    }

    /**
     * Change the displayed month to the next one
     */
    public void nextMonth() {
        setFocusedDay(mDays.get(mIndexOfCurrentDay), mCurrentMonth + 1, mCurrentYear);
    }

    /**
     * Change the displayed month to the previous one
     */
    public void prevMonth() {
        setFocusedDay(mDays.get(mIndexOfCurrentDay), mCurrentMonth - 1, mCurrentYear);
    }
}
