package ch.epfl.sweng.evento.gui.event_activity;

import android.app.Activity;
import android.util.Log;
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
import ch.epfl.sweng.evento.tabs_fragment.Refreshable;

/**
 * Class that handle the commenting part in the event activity
 */
public class AddingComment implements OnClickListener, Refreshable {
    private static final String TAG = "AddingComment";
    private static final String SUCCESSFULLY_POST_COMMENT = "201";
    private Activity mActivity;
    private ListView mListView;
    private boolean mCurrentlyAddingAComment;
    private EditText mMessageBox;
    private Button mAddCommentButton;
    private RestApi mRestApi;
    private int mCurrentEventId;
    private Refreshable mRefreshableParent;

    private AddingComment(Activity parentActivity, ListView commentListView, int currentEventId,
                          Refreshable refreshableParent) {
        mActivity = parentActivity;
        mListView = commentListView;
        mCurrentlyAddingAComment = false;
        mCurrentEventId = currentEventId;
        mRefreshableParent = refreshableParent;


        //initialize the button add comment
        mAddCommentButton = new Button(mActivity);
        mAddCommentButton.setText(mActivity.getResources()
                .getString(R.string.conversation_add_comment));
        mAddCommentButton.setOnClickListener(this);

        mListView.addFooterView(mAddCommentButton);


        mRestApi = new RestApi(new DefaultNetworkProvider(), Settings.getServerUrl());
    }

    public static void initialize(Activity parentActivity, ListView commentListView,
                                  int currentEventId, Refreshable refreshableParent) {
        new AddingComment(parentActivity, commentListView, currentEventId, refreshableParent);
    }

    @Override
    public void onClick(View v) {
        mListView.removeFooterView(v);

        if (mCurrentlyAddingAComment) {
            String message = mMessageBox.getText().toString();

            // creating the new Comment
            if (!message.isEmpty()) {
                mRestApi.postComment(mCurrentEventId, message, new HttpResponseCodeCallback() {
                    @Override
                    public void onSuccess(String httpResponseCode) {
                        if (httpResponseCode != null && httpResponseCode.equals(SUCCESSFULLY_POST_COMMENT)) {
                            Log.d(TAG, "Successful post the comment : " + httpResponseCode);
                            Toast.makeText(mActivity, "Success on posting the comment", Toast.LENGTH_LONG)
                                    .show();
                            refresh();
                        } else {
                            Log.e(TAG, "Error while posting the comment, error code : " + httpResponseCode);
                            Toast.makeText(mActivity, "Error while posting the comment", Toast.LENGTH_LONG)
                                    .show();
                            mActivity.finish();
                        }
                    }
                });
            }

            mListView.removeFooterView(mMessageBox);
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

            mMessageBox = message;

            mAddCommentButton.setText(mActivity.getResources().getString(R.string.event_validate));
        }

        mCurrentlyAddingAComment = !mCurrentlyAddingAComment;
        mAddCommentButton.setOnClickListener(this);

        mListView.addFooterView(mAddCommentButton);
    }

    @Override
    public void refresh() {
        mRefreshableParent.refresh();
    }
}
