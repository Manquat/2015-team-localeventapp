package ch.epfl.sweng.evento.gui.event_activity;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.epfl.sweng.evento.EventDatabase;
import ch.epfl.sweng.evento.Settings;
import ch.epfl.sweng.evento.User;
import ch.epfl.sweng.evento.event.Event;
import ch.epfl.sweng.evento.gui.ExpendableList;
import ch.epfl.sweng.evento.rest_api.RestApi;
import ch.epfl.sweng.evento.rest_api.callback.GetUserListCallback;
import ch.epfl.sweng.evento.rest_api.callback.HttpResponseCodeCallback;
import ch.epfl.sweng.evento.rest_api.network_provider.DefaultNetworkProvider;
import ch.epfl.sweng.evento.tabs_fragment.Refreshable;

/**
 * Class that handle all the managing of the participant in the event activity
 */
public class JoinEvent implements
        View.OnClickListener,
        HttpResponseCodeCallback,
        GetUserListCallback,
        ExpandableListView.OnChildClickListener,
        Refreshable {
    private static final String TAG = "JoinEvent";

    private Activity mActivity;
    private RestApi mRestApi;
    private Event mCurrentEvent;
    private Button mJoinEventButton;
    private Button mUnJoinEventButton;
    private boolean mIsTheEventJoined;
    private ArrayList<String> mListDataHeader;
    private HashMap<String, List<String>> mListDataChild;
    private ExpendableList mListAdapter;
    private Refreshable mParentRefreshable;

    private JoinEvent(Activity parentActivity, int currentEventId,
                      Button joinEventButton, Button unJoinEventButton,
                      ExpandableListView listViewOfParticipant, Refreshable parentRefreshable) {
        mActivity = parentActivity;
        mRestApi = new RestApi(new DefaultNetworkProvider(), Settings.getServerUrl());
        mCurrentEvent = EventDatabase.INSTANCE.getEvent(currentEventId);
        mJoinEventButton = joinEventButton;
        mUnJoinEventButton = unJoinEventButton;
        mListDataHeader = new ArrayList<>();
        mListDataChild = new HashMap<>();
        mParentRefreshable = parentRefreshable;

        mJoinEventButton.setOnClickListener(this);
        mUnJoinEventButton.setOnClickListener(this);

        mListAdapter = new ExpendableList(mActivity, mListDataHeader, mListDataChild);

        // setting list adapter
        listViewOfParticipant.setAdapter(mListAdapter);

        // ListView on child click listener
        listViewOfParticipant.setOnChildClickListener(this);

        updateButtonState();
        getParticipant();
    }

    public static void initialize(Activity parentActivity, int currentEventId,
                                  Button joinEventButton, Button unJoinEventButton,
                                  ExpandableListView listViewOfParticipant,
                                  Refreshable parentRefreshable) {
        new JoinEvent(parentActivity, currentEventId, joinEventButton, unJoinEventButton,
                listViewOfParticipant, parentRefreshable);
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
                    mRestApi.addParticipant(mCurrentEvent.getID(),
                            Settings.INSTANCE.getUser().getUserId(), this);
                }
            }
        }

        updateButtonState();
        getParticipant();
    }

    private void updateButtonState() {
        mIsTheEventJoined = mCurrentEvent.checkIfParticipantIsIn(Settings.INSTANCE.getUser());

        mJoinEventButton.setEnabled(!mIsTheEventJoined);
        mUnJoinEventButton.setEnabled(mIsTheEventJoined);
    }

    @Override
    public void onSuccess(String httpResponseCode) {
        Log.d(TAG, "Response" + httpResponseCode);
        refresh();
        //mParentRefreshable.refresh();
    }

    private void getParticipant() {
        Log.d(TAG, "getParticipant");
        mRestApi.getParticipant(this, mCurrentEvent.getID());
    }

    @Override
    public void onUserListReceived(List<User> userArrayList) {
        Log.d(TAG, "user list received for event " + mCurrentEvent.getTitle());
        if (userArrayList != null) {
            List<String> participant = new ArrayList<>();
            for (User user : userArrayList) {
                participant.add(user.getUsername());
            }
            mListDataHeader.clear();
            mListDataHeader.add("Participant of the event (" + participant.size() + ")");

            mListDataChild.put(mListDataHeader.get(0), participant);

            mListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        final int groupPosTmp = groupPosition;
        final int childPosTmp = childPosition;
        return false;
    }

    @Override
    public void refresh() {
        getParticipant();
    }
}
