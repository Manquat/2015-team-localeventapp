package ch.epfl.sweng.evento.gui;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

/**
 * Created by gautier on 07/12/2015.
 */
public class UpdateEventListener implements View.OnClickListener
{
    private Activity mActivity;
    private int mCurrentEventId;

    public UpdateEventListener(Activity activityParent, int currentEventId)
    {
        mActivity = activityParent;
        mCurrentEventId = currentEventId;
    }

    @Override
    public void onClick(View v)
    {
        Intent intent = new Intent(mActivity, UpdateEventActivity.class);
        intent.putExtra(UpdateEventActivity.EVENT_TO_UPDATE, mCurrentEventId);
        mActivity.startActivity(intent);
    }
}
