package ch.epfl.sweng.evento.gui;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;
import java.util.Set;

import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.Settings;
import ch.epfl.sweng.evento.User;
import ch.epfl.sweng.evento.event.Event;
import ch.epfl.sweng.evento.rest_api.RestApi;
import ch.epfl.sweng.evento.rest_api.callback.GetEventListCallback;
import ch.epfl.sweng.evento.rest_api.network_provider.DefaultNetworkProvider;
import ch.epfl.sweng.evento.rest_api.network_provider.NetworkProvider;


/**
 * Created by Gaffinet on 30/11/2015.
 */
public class UserProfileActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {


    private static final String TAG = "UserProfileActivity";



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_userprofile);


        //TODO Get users hosted events and events he participates in, save in settings and show down below
        int UserId = Settings.INSTANCE.getUser().getUserId();

        RestApi restApi = new RestApi(new DefaultNetworkProvider(), Settings.getServerUrl());

/*        restApi.getHostedEvent(new GetEventListCallback() {
            @Override
            public void onEventListReceived(List<Event> eventArrayList) {
                Settings.INSTANCE.getUser().setHostedEvent(eventArrayList);
            }
        },  UserId);

        restApi.getMatchedEvent((new GetEventListCallback() {
            @Override
            public void onEventListReceived(List<Event> eventArrayList) {
                Settings.INSTANCE.getUser().setMatchedEvent(eventArrayList);
            }
        }), UserId);*/


/*        Event mockevent1 = null;
        Event mockevent2 = null;
        mockevent1.setTitle("Beerpong");
        mockevent2.setTitle("Pingpong");
        Set<Event> mocklist = null;
        mocklist.add(mockevent1);
        mocklist.add(mockevent2);
        Set<Event> mocklist2 = mocklist;
        mocklist2.add(mockevent1);

        ListView HostedEvents = null;
        ListView MatchedEvents;
        ListAdapter Idontknow;
        HostedEvents.setAdapter(Idontknow);*/


        TextView UsernameView = (TextView) (findViewById(R.id.Username));
        UsernameView.setText("Username : " + Settings.INSTANCE.getUser().getUsername() );
        TextView EmailView = (TextView) (findViewById(R.id.Email));
        EmailView.setText("Email Adress : " + Settings.INSTANCE.getUser().getEmail() );

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
