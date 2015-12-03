package ch.epfl.sweng.evento.gui.infinite_pager_adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import ch.epfl.sweng.evento.Comment;
import ch.epfl.sweng.evento.Conversation;
import ch.epfl.sweng.evento.MockUser;
import ch.epfl.sweng.evento.gui.ConversationActivity;
import ch.epfl.sweng.evento.EventDatabase;
import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.event.Event;
import ch.epfl.sweng.evento.gui.ConversationAdapter;

/**
 * An infinite page adapter for the event activity
 */
public class EventInfinitePageAdapter extends InfinitePagerAdapter<Integer> {
    Activity mActivity;

    public EventInfinitePageAdapter(Integer initialEventSignature, Activity activity) {
        super(initialEventSignature);

        mActivity = activity;
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
        ScrollView layout = (ScrollView) inflater.inflate(R.layout.fragment_event,
                (ViewGroup) mActivity.getWindow().getDecorView().getRootView(), false);

        updateFields(layout, event);

        return layout;
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

        Conversation conversation = currentEvent.getConversation();
        if (conversation.size() == 0) { //TODO remove mock conversation
            conversation.addComment(new Comment(new MockUser(), "plop"));
            conversation.addComment(new Comment(new MockUser(), "blop"));
        }

        ConversationAdapter conversationAdapter = new ConversationAdapter(mActivity, conversation,
                getCurrentIndicator());
        ListView listView = (ListView) rootView.findViewById(R.id.event_list_comment);
        listView.setAdapter(conversationAdapter);

/*
        Button loadMoreComment = (Button) rootView.findViewById(R.id.event_conversation);
        loadMoreComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ConversationActivity.class);
                intent.putExtra(ConversationActivity.KEY_CURRENT_CONVERSATION, getCurrentIndicator());
                mActivity.startActivity(intent);
            }
        });*/
    }
}
