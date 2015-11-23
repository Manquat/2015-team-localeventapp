package ch.epfl.sweng.evento.InfinitePagerAdapter;

import android.view.ViewGroup;

/**
 * An infinite page adapter for the grid of the calendar
 */
public class GridInfinitePageAdapter extends InfinitePagerAdapter<Integer> {

    /**
     * Standard constructor.
     *
     * @param initValue the initial indicator value the ViewPager should start with.
     */
    public GridInfinitePageAdapter(Integer initValue) {
        super(initValue);
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
        return null;
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
