package ch.epfl.sweng.evento.gui;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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


        //TODO Get users hosted events, save in settings and show down below
        int UserId = Settings.INSTANCE.getUser().getUserId();

        RestApi restApi = new RestApi(new DefaultNetworkProvider(), Settings.getServerUrl());

        restApi.getHostedEvent(new GetEventListCallback() {
            @Override
            public void onEventListReceived(List<Event> eventArrayList) {
                Settings.INSTANCE.getUser().setHostedEvent(eventArrayList);
            }
        }, UserId);

        restApi.getMatchedEvent((new GetEventListCallback() {
            @Override
            public void onEventListReceived(List<Event> eventArrayList) {
                Settings.INSTANCE.getUser().setMatchedEvent(eventArrayList);
            }
        }), UserId);


        final ListView matchedListView = (ListView) findViewById(R.id.matchedListView);
        final ArrayList<String> list = new ArrayList<String>();
        list.add("Your joined events: ");
        for (int i = 0; i < Settings.INSTANCE.getUser().getMatchedEvent().size(); ++i) {
            list.add(Settings.INSTANCE.getUser().getMatchedEvent().get(i).getTitle());
        }

        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        matchedListView.setAdapter(adapter);

        final ListView hostedListView = (ListView) findViewById(R.id.hostedListView);
        for (int i = 0; i < Settings.INSTANCE.getUser().getHostedEvent().size(); ++i) {
            list.add(Settings.INSTANCE.getUser().getHostedEvent().get(i).getTitle());
        }
        hostedListView.setAdapter(adapter);


        TextView UsernameView = (TextView) (findViewById(R.id.Username));
        UsernameView.setText("Username : " + Settings.INSTANCE.getUser().getUsername() );
        TextView EmailView = (TextView) (findViewById(R.id.Email));
        EmailView.setText("Email Adress : " + Settings.INSTANCE.getUser().getEmail() );

    }



    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }



    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

}
