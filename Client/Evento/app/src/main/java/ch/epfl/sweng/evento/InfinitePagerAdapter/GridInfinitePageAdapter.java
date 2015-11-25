package ch.epfl.sweng.evento.InfinitePagerAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import ch.epfl.sweng.evento.EventDatabase;
import ch.epfl.sweng.evento.Events.Event;
import ch.epfl.sweng.evento.InfinitePagerAdapter.internal.Constants;
import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.tabsFragment.Calendar.GridCalendarAdapter;
import ch.epfl.sweng.evento.tabsFragment.Updatable;

/**
 * An infinite page adapter for the grid of the calendar
 */
public class GridInfinitePageAdapter extends InfinitePagerAdapter<Integer> implements Updatable, InfiniteViewPager.OnInfinitePageChangeListener {
    private Context mContext;
    private int mDayOfMonth;
    private int mYear;
    private Updatable mParent;

    /**
     * Standard constructor.
     *
     * @param targetedMonth the initial month the calendar should start at.
     */
    public GridInfinitePageAdapter(int targetedDayOfMonth, Integer targetedMonth, int targetedYear, Context context, Updatable updatableParent) {
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
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_calendar,
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
    public void update() {
        mParent.update();
    }

    public  void nextMonth() {
        setCurrentIndicator(getNextIndicator());
        fillPage(Constants.PAGE_POSITION_CENTER);
        update();
    }

    public void prevMonth() {
        setCurrentIndicator(getPreviousIndicator());
        fillPage(Constants.PAGE_POSITION_CENTER);
        update();
    }

    public void setFocusedDate(Calendar focusedDate) {
        mYear = focusedDate.get(Calendar.YEAR);
        mDayOfMonth = focusedDate.get(Calendar.DAY_OF_MONTH);
        setCurrentIndicator(focusedDate.get(Calendar.MONTH));
        fillPage(Constants.PAGE_POSITION_CENTER);
        update();
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
        update();
    }

    @Override
    public void onPageSelected(Object indicator) {
        update();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        update();
    }
}
