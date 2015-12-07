package ch.epfl.sweng.evento.gui.infinite_pager_adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.epfl.sweng.evento.EventDatabase;
import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.Settings;
import ch.epfl.sweng.evento.User;
import ch.epfl.sweng.evento.event.Event;
import ch.epfl.sweng.evento.gui.ConversationAdapter;
import ch.epfl.sweng.evento.gui.ExpendableList;
import ch.epfl.sweng.evento.gui.event_activity.AddingComment;
import ch.epfl.sweng.evento.gui.event_activity.JoinEvent;
import ch.epfl.sweng.evento.rest_api.RestApi;
import ch.epfl.sweng.evento.rest_api.callback.GetUserCallback;
import ch.epfl.sweng.evento.rest_api.callback.GetUserListCallback;
import ch.epfl.sweng.evento.rest_api.network_provider.DefaultNetworkProvider;

/**
 * An infinite page adapter for the event activity
 */
public class EventInfinitePageAdapter extends InfinitePagerAdapter<Integer> {
    Activity mActivity;
    public static final String TAG = "EventInfPageAdapter";
    private ArrayList<String> mListDataHeader;
    private HashMap<String, List<String>> mListDataChild;
    View mRootView;
    private List<User> mParticipants;
    private List<Event> hostedEvent;
    private ExpandableListView mExpListView;
    private RestApi mRestApi;
    private ConversationAdapter mConversationAdapter;


    public EventInfinitePageAdapter(Integer initialEventId, Activity activity) {
        super(initialEventId);
        mActivity = activity;
        mRestApi = new RestApi(new DefaultNetworkProvider(), Settings.getServerUrl());
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
        LinearLayout rootLayout = (LinearLayout) inflater.inflate(R.layout.event_adapter,
                (ViewGroup) mActivity.getWindow().getDecorView().getRootView(), false);

        mConversationAdapter = new ConversationAdapter(mActivity, currentEventId);
        ListView listOfComment = (ListView) rootLayout.findViewById(R.id.event_list_comment);

        listOfComment.setAdapter(mConversationAdapter);


        GridLayout layout = (GridLayout) inflater.inflate(R.layout.fragment_event,
                listOfComment, false);

        updateFields(layout, event);

        listOfComment.addHeaderView(layout);

        AddingComment.initialize(mActivity, listOfComment, currentEventId, mConversationAdapter);


        return rootLayout;
    }

    private void getHost(Event event, View view){
        final View innerView = view;

        mRestApi.getUser(new GetUserCallback() {
            public void onDataReceived(User user) {
                TextView creatorView = (TextView) innerView.findViewById(R.id.event_creator_view);
                creatorView.setText(user.getUsername());

            }
        }, event.getCreator());
    }


    private void updateFields(ViewGroup rootView, final Event currentEvent) {
        TextView titleView = (TextView) rootView.findViewById(R.id.event_title_view);
        TextView creatorView = (TextView) rootView.findViewById(R.id.event_creator_view);
        TextView startDateView = (TextView) rootView.findViewById(R.id.event_start_date_view);
        TextView endDateView = (TextView) rootView.findViewById(R.id.event_end_date_view);
        TextView addressView = (TextView) rootView.findViewById(R.id.event_address_view);
        TextView descriptionView = (TextView) rootView.findViewById(R.id.event_description_view);

        getHost(currentEvent, rootView);

        titleView.setText(currentEvent.getTitle());
        creatorView.setText(Integer.toString(currentEvent.getCreator()));
        startDateView.setText(currentEvent.getStartDateAsString());
        endDateView.setText(currentEvent.getEndDateAsString());
        addressView.setText(currentEvent.getAddress());
        descriptionView.setText(currentEvent.getDescription());

        ImageView pictureView = (ImageView) rootView.findViewById(R.id.eventPictureView);
        pictureView.setImageBitmap(currentEvent.getPicture());
        mExpListView = (ExpandableListView) rootView.findViewById(R.id.list_participant_exp);
        prepareListData();
        ExpendableList mListAdapter = new ExpendableList(mActivity.getApplicationContext(), mListDataHeader, mListDataChild);

        // setting list adapter
        mExpListView.setAdapter(mListAdapter);
        mExpListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConversationAdapter.refresh();
            }
        });

        // configure the joint and unjoin button
        //Button joinEventButton = (Button) rootView.findViewById(R.id.joinEvent);
        //Button unJoinEventButton = (Button) rootView.findViewById(R.id.remove_user_from_event);

        //JoinEvent.initialize(mActivity, currentEvent.getID(), joinEventButton, unJoinEventButton,
             //   mExpListView);
    }

    private void prepareListData() {
        mListDataHeader = new ArrayList<String>();
        mListDataChild = new HashMap<String, List<String>>();

        // Adding child data
        mListDataHeader.add("Participant of the event");

        // Adding child data
        List<String> participant = new ArrayList<String>();
        participant.add("dff");
        participant.add("ddf");
        participant.add("dfdf");
        participant.add(" ");
        participant.add(" ");
        participant.add(" ");
        participant.add(" ");
        participant.add(" ");


        mListDataChild.put(mListDataHeader.get(0), participant);
    }
}
