package ch.epfl.sweng.evento.gui.callback;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.Settings;
import ch.epfl.sweng.evento.rest_api.RestApi;
import ch.epfl.sweng.evento.rest_api.callback.HttpResponseCodeCallback;
import ch.epfl.sweng.evento.rest_api.network_provider.DefaultNetworkProvider;

/**
 * Created by gautier on 04/12/2015.
 */
public class AddingComment implements OnClickListener {
    private Activity mActivity;
    private ListView mListView;
    private boolean mCurrentlyAddingAComment;
    private EditText mMessageBox;
    private Button mAddCommentButton;
    private RestApi mRestApi;
    private int mCurrentEventId;

    private AddingComment(Activity parentActivity, ListView commentListView, int currentEventId) {
        mActivity = parentActivity;
        mListView = commentListView;
        mCurrentlyAddingAComment = false;
        mCurrentEventId = currentEventId;


        //initialize the button add comment
        mAddCommentButton = new Button(mActivity);
        mAddCommentButton.setText(mActivity.getResources()
                .getString(R.string.conversation_add_comment));
        mAddCommentButton.setOnClickListener(this);

        mListView.addFooterView(mAddCommentButton);


        mRestApi = new RestApi(new DefaultNetworkProvider(), Settings.getServerUrl());
    }

    public static void initialize(Activity parentActivity, ListView commentListView,
                                  int currentEventId) {
        new AddingComment(parentActivity, commentListView, currentEventId);
    }

    @Override
    public void onClick(View v) {
        mListView.removeFooterView(v);

        if (mCurrentlyAddingAComment) {
            String message = mMessageBox.getText().toString();

            // creating the new Comment
            mRestApi.postComment(mCurrentEventId, message, new HttpResponseCodeCallback(){
                @Override
                public void onSuccess(String httpResponseCode) {
                    Toast.makeText(mActivity, "Success on posting the comment", Toast.LENGTH_LONG)
                            .show();
                }
            });

            mListView.removeFooterView(mMessageBox);
            mActivity.finish();
        }

        addingFooterOfTheListView();
    }

    private void addingFooterOfTheListView() {
        if (mCurrentlyAddingAComment) {
            mAddCommentButton.setText(mActivity.getResources().getString(R.string.conversation_add_comment));
        } else {
            EditText message = new EditText(mActivity);
            message.setHint(mActivity.getResources().getString(R.string.comment_message_hint));
            mListView.addFooterView(message);

            mMessageBox =  message;

            mAddCommentButton.setText(mActivity.getResources().getString(R.string.event_validate));
        }

        mCurrentlyAddingAComment = !mCurrentlyAddingAComment;
        mAddCommentButton.setOnClickListener(this);

        mListView.addFooterView(mAddCommentButton);
    }
}
