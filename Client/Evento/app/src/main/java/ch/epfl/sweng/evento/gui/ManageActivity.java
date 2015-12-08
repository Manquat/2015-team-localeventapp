package ch.epfl.sweng.evento.gui;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.Settings;
import ch.epfl.sweng.evento.User;
import ch.epfl.sweng.evento.gui.event_activity.EventActivity;
import ch.epfl.sweng.evento.list_view.ListEntryAdapter;

import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.Settings;
import ch.epfl.sweng.evento.event.Event;
import ch.epfl.sweng.evento.rest_api.RestApi;
import ch.epfl.sweng.evento.rest_api.callback.GetEventListCallback;
import ch.epfl.sweng.evento.rest_api.callback.GetUserCallback;
import ch.epfl.sweng.evento.rest_api.callback.GetUserListCallback;
import ch.epfl.sweng.evento.rest_api.callback.HttpResponseCodeCallback;
import ch.epfl.sweng.evento.rest_api.network_provider.DefaultNetworkProvider;

public class ManageActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "ManageActivity";
    private ArrayList<ListEntryAdapter.Item> mItems = new ArrayList<ListEntryAdapter.Item>();
    private ListView mListView;
    private RestApi mRestAPI;
    private List<Event> mHostedEvent;
    private List<Event> mMatchedEvent;
    private Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        TextView WelcomeView = (TextView) (findViewById(R.id.Welcome));
        WelcomeView.setText("Welcome " + Settings.getUser().getUsername() + ".");

        mActivity = this;
        mRestAPI = new RestApi(new DefaultNetworkProvider(), Settings.getServerUrl());
        mListView=(ListView)findViewById(R.id.listViewManage);

        mHostedEvent = new ArrayList<Event>();;
        mItems.add(new ListEntryAdapter.Section("Hosted Events"));
        mRestAPI.getHostedEvent(new GetEventListCallback() {
            public void onEventListReceived(List<Event> eventArrayList) {
                if (eventArrayList != null) {
                    mHostedEvent = eventArrayList;
                    int i=1;
                    for (Event event : mHostedEvent) {
                        mItems.add(new ListEntryAdapter.Entry(Integer.toString(i++) + "/ " + event.getTitle(), "        " + event.getDescription()));
                    }
                    mItems.add(new ListEntryAdapter.Section("Joined Events"));
                    mRestAPI.getMatchedEvent(new GetEventListCallback() {
                        public void onEventListReceived(List<Event> eventArrayList) {
                            if (eventArrayList != null) {
                                mMatchedEvent = eventArrayList;
                                int i = 1;
                                for (Event event : mMatchedEvent) {
                                    mItems.add(new ListEntryAdapter.Entry(Integer.toString(i++) + "/ " +event.getTitle(), "        " + event.getDescription()));
                                }
                                ListEntryAdapter adapter = new ListEntryAdapter(mActivity, mItems);
                                mListView.setAdapter(adapter);
                            }
                        }

                    }, Settings.getUser().getUserId());

                    ListEntryAdapter adapter = new ListEntryAdapter(mActivity, mItems);
                    mListView.setAdapter(adapter);
                }
            }

        }, Settings.getUser().getUserId());

        mListView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView arg0, View arg1, int position, long arg3) {

        ListEntryAdapter.Entry item = (ListEntryAdapter.Entry) mItems.get(position);
        Toast.makeText(this, "You clicked " + item.getTitle(), Toast.LENGTH_SHORT).show();
        final int innerPosition = position;
        final Intent intent = new Intent(mActivity, EventActivity.class);

        // Create custom dialog object
        final Dialog dialog = new Dialog(mActivity);
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialog_manage);
        // Set dialog title
        dialog.setTitle(item.getTitle());

        // set values for custom dialog components - text, image and button
        final TextView text = (TextView) dialog.findViewById(R.id.text_manage_dialog);
        ImageView image = (ImageView) dialog.findViewById(R.id.image_dialog);
        image.setImageResource(R.drawable.football);

        dialog.show();

        Button addUserButton = (Button) dialog.findViewById(R.id.add_user);
        // if decline button is clicked, close the custom dialog
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRestAPI.getUserByName(new GetUserCallback() {
                    @Override
                    public void onDataReceived(User user) {
                        if (user != null) {
                            int id = 0;
                            if (innerPosition < mHostedEvent.size() + 1) {
                                id = mHostedEvent.get(innerPosition - 1).getID();
                                Log.d(TAG, Integer.toString(id));
                                mRestAPI.addParticipant(id, user.getUserId(), new HttpResponseCodeCallback() {
                                    @Override
                                    public void onSuccess(String response) {
                                        Log.d(TAG, "Response" + response);
                                        dialog.dismiss();
                                    }
                                });
                            } else {
                                Toast.makeText(mActivity.getApplicationContext(), "You can't add a user if you are not the host !", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(mActivity.getApplicationContext(), "No user called " + text.getText().toString().replace(" ", "%20"), Toast.LENGTH_LONG).show();
                        }
                    }
                }, text.getText().toString().replace(" ", "%20"));

            }
        });

        Button getEventPageButton = (Button) dialog.findViewById(R.id.get_event_page);
        // if decline button is clicked, close the custom dialog
        getEventPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (innerPosition < mHostedEvent.size() + 1) {
                    intent.putExtra(EventActivity.CURRENT_EVENT_KEY, mHostedEvent.get(innerPosition - 1).getID());
                }
                if (innerPosition > mHostedEvent.size() + 1) {
                    intent.putExtra(EventActivity.CURRENT_EVENT_KEY, mMatchedEvent.get(innerPosition - mHostedEvent.size() - 2).getID());
                }

                mActivity.startActivity(intent);
                mActivity.finish();
            }
        });


    }


}
