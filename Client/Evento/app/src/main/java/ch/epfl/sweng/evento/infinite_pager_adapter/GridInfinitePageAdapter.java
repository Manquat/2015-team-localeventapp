package ch.epfl.sweng.evento.infinite_pager_adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import ch.epfl.sweng.evento.EventDatabase;
import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.calendar.GridCalendarAdapter;
import ch.epfl.sweng.evento.event.Event;
import ch.epfl.sweng.evento.infinite_pager_adapter.internal.Constants;
import ch.epfl.sweng.evento.tabs_fragment.Refreshable;

/**
 * An infinite page adapter for the grid of the calendar
 */
public class GridInfinitePageAdapter extends InfinitePagerAdapter<Integer> implements Refreshable, InfiniteViewPager.OnInfinitePageChangeListener {
    private Context mContext;
    private int mDayOfMonth;
    private int mYear;
    private Refreshable mParent;

    /**
     * Standard constructor.
     *
     * @param targetedMonth the initial month the calendar should start at.
     */
    public GridInfinitePageAdapter(int targetedDayOfMonth, Integer targetedMonth, int targetedYear, Context context, Refreshable updatableParent) {
        super(targetedMonth);
        mContext = context;
        mDayOfMonth = targetedDayOfMonth;
        mYear = targetedYear;
        mParent = updatableParent;
    }

    @Override
    public Integer getNextIndicator() {
        return getCurrentIndicator() + 1;
    }

    @Override
    public Integer getPreviousIndicator() {
        return getCurrentIndicator() - 1;
    }

    @Override
    public ViewGroup instantiateItem(Integer indicator) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.calendar_gridview,
                (ViewGroup) ((Activity) mContext).getWindow().getDecorView().getRootView(), false);

        // create a calendar at the wanted month
        Calendar actualMonth = new GregorianCalendar(mYear, indicator, mDayOfMonth);

        // initialize the grid view
        GridView gridView = (GridView) layout.findViewById(R.id.gridView);
        gridView.setAdapter(new GridCalendarAdapter(mContext, this, actualMonth));

        return layout;
    }

    @Override
    public String getStringRepresentation(Integer currentIndicator) {
        return Integer.toString(currentIndicator);
    }

    @Override
    public Integer convertToIndicator(String representation) {
        return Integer.valueOf(representation);
    }

    @Override
    public void refresh() {
        mParent.refresh();
    }

    public void nextMonth() {
        setCurrentIndicator(getNextIndicator());
        fillPage(Constants.PAGE_POSITION_CENTER);
        refresh();
    }

    public void prevMonth() {
        setCurrentIndicator(getPreviousIndicator());
        fillPage(Constants.PAGE_POSITION_CENTER);
        refresh();
    }

    public void setFocusedDate(Calendar focusedDate) {
        mYear = focusedDate.get(Calendar.YEAR);
        mDayOfMonth = focusedDate.get(Calendar.DAY_OF_MONTH);
        setCurrentIndicator(focusedDate.get(Calendar.MONTH));
        fillPage(Constants.PAGE_POSITION_CENTER);
        refresh();
    }

    public List<Event> getCurrentEvents() {
        Calendar focusedDate = new GregorianCalendar(mYear, getCurrentIndicator(), mDayOfMonth);
        return EventDatabase.INSTANCE.filterOnDay(focusedDate).toArrayList();
    }

    public String getStringDate() {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL);
        Calendar focusedDate = new GregorianCalendar(mYear, getCurrentIndicator(), mDayOfMonth);
        return dateFormat.format(focusedDate.getTime());
    }

    @Override
    public void onPageScrolled(Object indicator, float positionOffset, int positionOffsetPixels) {
        refresh();
    }

    @Override
    public void onPageSelected(Object indicator) {
        refresh();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        refresh();
    }
}
