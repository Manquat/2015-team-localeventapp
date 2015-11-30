package ch.epfl.sweng.evento.tabsFragment.Calendar;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import ch.epfl.sweng.evento.EventDatabase;
import ch.epfl.sweng.evento.Events.Event;
import ch.epfl.sweng.evento.InfinitePagerAdapter.GridInfinitePageAdapter;
import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.tabsFragment.Refreshable;


/**
 * Adapter for the GridView that display the CalendarCells
 */
public class GridCalendarAdapter extends BaseAdapter implements View.OnClickListener {
    private static final String TAG = "GridCalendarAdapter";
    private static final int NUMBER_OF_CELLS = 7 * 7; // the line for the day of the week, and 6 lines for all the day of the month


    private Context mContext;               // the context where the adapter is used
    private CalendarGrid mCalendarGrid;     // the container of all the information
    private List<Event> mEvents;            // the events at the current day
    private Refreshable mUpdatableParent;   // the parent that holds the grid and will be update when something
    // changed in the adapter.


    /**
     * Constructor
     *
     * @param context         the context where the adapter is used
     * @param updatableParent the parent that holds the grid and will be update when something
     *                        changed in the adapter.
     */
    public GridCalendarAdapter(Context context, Refreshable updatableParent) {
        this(context, updatableParent, new GregorianCalendar());
    }

    public GridCalendarAdapter(Context context, Refreshable updatableParent, Calendar focusedDate) {
        super();
        mContext = context;
        mUpdatableParent = updatableParent;

        // Initialize the calendar grid at the given date
        mCalendarGrid = new CalendarGrid(focusedDate);
        mEvents = EventDatabase.INSTANCE.filter(focusedDate).toArrayList();
    }


    /**
     * Getting the number of cells in the grid view
     *
     * @return the number of cells including the first row that display the days of the week
     */
    @Override
    public int getCount() {
        return NUMBER_OF_CELLS;
    }

    @Override
    public Object getItem(int position) {
        if (position < 7) {
            GregorianCalendar calendar = new GregorianCalendar();

            // backtrack to the beginning of the current week
            calendar.add(Calendar.DAY_OF_YEAR, calendar.getFirstDayOfWeek()
                    - calendar.get(Calendar.DAY_OF_WEEK));

            // go to the corresponding day of the week for this column
            calendar.add(Calendar.DAY_OF_YEAR, position);

            return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT,
                    Locale.getDefault());
        } else {
            position = position - 7;
            return String.valueOf(mCalendarGrid.getDay(position));
        }
    }

    /**
     * Get the unique id of the day at this position in the grid view
     *
     * @param position position in the grid view
     * @return if it's a day return the date in millisecond, otherwise return 0
     */
    @Override
    public long getItemId(int position) {
        if (position > 7) {
            Calendar calendar = mCalendarGrid.getDateFromPosition(position - 7);
            return calendar.getTimeInMillis();
        }

        return 0;
    }

    /**
     * Create a new ImageView for each item referenced by the Adapter
     *
     * @param gridPosition Position in the grid, counting from the first cell on the top left
     * @param convertView
     * @param parent
     * @return An ImageView for this day
     */
    @Override
    public View getView(int gridPosition, View convertView, ViewGroup parent) {

        View rootView = convertView;

        if (gridPosition < 7) {
            if (rootView == null) {
                GregorianCalendar calendar = new GregorianCalendar();

                // backtrack to the beginning of the current week
                calendar.add(Calendar.DAY_OF_YEAR, calendar.getFirstDayOfWeek()
                        - calendar.get(Calendar.DAY_OF_WEEK));

                // go to the corresponding day of the week for this column
                calendar.add(Calendar.DAY_OF_YEAR, gridPosition);

                TextView textView = new TextView(mContext);
                textView.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT,
                        Locale.getDefault()));
                textView.setGravity(Gravity.CENTER);

                rootView = textView;
            }
        } else {
            int position = gridPosition - 7; // remove the first row of the calendar

            if (rootView == null) {
                LayoutInflater inflater = (LayoutInflater.from(mContext));
                rootView = inflater.inflate(R.layout.day_cell, parent, false);
            }

            // get a reference of the day button
            CalendarDay day = (CalendarDay) rootView.findViewById(R.id.cell_button_view);

            if (day == null) {
                Log.e(TAG, "No button in the view");
                throw new NullPointerException("No button in the view");
            }

            day.setText(String.valueOf(mCalendarGrid.getDay(position)));
            day.setOnClickListener(this);

            // adding the TAG to store the date position in the grid inside the button
            day.setTag(R.id.position_tag, String.valueOf(position));

            // by default all the text are disable (grey color)
            day.setTextColor(ContextCompat.getColor(mContext, R.color.colorDisableMonth));
            day.setStateCurrentDay(false);
            day.setStateCurrentMonth(false);
            day.setStateHaveEvents(false);

            if (mCalendarGrid.isCurrentMonth(position)) {
                day.setStateCurrentMonth(true);
                day.setTextColor(ContextCompat.getColor(mContext, R.color.defaultTextColor));
            }

            if (mCalendarGrid.isCurrentDay(position)) {
                day.setStateCurrentDay(true);
            }

            // highlight the actual day by changing the textColor
            GregorianCalendar calendar = new GregorianCalendar();
            if (mCalendarGrid.getDayOfYear(position) == calendar.get(Calendar.DAY_OF_YEAR) &&
                    mCalendarGrid.getCurrentMonth() == calendar.get(Calendar.MONTH) &&
                    mCalendarGrid.getCurrentYear() == calendar.get(Calendar.YEAR)) {
                day.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
            }

            List<Event> events = EventDatabase.INSTANCE.filterOnDay(mCalendarGrid.getDateFromPosition(position)).toArrayList();

            if (events.size() != 0) {
                day.setStateHaveEvents(true);

                if (day.getStateCurrentDay()) {
                    mEvents = events;
                    mUpdatableParent.refresh();
                }
            }
        }

        return rootView;
    }

    /**
     * Return a string with the date of the current day focused
     *
     * @return the date format as th default local convention
     */
    public String getStringDate() {
        return mCalendarGrid.getStringDate();
    }

    /**
     * Return the events at the current selected day
     *
     * @return Collection of event order as ID
     */
    public List<Event> getCurrentEvents() {
        return mEvents;
    }

    public void setFocusedDate(Calendar focusedDate) {
        mCalendarGrid.setFocusedDay(focusedDate);
        notifyDataSetChanged();
        mUpdatableParent.refresh();
    }



    @Override
    public void onClick(View v) {
        int position = Integer.valueOf((String) v.getTag(R.id.position_tag));

        mCalendarGrid.setFocusedDay(position);
        mEvents = Collections.EMPTY_LIST;
        ((GridInfinitePageAdapter) mUpdatableParent).setFocusedDate(mCalendarGrid.getFocusedDate());
        notifyDataSetChanged();
        mUpdatableParent.refresh();
    }


    public void nextMonth() {
        mCalendarGrid.nextMonth();
        mEvents = null;
        notifyDataSetChanged();
    }

    public void prevMonth() {
        mCalendarGrid.prevMonth();
        mEvents = null;
        notifyDataSetChanged();
    }
}
