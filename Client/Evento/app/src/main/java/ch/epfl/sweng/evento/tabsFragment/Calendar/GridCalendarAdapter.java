package ch.epfl.sweng.evento.tabsFragment.Calendar;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

import ch.epfl.sweng.evento.R;

/**
 * Created by Gautier on 27/10/2015.
 */
public class GridCalendarAdapter extends BaseAdapter implements View.OnClickListener
{
    private static final String tag = "GridCalendarAdapter";
    private static final int NUMBER_OF_CELLS = 6 * 7;

    private Context mContext;

    private CalendarGrid mCalendarGrid;

    public GridCalendarAdapter(Context context)
    {
        super();
        mContext = context;

        // Initialize the calendar grid at the current date
        mCalendarGrid = new CalendarGrid(new GregorianCalendar(2015, Calendar.DECEMBER, 25));
    }

    @Override
    public int getCount()
    {
        return NUMBER_OF_CELLS;
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Log.d(tag, "getView ...");
        View rootView = convertView;

        if (rootView == null)
        {
            LayoutInflater inflater = (LayoutInflater.from(mContext));
            rootView = inflater.inflate(R.layout.day_cell, parent, false);
        }

        // get a reference of the day button
        Button button = (Button) rootView.findViewById(R.id.cell_button_view);

        if (button == null)
        {
            Log.e(tag, "No button in the view");
            throw new NullPointerException("No button in the view");
        }

        button.setText(String.valueOf(mCalendarGrid.getDay(position)));
        button.setOnClickListener(this);

        // adding the tag to store the date position in the grid inside the button
        button.setTag(String.valueOf(position));

        return rootView;
    }

    @Override
    public void onClick(View v)
    {
        int position = Integer.valueOf((String) v.getTag());

        String day = mCalendarGrid.getStringDate(position);

        Toast.makeText(mContext, "Test " + day, Toast.LENGTH_SHORT).show();

        mCalendarGrid.setFocusedDay(position);
        notifyDataSetChanged();
    }
}
