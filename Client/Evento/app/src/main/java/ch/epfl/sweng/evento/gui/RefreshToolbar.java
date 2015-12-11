package ch.epfl.sweng.evento.gui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import ch.epfl.sweng.evento.EventDatabase;
import ch.epfl.sweng.evento.R;

/**
 * Manage all the aspect of the toolbar and the automatic refresh
 */
public class RefreshToolbar implements Toolbar.OnMenuItemClickListener {
    private Activity mActivity;

    RefreshToolbar(Activity activityParent) {
        mActivity = activityParent;
    }

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
