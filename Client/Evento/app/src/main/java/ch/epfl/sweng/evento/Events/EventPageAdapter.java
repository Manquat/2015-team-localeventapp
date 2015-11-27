package ch.epfl.sweng.evento.Events;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ch.epfl.sweng.evento.EventDatabase;

/**
 * Created by Gautier on 13/11/2015.
 */
public class EventPageAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = "EventPagerAdapter";

    public EventPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        EventFragment fragment = new EventFragment();

        // define the current event for this fragment
        Bundle bundle = new Bundle();
        bundle.putInt(EventFragment.KEYCURRENTEVENT,
                EventDatabase.INSTANCE.get(position).getID());
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public int getCount() {
        return 100;
    }
}
