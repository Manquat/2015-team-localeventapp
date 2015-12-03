package ch.epfl.sweng.evento.gui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ch.epfl.sweng.evento.Comment;
import ch.epfl.sweng.evento.Conversation;
import ch.epfl.sweng.evento.ConversationActivity;
import ch.epfl.sweng.evento.EventDatabase;
import ch.epfl.sweng.evento.MockUser;
import ch.epfl.sweng.evento.R;

public class CommentActivity extends AppCompatActivity {
    public static final String KEY_CURRENT_COMMENT = "current_event_id_for_comment";

    private int mCurrentEventId;
    private EditText mMessage;
    private Activity mActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        mActivity = this;

        mCurrentEventId = EventDatabase.INSTANCE.getFirstEvent().getID();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mCurrentEventId = bundle.getInt(KEY_CURRENT_COMMENT);
        }

        mMessage = (EditText) findViewById(R.id.comment_message);

        Button validateButton = (Button) findViewById(R.id.comment_validate_button);
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Conversation conversation =
                        EventDatabase.INSTANCE.getEvent(mCurrentEventId).getConversation();
                conversation.addComment(new Comment(new MockUser(), mMessage.getText().toString())); //TODO delete usage of mock user

                //TODO adding the send to the server
                Intent intent = new Intent(mActivity, ConversationActivity.class);
                intent.putExtra(ConversationActivity.KEY_CURRENT_CONVERSATION, mCurrentEventId);
                startActivity(intent);
            }
        });
    }
}
