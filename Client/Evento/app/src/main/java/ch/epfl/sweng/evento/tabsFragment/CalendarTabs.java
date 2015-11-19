package ch.epfl.sweng.evento.tabsFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Collection;

import ch.epfl.sweng.evento.Events.Event;
import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.tabsFragment.Calendar.GridCalendarAdapter;

/**
 * Created by Gautier on 21/10/2015.
 */
public class CalendarTabs extends Fragment implements Button.OnClickListener {
//---------------------------------------------------------------------------------------------
//----Members----------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------

    private GridCalendarAdapter mGridCalendarAdapter;
    private TextView mCurrentDate;
    private LinearLayout mBaseLayout;
    private TextView    mTestText;

//---------------------------------------------------------------------------------------------
//----Callbacks--------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        GridView gridView = (GridView) view.findViewById(R.id.gridview);
        mGridCalendarAdapter = new GridCalendarAdapter(view.getContext());
        gridView.setAdapter(mGridCalendarAdapter);

        Button nextButton = (Button) view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);

        Button prevButton = (Button) view.findViewById(R.id.prevButton);
        prevButton.setOnClickListener(this);

        mCurrentDate = (TextView) view.findViewById(R.id.dayTitle);

        mBaseLayout = (LinearLayout) view.findViewById(R.id.calendar_base_layout);

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

//---------------------------------------------------------------------------------------------
//----Methods----------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------

    private void update() {
        updateDate();

        Collection<Event> events = mGridCalendarAdapter.getCurrentEvents();

        if (events != null) {
            for (Event event : events) {
                TextView eventTextView = new TextView(getContext());
                eventTextView.setText(event.getTitle());

                mBaseLayout.addView(eventTextView);
            }
        }
    }

    private void updateDate() {
        mCurrentDate.setText(mGridCalendarAdapter.getStringDate());
    }
}
