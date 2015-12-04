package ch.epfl.sweng.evento.infinite_pager_adapter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.epfl.sweng.evento.EventDatabase;
import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.Settings;
import ch.epfl.sweng.evento.User;
import ch.epfl.sweng.evento.event.Event;
import ch.epfl.sweng.evento.gui.ExpendableList;
import ch.epfl.sweng.evento.rest_api.RestApi;
import ch.epfl.sweng.evento.rest_api.callback.GetUserCallback;
import ch.epfl.sweng.evento.rest_api.callback.GetUserListCallback;
import ch.epfl.sweng.evento.rest_api.callback.HttpResponseCodeCallback;
import ch.epfl.sweng.evento.rest_api.network_provider.DefaultNetworkProvider;

/**
 * An infinite page adapter for the event activity
 */
public class EventInfinitePageAdapter extends InfinitePagerAdapter<Integer> {
    private static final String TAG = "EventInfPageAdap";
    private Activity mActivity;
    private Event mEvent;
    private RestApi mRestAPI;

    private String mCreator;


    private ArrayList<String> mListDataHeader;
    private HashMap<String, List<String>> mListDataChild;
    private View mRootView;
    private List<User> mParticipants;
    private List<Event> hostedEvent;
    private ExpandableListView mExpListView;



    public EventInfinitePageAdapter(Integer initialEventSignature, Activity activity) {
        super(initialEventSignature);

        mRestAPI = new RestApi(new DefaultNetworkProvider(), Settings.getServerUrl());
        mActivity = activity;

    }

    private void getParticipant(){
        mParticipants = new ArrayList<User>();

        mRestAPI.getParticipant(new GetUserListCallback() {
            public void onUserListReceived(List<User> userArrayList) {
                if(userArrayList != null) {
                    mParticipants = userArrayList;
                    List<String> participant = new ArrayList<String>();
                    for (User user : mParticipants) {
                        participant.add(user.getUsername());
                    }
                    if (mListDataHeader.size() < 2) {
                        mListDataHeader.add("Participant of the event (" + mParticipants.size() + ")");
                    }
                    mListDataChild.put(mListDataHeader.get(1), participant);
                    ExpendableList mListAdapter = new ExpendableList(mActivity.getApplicationContext(), mListDataHeader, mListDataChild);

                    // setting list adapter
                    mExpListView.setAdapter(mListAdapter);

                    // ListView on child click listener
                    mExpListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                        @Override
                        public boolean onChildClick(ExpandableListView parent, View v,
                                                    int groupPosition, int childPosition, long id) {
                            final int groupPosTmp = groupPosition;
                            final int childPosTmp = childPosition;
                            return false;
                        }
                    });
                }
            }

        }, mEvent.getID());

    }

    @Override
    public Integer getNextIndicator() {
        Event currentEvent = EventDatabase.INSTANCE.getEvent(getCurrentIndicator());
        return EventDatabase.INSTANCE.getNextEvent(currentEvent).getID();
    }

    @Override
    public Integer getPreviousIndicator() {
        Event currentEvent = EventDatabase.INSTANCE.getEvent(getCurrentIndicator());
        return EventDatabase.INSTANCE.getPreviousEvent(currentEvent).getID();
    }

    @Override
    public ViewGroup instantiateItem(Integer currentEventId) {
        // getting the event
        Event event = EventDatabase.INSTANCE.getEvent(currentEventId);

        // inflating  the layout
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        GridLayout layout = (GridLayout) inflater.inflate(R.layout.fragment_event,
                (ViewGroup) mActivity.getWindow().getDecorView().getRootView(), false);

        mRootView = layout;
        mEvent = event;

        updateFields(layout, event);

        return layout;
    }

    public void init(){
        mCreator = new String();
        mListDataHeader = new ArrayList<String>();
        mListDataChild = new HashMap<String, List<String>>();


        getParticipant();
    }

    private void updateFields(ViewGroup rootView, Event currentEvent) {
        TextView titleView = (TextView) rootView.findViewById(R.id.event_title_view);
        TextView startDateView = (TextView) rootView.findViewById(R.id.event_start_date_view);
        TextView endDateView = (TextView) rootView.findViewById(R.id.event_end_date_view);
        TextView addressView = (TextView) rootView.findViewById(R.id.event_address_view);
        TextView descriptionView = (TextView) rootView.findViewById(R.id.event_description_view);
        mExpListView = (ExpandableListView) mRootView.findViewById(R.id.list_participant_exp);
        init();
        titleView.setText(currentEvent.getTitle());
        startDateView.setText(currentEvent.getStartDateAsString());
        endDateView.setText(currentEvent.getEndDateAsString());
        addressView.setText(currentEvent.getAddress());
        descriptionView.setText(currentEvent.getDescription());

        ImageView pictureView = (ImageView) rootView.findViewById(R.id.eventPictureView);
        pictureView.setImageBitmap(currentEvent.getPicture());
        Button joinEvent = (Button) rootView.findViewById(R.id.joinEvent);
        joinEvent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d(TAG, Settings.INSTANCE.getUser().getEmail());
                if (!mEvent.addParticipant(Settings.INSTANCE.getUser())) {
                    Log.d("TAG", "addParticipant just returned false");
                    mActivity.finish();
                } else {
                    Toast.makeText(mActivity.getApplicationContext(), "Joined", Toast.LENGTH_SHORT).show();
                    Settings.INSTANCE.getUser().addMatchedEvent(mEvent);
                    mRestAPI.addParticipant(mEvent.getID(), Settings.INSTANCE.getUser().getUserId(), new HttpResponseCodeCallback() {
                        @Override
                        public void onSuccess(String response) {
                            Log.d(TAG, "Response" + response);
                            mActivity.finish();
                        }
                    });
                }
            }
        });

        Button removeUserFromEvent = (Button) rootView.findViewById(R.id.remove_user_from_event);
        if(mEvent.checkIfParticipantIsIn(Settings.INSTANCE.getUser())){
            removeUserFromEvent.setVisibility(View.VISIBLE);
        } else {
            removeUserFromEvent.setVisibility(View.INVISIBLE);
        }
        removeUserFromEvent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(mActivity.getApplicationContext(), "Removed from the event", Toast.LENGTH_SHORT).show();
                mRestAPI.removeParticipant(mEvent.getID(), Settings.INSTANCE.getUser().getUserId(), new HttpResponseCodeCallback() {
                    @Override
                    public void onSuccess(String response) {
                        Log.d(TAG, "Response" + response);
                        mActivity.finish();
                    }
                });
            }
        });
    }

}
