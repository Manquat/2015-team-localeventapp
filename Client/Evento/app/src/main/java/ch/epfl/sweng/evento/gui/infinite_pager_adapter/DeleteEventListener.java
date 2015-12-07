package ch.epfl.sweng.evento.gui.infinite_pager_adapter;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import ch.epfl.sweng.evento.Settings;
import ch.epfl.sweng.evento.rest_api.RestApi;
import ch.epfl.sweng.evento.rest_api.callback.HttpResponseCodeCallback;
import ch.epfl.sweng.evento.rest_api.network_provider.DefaultNetworkProvider;

/**
 * Created by Tago on 07/12/2015.
 */
public class DeleteEventListener implements View.OnClickListener {
    private Activity mActivity;
    private int mCurrentEventId;
    private RestApi mRestApi;

    public DeleteEventListener(Activity activityParent, int currentEventId) {
        mActivity = activityParent;
        mCurrentEventId = currentEventId;

        mRestApi = new RestApi(new DefaultNetworkProvider(), Settings.getServerUrl());
    }

    @Override
    public void onClick(View v) {
        mRestApi.deleteEvent(mCurrentEventId, new HttpResponseCodeCallback() {
            @Override
            public void onSuccess(String httpResponseCode) {
                Toast.makeText(mActivity, "Successfully delete the event : " + httpResponseCode,
                        Toast.LENGTH_LONG).show();
            }
        });
        mActivity.finish();
    }
}
