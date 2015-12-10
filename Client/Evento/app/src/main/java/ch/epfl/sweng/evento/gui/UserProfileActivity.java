package ch.epfl.sweng.evento.gui;


import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.Settings;
import ch.epfl.sweng.evento.event.Event;
import ch.epfl.sweng.evento.rest_api.RestApi;
import ch.epfl.sweng.evento.rest_api.network_provider.DefaultNetworkProvider;


/**
 * Created by Gaffinet on 30/11/2015.
 */
public class UserProfileActivity extends AppCompatActivity {


    private static final String TAG = "UserProfileActivity";

    private Activity mActivity;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_userprofile);


        //TODO Get users hosted events and events he participates in, save in settings and show down below
        int UserId = Settings.getUser().getUserId();
        List<Event> hostedEvents;
        List<Event> matchedEvents;
        RestApi restApi = new RestApi(new DefaultNetworkProvider(), Settings.getServerUrl());

       /* restApi.getHostedEvent(new GetEventListCallback(), UserId ){
            public void onEventListReceived(List<Event> hostedEvents){
                        hostedEvents = null;
            }
            public void onUserListReceived(List<User> userArrayList){

            }
        }*/

        Settings.getUser().getHostedEvent();
        Settings.getUser().getMatchedEvent();

        TextView UsernameView = (TextView) (findViewById(R.id.Username));
        UsernameView.setText("Username : " + Settings.getUser().getUsername());
        TextView EmailView = (TextView) (findViewById(R.id.Email));
        EmailView.setText("Email Adress : " + Settings.getUser().getEmail());


    }

}
