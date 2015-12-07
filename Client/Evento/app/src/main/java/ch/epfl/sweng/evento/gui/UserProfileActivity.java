package ch.epfl.sweng.evento.gui;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.Settings;
import ch.epfl.sweng.evento.event.Event;
import ch.epfl.sweng.evento.gui.event_activity.EventActivity;
import ch.epfl.sweng.evento.rest_api.RestApi;
import ch.epfl.sweng.evento.rest_api.callback.GetEventListCallback;
import ch.epfl.sweng.evento.rest_api.network_provider.DefaultNetworkProvider;
import ch.epfl.sweng.evento.rest_api.network_provider.NetworkProvider;


/**
 * Created by Gaffinet on 30/11/2015.
 */
public class UserProfileActivity extends AppCompatActivity {


    private static final String TAG = "UserProfileActivity";

    private Activity mActivity;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_userprofile);

        //Show Name and Email on Profile page.
        TextView UsernameView = (TextView) (findViewById(R.id.Username));
        UsernameView.setText("Username : " + Settings.INSTANCE.getUser().getUsername());
        TextView EmailView = (TextView) (findViewById(R.id.Email));
        EmailView.setText("Email Address : " + Settings.INSTANCE.getUser().getEmail());


    }

}
