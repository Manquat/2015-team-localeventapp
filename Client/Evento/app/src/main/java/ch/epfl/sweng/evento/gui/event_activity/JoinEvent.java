package ch.epfl.sweng.evento.gui.event_activity;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.epfl.sweng.evento.EventDatabase;
import ch.epfl.sweng.evento.Settings;
import ch.epfl.sweng.evento.User;
import ch.epfl.sweng.evento.event.Event;
import ch.epfl.sweng.evento.rest_api.RestApi;
import ch.epfl.sweng.evento.rest_api.callback.GetUserListCallback;
import ch.epfl.sweng.evento.rest_api.callback.HttpResponseCodeCallback;
import ch.epfl.sweng.evento.rest_api.network_provider.DefaultNetworkProvider;
import ch.epfl.sweng.evento.tabs_fragment.Refreshable;

/**
 * Class that handle all the managing of the getParticipant in the event activity
 */
public class JoinEvent implements
        View.OnClickListener,
        HttpResponseCodeCallback,
        GetUserListCallback,
        Refreshable
{
    private static final String TAG = "JoinEvent";

    private Activity mActivity;
    private RestApi mRestApi;
    private Event mCurrentEvent;
    private Button mJoinEventButton;
    private Button mUnJoinEventButton;
    private boolean mIsTheEventJoined;
    private Set<User> mParticipant;
    private Refreshable mParentRefreshable;

    private JoinEvent(Activity parentActivity, int currentEventId,
                      Button joinEventButton, Button unJoinEventButton,
                      Refreshable parentRefreshable)
    {
        mActivity = parentActivity;
        mRestApi = new RestApi(new DefaultNetworkProvider(), Settings.getServerUrl());
        mCurrentEvent = EventDatabase.INSTANCE.getEvent(currentEventId);
        mJoinEventButton = joinEventButton;
        mUnJoinEventButton = unJoinEventButton;
        mParticipant = new HashSet<>();
        mParentRefreshable = parentRefreshable;

        mJoinEventButton.setOnClickListener(this);
        mUnJoinEventButton.setOnClickListener(this);

        getParticipant();

        updateButtonState();
    }

    public static void initialize(Activity parentActivity, int currentEventId,
                                  Button joinEventButton, Button unJoinEventButton,
                                  Refreshable listOfParticipant)
    {
        new JoinEvent(parentActivity, currentEventId, joinEventButton, unJoinEventButton,
                listOfParticipant);
    }

    @Override
    public void onClick(View view)
    {
        if (mIsTheEventJoined)
        {
            Log.d(TAG, Settings.getUser().getUsername() + " unjoin");
            if (!mCurrentEvent.removeParticipant(Settings.getUser()))
            {
                Log.d(TAG, "addParticipant just returned false");
                mActivity.finish();
            } else
            {
                Toast.makeText(mActivity.getApplicationContext(), "UnJoined", Toast.LENGTH_SHORT).show();

                mRestApi.removeParticipant(mCurrentEvent.getID(), Settings.getUser().getUserId(), this);
            }

        } else
        {
            Log.d(TAG, Settings.getUser().getUsername() + " join");
            if (!mCurrentEvent.addParticipant(Settings.getUser()))
            {
                Log.d(TAG, "addParticipant just returned false");
                mActivity.finish();
            } else
            {
                Toast.makeText(mActivity.getApplicationContext(), "Joined", Toast.LENGTH_SHORT).show();
                if (Settings.getUser().addMatchedEvent(mCurrentEvent))
                {
                    mRestApi.addParticipant(mCurrentEvent.getID(),
                            Settings.getUser().getUserId(), this);
                }
            }
        }

        updateButtonState();
        getParticipant();
    }

    private void updateButtonState()
    {
        mIsTheEventJoined = mParticipant.contains(Settings.getUser());

        mJoinEventButton.setEnabled(!mIsTheEventJoined);
        mUnJoinEventButton.setEnabled(mIsTheEventJoined);

        mParentRefreshable.refresh();
    }

    @Override
    public void onSuccess(String httpResponseCode)
    {
        Log.d(TAG, "Response" + httpResponseCode);
        refresh();
    }

    private void getParticipant()
    {
        Log.d(TAG, "getParticipant");
        mRestApi.getParticipant(this, mCurrentEvent.getID());
    }

    @Override
    public void onUserListReceived(List<User> userArrayList)
    {
        Log.d(TAG, "user list received for event " + mCurrentEvent.getTitle());
        if (userArrayList != null)
        {
            mParticipant.clear();
            mParticipant.addAll(userArrayList);
        }

        updateButtonState();
    }

    @Override
    public void refresh()
    {
        getParticipant();
    }
}
