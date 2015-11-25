package ch.epfl.sweng.evento.InfinitePagerAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.util.Calendar;
import java.util.GregorianCalendar;

import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.tabsFragment.Calendar.GridCalendarAdapter;
import ch.epfl.sweng.evento.tabsFragment.Updatable;

/**
 * An infinite page adapter for the grid of the calendar
 */
public class GridInfinitePageAdapter extends InfinitePagerAdapter<Integer> implements Updatable {
    private Context mContext;
    private int mYear;
    private Updatable mParent;

    /**
     * Standard constructor.
     *
     * @param targetedMonth the initial month the calendar should start at.
     */
    public GridInfinitePageAdapter(Integer targetedMonth, int targetedYear, Context context, Updatable updatableParent) {
        super(targetedMonth);
        mContext = context;
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
        Calendar actualMonth = new GregorianCalendar(mYear, indicator, 15);

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
}
