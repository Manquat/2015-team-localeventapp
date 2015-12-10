package ch.epfl.sweng.evento.gui;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.evento.Settings;
import ch.epfl.sweng.evento.User;
import ch.epfl.sweng.evento.rest_api.RestApi;
import ch.epfl.sweng.evento.rest_api.callback.GetUserListCallback;
import ch.epfl.sweng.evento.rest_api.network_provider.DefaultNetworkProvider;
import ch.epfl.sweng.evento.tabs_fragment.Refreshable;

/**
 * Created by gautier on 08/12/2015.
 */
public class ListOfParticipantListener implements
        View.OnClickListener,
        GetUserListCallback,
        Refreshable
{
    private static final String PARTICIPANT = "Number of participants : ";
    private final Activity mActivity;
    private final RestApi mRestApi;
    private List<User> mParticipant;
    private TextView mParentListOfParent;
    private int mCurrentEventId;

    public ListOfParticipantListener(Activity parentActivity, int currentEventId, TextView parentListOfParticipant)
    {
        mActivity = parentActivity;
        mParentListOfParent = parentListOfParticipant;
        //mParentListOfParent.setClickable(true);
        mParentListOfParent.setOnClickListener(this);

        mRestApi = new RestApi(new DefaultNetworkProvider(), Settings.getServerUrl());

        mCurrentEventId = currentEventId;

        getParticipant();

        updateState();
    }

    private void updateState()
    {
        if (mParticipant == null || mParticipant.size() == 0)
        {
            mParentListOfParent.setEnabled(false);
            mParentListOfParent.setText(PARTICIPANT + 0);
        } else
        {
            mParentListOfParent.setEnabled(true);
            mParentListOfParent.setText(PARTICIPANT + mParticipant.size());
        }
    }

    private void getParticipant()
    {
        mRestApi.getParticipant(this, mCurrentEventId);
    }

    @Override
    public void onClick(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

        CharSequence[] participantName = new CharSequence[mParticipant.size()];
        for (int i = 0; i < mParticipant.size(); i++)
        {
            participantName[i] = mParticipant.get(i).getUsername();
        }

        builder.setTitle("Touch a participant to see his profile")
                .setItems(participantName, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                    }
                });
        builder.create().show();
    }

    @Override
    public void onUserListReceived(List<User> userArrayList)
    {
        mParticipant = userArrayList;
        updateState();
    }

    @Override
    public void refresh()
    {
        getParticipant();
    }
}
