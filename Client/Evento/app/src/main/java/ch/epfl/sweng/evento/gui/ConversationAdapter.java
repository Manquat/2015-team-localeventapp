package ch.epfl.sweng.evento.gui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.evento.Comment;
import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.Settings;
import ch.epfl.sweng.evento.rest_api.RestApi;
import ch.epfl.sweng.evento.rest_api.callback.GetCommentListCallback;
import ch.epfl.sweng.evento.rest_api.network_provider.DefaultNetworkProvider;
import ch.epfl.sweng.evento.tabs_fragment.Refreshable;

/**
 * List view adapter for a conversation
 */
public class ConversationAdapter extends BaseAdapter
        implements Refreshable, GetCommentListCallback
{
    private static final String TAG = "ConversationAdpt";

    private int mCurrentEventId;
    private List<Comment> mListOfComment;
    private Context mContext;

    private RestApi mRestApi;

    public ConversationAdapter(Context context, int currentEventId)
    {
        mContext = context;
        mCurrentEventId = currentEventId;

        mRestApi = new RestApi(new DefaultNetworkProvider(), Settings.getServerUrl());

        mListOfComment = new ArrayList<>();

        refresh();
    }

    @Override
    public int getCount()
    {
        return mListOfComment.size();
    }

    @Override
    public Object getItem(int position)
    {
        return mListOfComment.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return mListOfComment.get(position).getID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater.from(mContext));
            convertView = inflater.inflate(R.layout.list_comment, parent, false);
        }

        Comment comment = mListOfComment.get(position);

        TextView owner = (TextView) convertView.findViewById(R.id.list_comment_owner);
        owner.setText(comment.getCreatorName());

        TextView message = (TextView) convertView.findViewById(R.id.list_comment_message);
        message.setText(comment.getMessage());

        return convertView;
    }

    @Override
    public void refresh()
    {
        mRestApi.getComment(mCurrentEventId, this);
    }

    @Override
    public void onCommentListReceived(List<Comment> commentList)
    {
        if (commentList != null)
        {
            mListOfComment = commentList;
            notifyDataSetChanged();
        } else
        {
            //Toast.makeText(mContext, "Error while parsing the comment(s)", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "RestAPI return a null list");
        }
    }
}
