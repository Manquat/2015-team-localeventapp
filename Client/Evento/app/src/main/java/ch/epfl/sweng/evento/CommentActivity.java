package ch.epfl.sweng.evento;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import ch.epfl.sweng.evento.Events.Event;

/**
 * Created by gautier on 30/11/2015.
 */
public class CommentActivity extends AppCompatActivity {
    private ConversationAdapter mConversationAdpater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Event currentEvent = EventDatabase.INSTANCE.getFirstEvent();

        TextView eventTitle = (TextView) findViewById(R.id.conversation_title);
        eventTitle.setText(currentEvent.getTitle());

        ListView conversationListView = (ListView) findViewById(R.id.conversation_list_comment);
        Conversation conversation = new Conversation();
        conversation.addComment(new Comment(new MockUser(), "plop"));
        conversation.addComment(new Comment(new MockUser(), "blop"));
        mConversationAdpater = new ConversationAdapter(this, conversation);

        conversationListView.setAdapter(mConversationAdpater);
    }
}
