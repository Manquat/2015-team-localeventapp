package ch.epfl.sweng.evento.gui.infinite_pager_adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.evento.Comment;
import ch.epfl.sweng.evento.Conversation;
import ch.epfl.sweng.evento.EventDatabase;
import ch.epfl.sweng.evento.MockUser;
import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.Settings;
import ch.epfl.sweng.evento.User;
import ch.epfl.sweng.evento.event.Event;
import ch.epfl.sweng.evento.gui.ConversationAdapter;
import ch.epfl.sweng.evento.rest_api.RestApi;
import ch.epfl.sweng.evento.rest_api.callback.HttpResponseCodeCallback;
import ch.epfl.sweng.evento.rest_api.network_provider.DefaultNetworkProvider;

/**
 * An infinite page adapter for the event activity
 */
public class EventInfinitePageAdapter extends InfinitePagerAdapter<Integer> implements View.OnClickListener {
    Activity mActivity;
    public static final String TAG = "EventInfinitePageAdapter";

    private Map<Integer, ListView> mListViews;
    private Map<Integer, Boolean> mCurrentlyAddingAComment;
    private Map<Integer, EditText> mMessagesBox;
    private List<User> mParticipants;
    private List<Event> hostedEvent;
    private RestApi mRestApi;

    public EventInfinitePageAdapter(Integer initialEventId, Activity activity) {
        super(initialEventId);

        mActivity = activity;
        mListViews = new HashMap<>();
        mCurrentlyAddingAComment = new HashMap<>();
        mMessagesBox = new HashMap<>();

        mRestApi = new RestApi(new DefaultNetworkProvider(), Settings.getServerUrl());
        //restApi.getParticipant(initialEventId);
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
            conversation.addComment(new Comment(new MockUser(), "plop"));
            conversation.addComment(new Comment(new MockUser(), "plop"));
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
        addCommentButton.setOnClickListener(this);

        listOfComment.addFooterView(addCommentButton);

        mListViews.put(currentEventId, listOfComment);

        return rootLayout;
    }

    private void updateFields(ViewGroup rootView, Event currentEvent) {
        TextView titleView = (TextView) rootView.findViewById(R.id.event_title_view);
        TextView creatorView = (TextView) rootView.findViewById(R.id.event_creator_view);
        TextView startDateView = (TextView) rootView.findViewById(R.id.event_start_date_view);
        TextView endDateView = (TextView) rootView.findViewById(R.id.event_end_date_view);
        TextView addressView = (TextView) rootView.findViewById(R.id.event_address_view);
        TextView descriptionView = (TextView) rootView.findViewById(R.id.event_description_view);

        titleView.setText(currentEvent.getTitle());
        creatorView.setText(currentEvent.getCreator());
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
                Toast.makeText(mActivity, "Submitted", Toast.LENGTH_SHORT).show();
                mActivity.finish();
            }
        });

        Button removeUserFromEvent = (Button) rootView.findViewById(R.id.remove_user_from_event);
        if(currentEvent.checkIfParticipantIsIn(Settings.INSTANCE.getUser())){
            removeUserFromEvent.setVisibility(View.VISIBLE);
        } else {
            removeUserFromEvent.setVisibility(View.INVISIBLE);
        }
        removeUserFromEvent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(mActivity.getApplicationContext(), "Removed from the event", Toast.LENGTH_SHORT).show();
                mRestApi.removeParticipant(currentEvent.getID(), Settings.INSTANCE.getUser().getUserId(), new HttpResponseCodeCallback() {
                    @Override
                    public void onSuccess(String response) {
                        Log.d(TAG, "Response" + response);
                    }
                });
                mActivity.finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int currentEventId = getCurrentIndicator();

        ListView listView = mListViews.get(currentEventId);
        listView.removeFooterView(v);

        if (mCurrentlyAddingAComment.get(currentEventId)) {
            EditText messageBox = mMessagesBox.get(currentEventId);
            String message = messageBox.getText().toString();

            // creating the new Comment
            //TODO use the restApi
            EventDatabase.INSTANCE.getEvent(currentEventId).getConversation()
                    .addComment(new Comment(new MockUser(), message));

            listView.removeFooterView(messageBox);
        }

        addingFooterOfTheListView(currentEventId);
    }

    private void addingFooterOfTheListView(int currentEventID) {
        Button addCommentButton = new Button(mActivity);
        boolean currentlyAddingAComment = mCurrentlyAddingAComment.get(currentEventID);

        if (currentlyAddingAComment) {
            addCommentButton.setText(mActivity.getResources().getString(R.string.conversation_add_comment));
        } else {
            EditText message = new EditText(mActivity);
            message.setHint(mActivity.getResources().getString(R.string.comment_message_hint));
            mListViews.get(currentEventID).addFooterView(message);

            mMessagesBox.put(currentEventID, message);

            addCommentButton.setText(mActivity.getResources().getString(R.string.event_validate));
        }

        mCurrentlyAddingAComment.put(currentEventID, !currentlyAddingAComment);
        addCommentButton.setOnClickListener(this);

        mListViews.get(currentEventID).addFooterView(addCommentButton);
    }
}
