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

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import ch.epfl.sweng.evento.R;

/**
 * Created by Gautier on 27/10/2015.
 */
public class GridCalendarAdapter extends BaseAdapter implements View.OnClickListener {
    private static final String tag = "GridCalendarAdapter";
    private static final int NUMBER_OF_CELLS = 7 * 7; // the line for the day of the week, and 6 lines for all the day of the month

    private Context mContext;

    private CalendarGrid mCalendarGrid;

    public GridCalendarAdapter(Context context) {
        super();
        mContext = context;

        // Initialize the calendar grid at the current date
        mCalendarGrid = new CalendarGrid(new GregorianCalendar(2015, Calendar.DECEMBER, 25));
    }

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

    // create a new ImageView for each item referenced by the Adapter
    @Override
    public View getView(int gridPosition, View convertView, ViewGroup parent) {
        Log.d(tag, "getView ...");

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
            Button button = (Button) rootView.findViewById(R.id.cell_button_view);

            if (button == null) {
                Log.e(tag, "No button in the view");
                throw new NullPointerException("No button in the view");
            }

            button.setText(String.valueOf(mCalendarGrid.getDay(position)));
            button.setOnClickListener(this);

            // adding the tag to store the date position in the grid inside the button
            button.setTag(R.id.position_tag, String.valueOf(position));

            // by default all the text are disable (grey color)
            button.setTextColor(ContextCompat.getColor(mContext, R.color.colorDisableMonth));
            button.setActivated(false);

            if (mCalendarGrid.isCurrentMonth(position)) {
                button.setTextColor(ContextCompat.getColor(mContext, R.color.colorCurrentMonth));
            }

            if (mCalendarGrid.isCurrentDay(position)) {
                button.setActivated(true);
            }

            // highlight the current day by changing it textColor
            GregorianCalendar calendar = new GregorianCalendar();
            if (mCalendarGrid.getDayOfYear(position) == calendar.get(Calendar.DAY_OF_YEAR)
                    && mCalendarGrid.getCurrentYear() == calendar.get(Calendar.YEAR)) {
                button.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
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

    @Override
    public void onClick(View v) {
        int position = Integer.valueOf((String) v.getTag(R.id.position_tag));

        String day = mCalendarGrid.getStringDate(position);

        //Toast.makeText(mContext, "Test " + day, Toast.LENGTH_SHORT).show();

        mCalendarGrid.setFocusedDay(position);
        notifyDataSetChanged();
    }

    public void nextMonth() {
        mCalendarGrid.nextMonth();
        notifyDataSetChanged();
    }

    public void prevMonth() {
        mCalendarGrid.prevMonth();
        notifyDataSetChanged();
    }
}
