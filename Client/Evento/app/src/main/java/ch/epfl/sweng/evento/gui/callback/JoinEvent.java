package ch.epfl.sweng.evento.gui.callback;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ch.epfl.sweng.evento.EventDatabase;
import ch.epfl.sweng.evento.Settings;
import ch.epfl.sweng.evento.event.Event;
import ch.epfl.sweng.evento.rest_api.RestApi;
import ch.epfl.sweng.evento.rest_api.callback.HttpResponseCodeCallback;
import ch.epfl.sweng.evento.rest_api.network_provider.DefaultNetworkProvider;

/**
 * Callback for the join button on the event activity
 */
public class JoinEvent extends HttpResponseCodeCallback implements View.OnClickListener {
    private static final String TAG = "JoinEvent";

    private Activity mActivity;
    private RestApi mRestApi;
    private Event mCurrentEvent;
    private Button mJoinEventButton;
    private Button mUnJoinEventButton;
    private boolean mIsTheEventJoined;

    private JoinEvent(Activity parentActivity, int currentEventId,
                     Button joinEventButton, Button unJoinEventButton) {
        mActivity = parentActivity;
        mRestApi = new RestApi(new DefaultNetworkProvider(), Settings.getServerUrl());
        mCurrentEvent = EventDatabase.INSTANCE.getEvent(currentEventId);
        mJoinEventButton = joinEventButton;
        mUnJoinEventButton = unJoinEventButton;

        mJoinEventButton.setOnClickListener(this);
        mUnJoinEventButton.setOnClickListener(this);

        updateButtonState();
    }

    public static void initialize(Activity parentActivity, int currentEventId,
                            Button joinEventButton, Button unJoinEventButton) {
        new JoinEvent(parentActivity, currentEventId, joinEventButton, unJoinEventButton);
    }

    @Override
    public void onClick(View view) {
        if (mIsTheEventJoined) {
            Log.d(TAG, Settings.INSTANCE.getUser().getUsername() + " unjoin");
            if (!mCurrentEvent.removeParticipant(Settings.INSTANCE.getUser())) {
                Log.d(TAG, "addParticipant just returned false");
                mActivity.finish();
            } else {
                Toast.makeText(mActivity.getApplicationContext(), "UnJoined", Toast.LENGTH_SHORT).show();
                if (Settings.INSTANCE.getUser().removeMatchedEvent(mCurrentEvent)) {
                    mRestApi.removeParticipant(mCurrentEvent.getID(), Settings.INSTANCE.getUser().getUserId(), this);
                }
            }

        } else {
            Log.d(TAG, Settings.INSTANCE.getUser().getUsername() + " join");
            if (!mCurrentEvent.addParticipant(Settings.INSTANCE.getUser())) {
                Log.d(TAG, "addParticipant just returned false");
                mActivity.finish();
            } else {
                Toast.makeText(mActivity.getApplicationContext(), "Joined", Toast.LENGTH_SHORT).show();
                if (Settings.INSTANCE.getUser().addMatchedEvent(mCurrentEvent)) {
                    mRestApi.addParticipant(mCurrentEvent.getID(), Settings.INSTANCE.getUser().getUserId(), this);
                }
            }
        }

        updateButtonState();
    }

    private void updateButtonState() {
        mIsTheEventJoined = mCurrentEvent.checkIfParticipantIsIn(Settings.INSTANCE.getUser());

        mJoinEventButton.setEnabled(!mIsTheEventJoined);
        mUnJoinEventButton.setEnabled(mIsTheEventJoined);
    }

    @Override
    public void onSuccess(String httpResponseCode) {
        Log.d(TAG, "Response" + httpResponseCode);
        mActivity.finish();
    }
}
