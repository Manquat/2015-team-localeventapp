package ch.epfl.sweng.evento.event;

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

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import ch.epfl.sweng.evento.EventDatabase;
import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.Settings;
import ch.epfl.sweng.evento.User;
import ch.epfl.sweng.evento.gui.ExpendableList;
import ch.epfl.sweng.evento.gui.MainActivity;
import ch.epfl.sweng.evento.rest_api.RestApi;
import ch.epfl.sweng.evento.rest_api.callback.HttpResponseCodeCallback;
import ch.epfl.sweng.evento.rest_api.network_provider.DefaultNetworkProvider;
import ch.epfl.sweng.evento.rest_api.task.PutTask;

/**
 * Fragment that display an Event with an ID passed as an Extra with the key KEYCURRENTEVENT.
 * After that allow to swipe left or right to explore the events actually loaded.
 */
public class EventFragment extends Fragment {

    private static final String TAG = "EventFragment";
    public static final String KEYCURRENTEVENT = "CurrentEvent";
   /* private RestApi mRestAPI;
    private Event mEvent;
    private ArrayList<String> mListDataHeader;
    private HashMap<String, List<String>> mListDataChild;
    private View mRootView;


    @Override
    public void onCreate(Bundle savedInstanceState){
        Bundle bundle = getArguments();
        int currentEventID = bundle.getInt(KEYCURRENTEVENT);
        mRestAPI = new RestApi(new DefaultNetworkProvider(), Settings.getServerUrl());

        mEvent = EventDatabase.INSTANCE.getEvent(currentEventID);
    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_event, container, false);
        updateFields();

        return mRootView;
    }



    private void getParticipant(int signature){
        mParticipants = new ArrayList<User>();
        mEvent = EventDatabase.INSTANCE.getEvent(signature);

        RestApi restAPI = new RestApi(new DefaultNetworkProvider(), Settings.getServerUrl());
        restAPI.getUser(new GetEventListCallback() {
            public void onUserListReceived(List<User> userArrayList) {
                mParticipants = userArrayList;
            }
            public void onEventListReceived(List<Event> eventArrayList){

            }

        }, mEvent.getID());

        //Hosted event
        /*hostedEvent = new ArrayList<Event>();;
        restAPI.getHostedEvent(new GetMultipleResponseCallback() {
            public void onDataReceived(List<Event> eventArrayList) {
                hostedEvent = eventArrayList;
                Log.d(TAG, hostedEvent.get(0).getTitle());
                Log.d(TAG, hostedEvent.get(1).getTitle());
            }

        }, 8);*/

    /*    //Matched event
        hostedEvent = new ArrayList<Event>();;
        restAPI.getMatchedEvent(new GetEventListCallback() {
            public void onEventListReceived(List<Event> eventArrayList) {
                hostedEvent = eventArrayList;
                Log.d(TAG, hostedEvent.get(0).getTitle());
                Log.d(TAG, hostedEvent.get(1).getTitle());
            }
            public void onUserListReceived(List<User> userArrayList){

            }

        }, 8);

    }

    private void updateFields(View rootView) {
        TextView titleView = (TextView) rootView.findViewById(R.id.event_title_view);
        TextView creatorView = (TextView) rootView.findViewById(R.id.event_creator_view);
        TextView startDateView = (TextView) rootView.findViewById(R.id.event_start_date_view);
        TextView endDateView = (TextView) rootView.findViewById(R.id.event_end_date_view);
        TextView addressView = (TextView) rootView.findViewById(R.id.event_address_view);
        TextView descriptionView = (TextView) rootView.findViewById(R.id.event_description_view);
        TextView participantView = (TextView) rootView.findViewById(R.id.listParticipantView);


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
                MainActivity.getUser(1).addMatchedEvent(mEvent);
                if(mEvent.addParticipant(MainActivity.getUser(1))) {
                    Toast.makeText(getActivity().getApplicationContext(), "Joined", Toast.LENGTH_SHORT).show();
                    if(mEvent.isFull()) {
                        joinEvent.setClickable(false);
                    }
                    mRestAPI.updateEvent(mEvent,new HttpResponseCodeCallback() {
                        @Override
                        public void onSuccess(String response) {
                            Log.d("EventFrag.upd.", "Response" + response);
                        }
                    });
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Sorry but this event just got completed by another guy.", Toast.LENGTH_SHORT).show();
                }
                getActivity().finish();
            }
        });
    }

    private void setTagExpandableList() {
        // get the list_view
        ExpandableListView mExpListView = (ExpandableListView) mRootView.findViewById(R.id.listParticipantExp);

        // preparing list data
        prepareListData();
        ExpendableList mListAdapter = new ExpendableList(getActivity().getApplicationContext(), mListDataHeader, mListDataChild);

        // setting list adapter
        mExpListView.setAdapter(mListAdapter);

    }

    private void prepareListData() {
        mListDataHeader = new ArrayList<String>();
        mListDataChild = new HashMap<String, List<String>>();

        // Adding child data
        mListDataHeader.add("Host of the event");
        mListDataHeader.add("Participant of the event (" + mEvent.getAllParticipant().size() + ")");


        // Adding child data
        String host = mEvent.getCreator();

        List<String> participant = new ArrayList<String>();
        for (User user: mEvent.getAllParticipant()) {
            participant.add(user.getUsername());
        }


        mListDataChild.put(mListDataHeader.get(0), new ArrayList<String>(Arrays.asList(host)));
        mListDataChild.put(mListDataHeader.get(1), participant);
    }*/
}
