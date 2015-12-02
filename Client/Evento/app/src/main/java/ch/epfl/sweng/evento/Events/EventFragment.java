package ch.epfl.sweng.evento.Events;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import ch.epfl.sweng.evento.DefaultNetworkProvider;
import ch.epfl.sweng.evento.EventDatabase;
import ch.epfl.sweng.evento.ExpendableList;
import ch.epfl.sweng.evento.MainActivity;
import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.RestApi.PutCallback;
import ch.epfl.sweng.evento.RestApi.RestApi;
import ch.epfl.sweng.evento.Settings;
import ch.epfl.sweng.evento.User;

/**
 * Fragment that display an Event with an ID passed as an Extra with the key KEYCURRENTEVENT.
 * After that allow to swipe left or right to explore the events actually loaded.
 */
public class EventFragment extends Fragment {

    public static final String KEYCURRENTEVENT = "CurrentEvent";
    private RestApi mRestAPI;
    private Event mEvent;
    private ArrayList<String> mListDataHeader;
    private HashMap<String, List<String>> mListDataChild;
    private View mRootView;
    private Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState){
        Bundle bundle = getArguments();
        int currentEventID = bundle.getInt(KEYCURRENTEVENT);

        mActivity = getActivity();
        mEvent = EventDatabase.INSTANCE.getEvent(currentEventID);
    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_event, container, false);

        updateFields();

        return mRootView;
    }

    private void updateFields() {
        TextView titleView = (TextView) mRootView.findViewById(R.id.event_title_view);
        TextView creatorView = (TextView) mRootView.findViewById(R.id.event_creator_view);
        TextView startDateView = (TextView) mRootView.findViewById(R.id.event_start_date_view);
        TextView endDateView = (TextView) mRootView.findViewById(R.id.event_end_date_view);
        TextView addressView = (TextView) mRootView.findViewById(R.id.event_address_view);
        TextView descriptionView = (TextView) mRootView.findViewById(R.id.event_description_view);

        titleView.setText(mEvent.getTitle());
        creatorView.setText(mEvent.getCreator());
        startDateView.setText(mEvent.getStartDateAsString());
        endDateView.setText(mEvent.getEndDateAsString());
        addressView.setText(mEvent.getAddress());
        descriptionView.setText(mEvent.getDescription());
        setTagExpandableList();

        ImageView pictureView = (ImageView) mRootView.findViewById(R.id.eventPictureView);
        pictureView.setImageBitmap(mEvent.getPicture());
        final Button joinEvent = (Button) mRootView.findViewById(R.id.joinEvent);
        joinEvent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "Joined", Toast.LENGTH_SHORT).show();
                MainActivity.getUser(1).addMatchedEvent(mEvent);
                if(mEvent.addParticipant(MainActivity.getUser(1))) {
                    if(mEvent.isFull()) {
                        joinEvent.setClickable(false);
                    }
                    mRestAPI = new RestApi(new DefaultNetworkProvider(), Settings.getServerUrl());
                    mRestAPI.updateEvent(mEvent,new PutCallback() {
                        @Override
                        public void onPostSuccess(String response) {
                            Log.d("EventFrag.upd.", "Response" + response);
                        }
                    });
                } else {
                    Toast.makeText(mActivity.getApplicationContext(), "Sorry but this event just got completed by another guy.", Toast.LENGTH_SHORT).show();
                }
                getActivity().finish();
            }
        });
    }

    private void setTagExpandableList() {
        // get the ListView
        ExpandableListView mExpListView = (ExpandableListView) mRootView.findViewById(R.id.listParticipantExp);

        // preparing list data
        prepareListData();
        ExpendableList mListAdapter = new ExpendableList(getActivity().getApplicationContext(), mListDataHeader, mListDataChild);

        // setting list adapter
        mExpListView.setAdapter(mListAdapter);

        // ListView on child click listener
        mExpListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                final int groupPosTmp = groupPosition;
                final int childPosTmp = childPosition;
                /* new HashSet<String>() {{
                    add(mListDataChild.get(
                            mListDataHeader.get(groupPosTmp)).get(
                            childPosTmp));
                }};*/

                return false;
            }
        });
    }

    private void prepareListData() {
        mListDataHeader = new ArrayList<String>();
        mListDataChild = new HashMap<String, List<String>>();

        // Adding child data
        mListDataHeader.add("Host of the event");
        mListDataHeader.add("Participant of the event (" + mEvent.getAllParticipant().size() + ")");


        // Adding child data
        List<String> host = new ArrayList<String>();
        host.add(mEvent.getCreator());

        List<String> participant = new ArrayList<String>();
        for (User user: mEvent.getAllParticipant()) {
            participant.add(user.getmUsername());
        }


        mListDataChild.put(mListDataHeader.get(0), host);
        mListDataChild.put(mListDataHeader.get(1), participant);
    }
}
