package ch.epfl.sweng.evento.InfinitePagerAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import ch.epfl.sweng.evento.R;

/**
 * An infinite page adapter for the grid of the calendar
 */
public class GridInfinitePageAdapter extends InfinitePagerAdapter<Integer> {
    private Context mContext;

    /**
     * Standard constructor.
     *
     * @param initValue the initial indicator value the ViewPager should start with.
     */
    public GridInfinitePageAdapter(Integer initValue, Context context) {
        super(initValue);
        mContext = context;
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
}
