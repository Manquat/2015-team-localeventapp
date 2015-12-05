package ch.epfl.sweng.evento.gui.infinite_pager_adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.evento.Comment;
import ch.epfl.sweng.evento.Conversation;
import ch.epfl.sweng.evento.EventDatabase;
import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.Settings;
import ch.epfl.sweng.evento.User;
import ch.epfl.sweng.evento.event.Event;
import ch.epfl.sweng.evento.gui.ConversationAdapter;
import ch.epfl.sweng.evento.gui.ExpendableList;
import ch.epfl.sweng.evento.gui.callback.AddingComment;
import ch.epfl.sweng.evento.gui.callback.JoinEvent;
import ch.epfl.sweng.evento.rest_api.RestApi;
import ch.epfl.sweng.evento.rest_api.callback.GetUserListCallback;
import ch.epfl.sweng.evento.rest_api.network_provider.DefaultNetworkProvider;

/**
 * An infinite page adapter for the event activity
 */
public class EventInfinitePageAdapter extends InfinitePagerAdapter<Integer> {
    Activity mActivity;
    public static final String TAG = "EventInfPageAdapter";

    private Map<Integer, ListView> mListViews;
    private Map<Integer, Boolean> mCurrentlyAddingAComment;
    private Map<Integer, EditText> mMessagesBox;
    private RestApi mRestApi;
    private Map<Integer, ExpandableListView> mExpandableListViews;
    private ArrayList<String> mListDataHeader;
    private HashMap<String, List<String>> mListDataChild;
    private List<User> mParticipants;

    public EventInfinitePageAdapter(Integer initialEventId, Activity activity) {
        super(initialEventId);

        mActivity = activity;
        mListViews = new HashMap<>();
        mCurrentlyAddingAComment = new HashMap<>();
        mMessagesBox = new HashMap<>();
        mExpandableListViews = new HashMap<>();

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

        Conversation conversation = event.getConversation();
        if (conversation.size() == 0) { //TODO remove mock conversation
            conversation.addComment(new Comment(Settings.INSTANCE.getUser(), "plop", -1));
            conversation.addComment(new Comment(Settings.INSTANCE.getUser(), "plop", -1));
        }

        ConversationAdapter conversationAdapter = new ConversationAdapter(mActivity, conversation);
        ListView listOfComment = (ListView) rootLayout.findViewById(R.id.event_list_comment);
        listOfComment.setAdapter(conversationAdapter);

        GridLayout layout = (GridLayout) inflater.inflate(R.layout.fragment_event,
                listOfComment, false);

        updateFields(layout, event);

        listOfComment.addHeaderView(layout);

        Button addCommentButton = new Button(mActivity);
        addCommentButton.setText(mActivity.getResources().getString(R.string.conversation_add_comment));
        mCurrentlyAddingAComment.put(currentEventId, false);
        addCommentButton.setTag(currentEventId);
        addCommentButton.setOnClickListener(new AddingComment(mActivity, listOfComment, addCommentButton));

        listOfComment.addFooterView(addCommentButton);

        mListViews.put(currentEventId, listOfComment);

        return rootLayout;
    }

    private void updateFields(ViewGroup rootView, final Event currentEvent) {
        TextView titleView = (TextView) rootView.findViewById(R.id.event_title_view);
        TextView creatorView = (TextView) rootView.findViewById(R.id.event_creator_view);
        TextView startDateView = (TextView) rootView.findViewById(R.id.event_start_date_view);
        TextView endDateView = (TextView) rootView.findViewById(R.id.event_end_date_view);
        TextView addressView = (TextView) rootView.findViewById(R.id.event_address_view);
        TextView descriptionView = (TextView) rootView.findViewById(R.id.event_description_view);

        mExpandableListViews.put(currentEvent.getID(), (ExpandableListView) rootView.findViewById(R.id.list_participant_exp));

        mListDataHeader = new ArrayList<String>();
        mListDataChild = new HashMap<String, List<String>>();

        getParticipant(currentEvent);

        titleView.setText(currentEvent.getTitle());
        creatorView.setText(Integer.toString(currentEvent.getCreator()));
        startDateView.setText(currentEvent.getStartDateAsString());
        endDateView.setText(currentEvent.getEndDateAsString());
        addressView.setText(currentEvent.getAddress());
        descriptionView.setText(currentEvent.getDescription());

        ImageView pictureView = (ImageView) rootView.findViewById(R.id.eventPictureView);
        pictureView.setImageBitmap(currentEvent.getPicture());

        // configure the joint and unjoin button
        Button joinEventButton = (Button) rootView.findViewById(R.id.joinEvent);
        Button unJoinEventButton = (Button) rootView.findViewById(R.id.remove_user_from_event);

       JoinEvent.initialize(mActivity, currentEvent.getID(), joinEventButton, unJoinEventButton);
    }

    private void getParticipant(final Event currentEvent){
        mParticipants = new ArrayList<>();

        mRestApi.getParticipant(new GetUserListCallback() {
            public void onUserListReceived(List<User> userArrayList) {
                if (userArrayList != null) {
                    mParticipants = userArrayList;
                    List<String> participant = new ArrayList<>();
                    for (User user : mParticipants) {
                        participant.add(user.getUsername());
                    }
                    if (mListDataHeader.size() < 2) {
                        mListDataHeader.add("Participant of the event (" + mParticipants.size() + ")");
                    }
                    mListDataChild.put(mListDataHeader.get(0), participant);
                    ExpendableList listAdapter = new ExpendableList(mActivity, mListDataHeader, mListDataChild);

                    // setting list adapter
                    mExpandableListViews.get(currentEvent.getID()).setAdapter(listAdapter);

                    // ListView on child click listener
                    mExpandableListViews.get(currentEvent.getID())
                            .setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

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

        }, currentEvent.getID());

    }
}
