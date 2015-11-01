package ch.epfl.sweng.evento.tabsFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.tabsFragment.Calendar.GridCalendarAdapter;

/**
 * Created by Gautier on 21/10/2015.
 */
public class CalendarTabs extends Fragment implements Button.OnClickListener
{
    private GridCalendarAdapter mGridCalendarAdapter;
    private TextView mCurrentDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        GridView gridView = (GridView) view.findViewById(R.id.gridview);
        mGridCalendarAdapter = new GridCalendarAdapter(view.getContext());
        gridView.setAdapter(mGridCalendarAdapter);

        Button nextButton = (Button) view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);

        Button prevButton = (Button) view.findViewById(R.id.prevButton);
        prevButton.setOnClickListener(this);

        mCurrentDate = (TextView) view.findViewById(R.id.dayTitle);
        updateDate();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.nextButton:
                mGridCalendarAdapter.nextMonth();
                updateDate();
                break;
            case R.id.prevButton:
                mGridCalendarAdapter.prevMonth();
                updateDate();
                break;
            default:
        }
    }

    private void updateDate()
    {
        mCurrentDate.setText(mGridCalendarAdapter.getStringDate());
    }
}
