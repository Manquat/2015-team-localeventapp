package ch.epfl.sweng.evento.tabsFragment.Calendar;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import ch.epfl.sweng.evento.R;

/**
 * Adapter for the GridView that display the CalendarCells
 */
public class GridCalendarAdapter extends BaseAdapter implements View.OnClickListener {
    private static final String TAG = "GridCalendarAdapter";
    private static final int NUMBER_OF_CELLS = 7 * 7; // the line for the day of the week, and 6 lines for all the day of the month

//---------------------------------------------------------------------------------------------
//----Members----------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------

    private Context mContext;
    private CalendarGrid mCalendarGrid;

//---------------------------------------------------------------------------------------------
//----Constructor------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------

    public GridCalendarAdapter(Context context) {
        super();
        mContext = context;

        // Initialize the calendar grid at the current date
        mCalendarGrid = new CalendarGrid(new GregorianCalendar());
    }

//---------------------------------------------------------------------------------------------
//----Get--------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------

    @Override
    public int getCount() {
        return NUMBER_OF_CELLS;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
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

            if (mCalendarGrid.isCurrentMonth(position)) {
                day.setStateCurrentMonth(true);
            }

            if (mCalendarGrid.isCurrentDay(position)) {
                day.setStateCurrentDay(true);
            }

            // highlight the current day by changing it textColor
            GregorianCalendar calendar = new GregorianCalendar();
            if (mCalendarGrid.getDayOfYear(position) == calendar.get(Calendar.DAY_OF_YEAR)
                    && mCalendarGrid.getCurrentYear() == calendar.get(Calendar.YEAR)) {
                day.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
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

//---------------------------------------------------------------------------------------------
//----Callbacks-------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------

    @Override
    public void onClick(View v) {
        int position = Integer.valueOf((String) v.getTag(R.id.position_tag));

        mCalendarGrid.setFocusedDay(position);
        notifyDataSetChanged();
    }

//---------------------------------------------------------------------------------------------
//----Methods----------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------

    public void nextMonth() {
        mCalendarGrid.nextMonth();
        notifyDataSetChanged();
    }

    public void prevMonth() {
        mCalendarGrid.prevMonth();
        notifyDataSetChanged();
    }
}
