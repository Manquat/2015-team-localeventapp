package ch.epfl.sweng.evento.gui.infinite_pager_adapter;

import android.app.Activity;
import android.widget.Button;

/**
 * Created by gautier on 07/12/2015.
 */
public class ModifyEvent {
    private Activity mActivity;
    private int mCurrentEventId;
    private Button mDeleteEventButton;
    private Button mUpdateEventButton;

    private ModifyEvent(Activity activityParent, int currentEventId,
                        Button deleteEventButton, Button updateEventButton) {
        mActivity = activityParent;
        mCurrentEventId = currentEventId;
        mDeleteEventButton = deleteEventButton;
        mUpdateEventButton = updateEventButton;


    }

    public static void initialize(Activity activityParent, int currentEventId,
                                  Button deleteEventButton, Button updateEventButton) {
        new ModifyEvent(activityParent, currentEventId, deleteEventButton, updateEventButton);
    }
}
