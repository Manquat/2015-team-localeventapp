package ch.epfl.sweng.evento.tabs_fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import ch.epfl.sweng.evento.EventDatabase;
import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.event.Event;
import ch.epfl.sweng.evento.gui.EventListViewAdapter;
import ch.epfl.sweng.evento.gui.infinite_pager_adapter.GridInfinitePageAdapter;
import ch.epfl.sweng.evento.gui.infinite_pager_adapter.InfiniteViewPager;

/**
 * The fragment that holds the calendar and the listView that display the events at the current
 * selected date
 */
public class CalendarTabs extends Fragment implements
        Button.OnClickListener,
        Refreshable,
        DatePickerDialog.OnDateSetListener {


    private GridInfinitePageAdapter mGridCalendarAdapter;
    private TextView mCurrentDate;
    private View mBaseView;
    private EventListViewAdapter mEventListAdapter;
    private DatePickerDialog mDatePicker;
    private InfiniteViewPager mPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // create the calendar centred on the actual date
        GregorianCalendar actualDate = new GregorianCalendar();

        // inflate the layout
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        // create the infinite page viewer at the actual date
        mPager = (InfiniteViewPager) view.findViewById(R.id.calendar_infinite_pager);
        mGridCalendarAdapter = new GridInfinitePageAdapter(actualDate.get(Calendar.DAY_OF_MONTH),
                actualDate.get(Calendar.MONTH),
                actualDate.get(Calendar.YEAR),
                getContext(),
                this);
        mPager.setAdapter(mGridCalendarAdapter);
        mPager.setOnInfinitePageChangeListener(mGridCalendarAdapter);

        Button nextButton = (Button) view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);

        Button prevButton = (Button) view.findViewById(R.id.prevButton);
        prevButton.setOnClickListener(this);

        mCurrentDate = (TextView) view.findViewById(R.id.dayTitle);

        // show a date picker when click on the date
        mDatePicker = new DatePickerDialog(getContext(), this, actualDate.get(Calendar.YEAR),
                actualDate.get(Calendar.MONTH), actualDate.get(Calendar.DAY_OF_MONTH));
        mCurrentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePicker.show();
            }
        });

        ListView listView = (ListView) view.findViewById(R.id.calendar_list_event);
        mEventListAdapter = new EventListViewAdapter(view.getContext(),
                new ArrayList<Event>(0), getActivity());
        listView.setAdapter(mEventListAdapter);
        listView.setOnItemClickListener(mEventListAdapter);

        mBaseView = view;
        refresh();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        //adding the mainActivity to the observer of the eventDatabase
        EventDatabase.INSTANCE.addObserver(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        //removing the mainActivity to the observer of the eventDatabase
        EventDatabase.INSTANCE.removeObserver(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextButton:
                mGridCalendarAdapter.nextMonth();
                refresh();
                break;
            case R.id.prevButton:
                mGridCalendarAdapter.prevMonth();
                refresh();
                break;
            default:
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mGridCalendarAdapter.setFocusedDate(new GregorianCalendar(year, monthOfYear, dayOfMonth));
    }


    public void refresh() {
        updateDate();

        mGridCalendarAdapter.notifyDataSetChanged();

        List<Event> events = mGridCalendarAdapter.getCurrentEvents();
        mEventListAdapter.setEvents(events);

        mPager.invalidate();
        mBaseView.invalidate();
    }

    private void updateDate() {
        mCurrentDate.setText(mGridCalendarAdapter.getStringDate());
    }
}
