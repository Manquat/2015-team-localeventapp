package ch.epfl.sweng.evento.event;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


import ch.epfl.sweng.evento.EventDatabase;

import ch.epfl.sweng.evento.R;

import ch.epfl.sweng.evento.Settings;
import ch.epfl.sweng.evento.User;
import ch.epfl.sweng.evento.gui.MainActivity;
import ch.epfl.sweng.evento.rest_api.RestApi;
import ch.epfl.sweng.evento.rest_api.callback.GetEventListCallback;
import ch.epfl.sweng.evento.rest_api.callback.HttpResponseCodeCallback;
import ch.epfl.sweng.evento.rest_api.network_provider.DefaultNetworkProvider;

/**
 * Fragment that display an Event with an ID passed as an Extra with the key KEYCURRENTEVENT.
 * After that allow to swipe left or right to explore the events actually loaded.
 */
public class EventFragment extends Fragment {

    private static final String TAG = "EventFragment";
    public static final String KEYCURRENTEVENT = "CurrentEvent";
    private RestApi mRestAPI;
    private Event mEvent;
    private List<User> mParticipants;
    private List<Event> hostedEvent;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event, container, false);

        Bundle bundle = getArguments();
        int currentEventID = bundle.getInt(KEYCURRENTEVENT);
        mRestAPI = new RestApi(new DefaultNetworkProvider(), Settings.getServerUrl());
        mEvent = EventDatabase.INSTANCE.getEvent(currentEventID);

        //getParticipant(currentEventID);
        updateFields(rootView);

        return rootView;
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

        //Matched event
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
        participantView.setText(mEvent.getListParticipantString(", "));

        ImageView pictureView = (ImageView) rootView.findViewById(R.id.eventPictureView);
        pictureView.setImageBitmap(mEvent.getPicture());

        Button joinEvent = (Button) rootView.findViewById(R.id.joinEvent);
        joinEvent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(!mEvent.addParticipant(Settings.INSTANCE.getUser())) {
                    Log.d(TAG, "addParticipant just returned false");
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Joined", Toast.LENGTH_SHORT).show();
                    Settings.INSTANCE.getUser().addMatchedEvent(mEvent);
                    mRestAPI.addParticipant(mEvent.getID(), Settings.INSTANCE.getUser().getUserId(), new HttpResponseCodeCallback() {
                        @Override
                        public void onSuccess(String response) {
                            Log.d(TAG, "Response" + response);
                        }
                    });
                }
                getActivity().finish();
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
                Toast.makeText(getActivity().getApplicationContext(), "Removed from the event", Toast.LENGTH_SHORT).show();
                mRestAPI.removeParticipant(mEvent.getID(), Settings.INSTANCE.getUser().getUserId(), new HttpResponseCodeCallback() {
                    @Override
                    public void onSuccess(String response) {
                        Log.d(TAG, "Response" + response);
                    }
                });
                getActivity().finish();
            }
        });
    }
}
