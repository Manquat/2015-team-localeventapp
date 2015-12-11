package ch.epfl.sweng.evento.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


import java.util.Timer;
import java.util.TimerTask;

import ch.epfl.sweng.evento.EventDatabase;
import ch.epfl.sweng.evento.R;

/**
 * Manage all the aspect of the toolbar except the creation
 * Manage the automatic refresh
 */
public class AutoRefreshToolbar implements Toolbar.OnMenuItemClickListener {
    private AppCompatActivity mActivity;

    /**
     * Constructor that make this object as the listener of the click on the menu and
     * start the Timer to automatically refresh every 10min
     * @param activityParent the parentActivity that must be an AppCompatActivity (to accept a Toolbar)
     * @param toolbar the toolbar in the current activity
     */
    public AutoRefreshToolbar(AppCompatActivity activityParent, Toolbar toolbar) {
        mActivity = activityParent;

        mActivity.setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(this);

        //to refresh every 10 minutes
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                EventDatabase.INSTANCE.refresh();
            }
        }, 0, 10 * 60 * 1000);
    }

    /**
     * Call when a click on the menu is made
     * Implements the OnMenuItemClickListener interface
     * @param item the item that have been click by the user
     * @return true to eventually inflate the menu if the click is on 3 dots
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        // based on the current position you can then cast the page to the correct
        // class and call the method:
        if (id == R.id.action_createAnEvent) {
            Intent intent = new Intent(mActivity, CreatingEventActivity.class);
            mActivity.startActivity(intent);
        } else if (id == R.id.action_search) {
            Intent intent = new Intent(mActivity, SearchActivity.class);
            mActivity.startActivity(intent);
        } else if (id == R.id.action_refresh) {
            EventDatabase.INSTANCE.refresh();
        } else if (id == R.id.action_logout) {
            Intent intent = new Intent(mActivity, LoginActivity.class);
            intent.putExtra(LoginActivity.LOGOUT_TAG, true);
            mActivity.startActivity(intent);
            mActivity.finish();
        } else if (id == R.id.action_manageYourEvent) {
            Intent intent = new Intent(mActivity, ManageActivity.class);
            mActivity.startActivity(intent);
        }

        return true;
    }
}
