package ch.epfl.sweng.evento;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import ch.epfl.sweng.evento.event.Event;
import ch.epfl.sweng.evento.gui.ConversationAdapter;

/**
 * Created by gautier on 30/11/2015.
 */
public class ConversationActivity extends AppCompatActivity {
    public static final String KEY_CURRENT_CONVERSATION = "current_event_conversation";

    private ConversationAdapter mConversationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        int currentId = EventDatabase.INSTANCE.getFirstEvent().getID();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currentId = bundle.getInt(KEY_CURRENT_CONVERSATION);
        }
        Event currentEvent = EventDatabase.INSTANCE.getEvent(currentId);

        TextView eventTitle = (TextView) findViewById(R.id.conversation_title);
        eventTitle.setText(currentEvent.getTitle());

        ListView conversationListView = (ListView) findViewById(R.id.conversation_list_comment);
        Conversation conversation = currentEvent.getConversation() ;
        if (conversation.size() == 0) { //TODO remove mock conversation
            conversation.addComment(new Comment(new MockUser(), "plop"));
            conversation.addComment(new Comment(new MockUser(), "blop"));
        }
        mConversationAdapter = new ConversationAdapter(this, conversation, currentId);

        conversationListView.setAdapter(mConversationAdapter);
    }
}
