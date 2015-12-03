package ch.epfl.sweng.evento.gui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import ch.epfl.sweng.evento.Comment;
import ch.epfl.sweng.evento.Conversation;
import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.gui.CommentActivity;

/**
 * List view adapter for a conversation
 */
public class ConversationAdapter extends BaseAdapter {
    private Conversation mConversation;
    private Context mContext;
    private Button mAddCommentButton;
    private int mCurrentEventId;

    public ConversationAdapter(Context context, Conversation conversation, int currentEventId) {
        mContext = context;
        mConversation = conversation;
        mCurrentEventId = currentEventId;
    }

    @Override
    public int getCount() {
        return mConversation.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        if (position < mConversation.size()) {
            return mConversation.getComment(position);
        }
        else {
            return mAddCommentButton;
        }
    }

    @Override
    public long getItemId(int position) {
        if (position < mConversation.size()) {
            return mConversation.getComment(position).getID();
        }
        else {
            return 0;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position < mConversation.size()) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater.from(mContext));
                convertView = inflater.inflate(R.layout.list_comment, parent, false);
            }

            Comment comment = mConversation.getComment(position);

            TextView owner = (TextView) convertView.findViewById(R.id.list_comment_owner);
            owner.setText(comment.getOwner().getName());

            TextView message = (TextView) convertView.findViewById(R.id.list_comment_message);
            message.setText(comment.getMessage());
        }
        else {
            if (convertView == null) {
                mAddCommentButton = new Button(mContext);
                mAddCommentButton.setText(R.string.conversation_add_comment);
                mAddCommentButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, CommentActivity.class);
                        intent.putExtra(CommentActivity.KEY_CURRENT_COMMENT, mCurrentEventId);
                        mContext.startActivity(intent);
                    }
                });
                convertView = mAddCommentButton;
            }
        }

        return convertView;
    }
}
