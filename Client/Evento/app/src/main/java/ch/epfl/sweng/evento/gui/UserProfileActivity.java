package ch.epfl.sweng.evento.gui;


import android.content.Context;
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
        final ArrayList<String> matchedList = new ArrayList<String>();
        matchedList.add("Your joined events: ");
        for (int i = 0; i < Settings.INSTANCE.getUser().getMatchedEvent().size(); ++i) {
            matchedList.add(Settings.INSTANCE.getUser().getMatchedEvent().get(i).getTitle());
        }

        final StableArrayAdapter matchedAdapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, matchedList);
        matchedListView.setAdapter(matchedAdapter);


        final ListView hostedListView = (ListView) findViewById(R.id.hostedListView);
        final ArrayList<String> hostedList = new ArrayList<String>();
        hostedList.add("Your own events: ");
        for (int i = 0; i < Settings.INSTANCE.getUser().getHostedEvent().size(); ++i) {
            hostedList.add(Settings.INSTANCE.getUser().getHostedEvent().get(i).getTitle());
        }

        final StableArrayAdapter hostedAdapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, hostedList);
        hostedListView.setAdapter(hostedAdapter);


        TextView UsernameView = (TextView) (findViewById(R.id.Username));
        UsernameView.setText("Username : " + Settings.INSTANCE.getUser().getUsername());
        TextView EmailView = (TextView) (findViewById(R.id.Email));
        EmailView.setText("Email Adress : " + Settings.INSTANCE.getUser().getEmail());


        /*hostedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(2000).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                hostedList.remove(item);
                                hostedAdapter.notifyDataSetChanged();
                                view.setAlpha(1);
                            }
                        });
            }

        });*/
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
