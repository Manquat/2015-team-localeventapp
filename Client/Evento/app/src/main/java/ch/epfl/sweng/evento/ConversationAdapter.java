package ch.epfl.sweng.evento;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * List view adapter for a conversation
 */
public class ConversationAdapter extends BaseAdapter {
    private Conversation mConversation;
    private Context mContext;

    public ConversationAdapter(Context context, Conversation conversation) {
        mContext = context;
        mConversation = conversation;
    }

    @Override
    public int getCount() {
        return mConversation.size();
    }

    @Override
    public Object getItem(int position) {
        return mConversation.getComment(position);
    }

    @Override
    public long getItemId(int position) {
        return mConversation.getComment(position).getID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.list_comment, parent);
        }

        return convertView;
    }
}
