package ch.epfl.sweng.evento.tabsFragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import ch.epfl.sweng.evento.Events.Event;
import ch.epfl.sweng.evento.Events.EventListViewAdapter;
import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.tabsFragment.Calendar.GridCalendarAdapter;

/**
 * The fragment that holds the calendar and the listView that display the events at the current
 * selected date
 */
public class CalendarTabs extends Fragment implements
        Button.OnClickListener,
        Updatable,
        DatePickerDialog.OnDateSetListener {
//---------------------------------------------------------------------------------------------
//----Members----------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------

    private GridCalendarAdapter  mGridCalendarAdapter;
    private TextView             mCurrentDate;
    private LinearLayout         mBaseLayout;
    private EventListViewAdapter mEventListAdapter;
    private DatePickerDialog     mDatePicker;

//---------------------------------------------------------------------------------------------
//----Callbacks--------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        GridView gridView = (GridView) view.findViewById(R.id.gridView);
        mGridCalendarAdapter = new GridCalendarAdapter(view.getContext(), this);
        gridView.setAdapter(mGridCalendarAdapter);

        Button nextButton = (Button) view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);

        Button prevButton = (Button) view.findViewById(R.id.prevButton);
        prevButton.setOnClickListener(this);

        mCurrentDate = (TextView) view.findViewById(R.id.dayTitle);

        // show a date picker when click on the date
        GregorianCalendar actualDate = new GregorianCalendar();
        mDatePicker = new DatePickerDialog(getContext(), this, actualDate.get(Calendar.YEAR),
                actualDate.get(Calendar.MONTH), actualDate.get(Calendar.DAY_OF_MONTH));
        mCurrentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePicker.show();
            }
        });

        mBaseLayout = (LinearLayout) view.findViewById(R.id.calendar_base_layout);

        ListView listView = (ListView) view.findViewById(R.id.calendar_list_event);
        mEventListAdapter = new EventListViewAdapter(view.getContext(),
                new ArrayList<Event>(0), getActivity());
        listView.setAdapter(mEventListAdapter);
        listView.setOnItemClickListener(mEventListAdapter);

        update();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextButton:
                mGridCalendarAdapter.nextMonth();
                update();
                break;
            case R.id.prevButton:
                mGridCalendarAdapter.prevMonth();
                update();
                break;
            default:
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mGridCalendarAdapter.setFocusedDate(new GregorianCalendar(year, monthOfYear, dayOfMonth));
    }

//---------------------------------------------------------------------------------------------
//----Methods----------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------

    public void update() {
        updateDate();

        List<Event> events = mGridCalendarAdapter.getCurrentEvents();
        mEventListAdapter.setEvents(events);

        mBaseLayout.invalidate();
    }

    private void updateDate() {
        mCurrentDate.setText(mGridCalendarAdapter.getStringDate());
    }
}
