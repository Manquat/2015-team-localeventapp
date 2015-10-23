package ch.epfl.sweng.evento;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import ch.epfl.sweng.evento.tabsFragment.CalendarTabs;
import ch.epfl.sweng.evento.tabsFragment.ContentFragment;
import ch.epfl.sweng.evento.tabsFragment.MapsFragment;

/**
 * Created by Gautier on 21/10/2015.
 */
public class ViewPageAdapter extends FragmentStatePagerAdapter
{
    private List<CharSequence> mTitles; // This will Store the Titles of the Tabs

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPageAdapter(FragmentManager fragmentManager, List<CharSequence> titles)
    {
        super(fragmentManager);

        mTitles = titles;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0:
                return new MapsFragment();
            case 2:
                return new CalendarTabs();
            default:
                return ContentFragment.newInstance("Event", 0, 0);
        }
    }

    // This method return the Number of tabs for the tabs Strip
    @Override
    public int getCount() {
        return mTitles.size();
    }

    // This method return the titles for the Tabs in the CalendarTabs Strip
    @Override
    public CharSequence getPageTitle(int position)
    {
        return mTitles.get(position);
    }
}

