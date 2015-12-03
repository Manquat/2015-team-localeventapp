package ch.epfl.sweng.evento;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.evento.list_view.ListEntryAdapter;

import ch.epfl.sweng.evento.event.Event;
import ch.epfl.sweng.evento.rest_api.RestApi;
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
        mActivity = this;
        mRestAPI = new RestApi(new DefaultNetworkProvider(), Settings.getServerUrl());
        mListView=(ListView)findViewById(R.id.listViewManage);

        mItems.add(new ListEntryAdapter.Section("Hosted Event"));
        for (Event event : mHostedEvent) {
            mItems.add(new ListEntryAdapter.Entry(event.getTitle(), event.getDescription()));
        }

        mItems.add(new ListEntryAdapter.Section("Matched Event"));
        for (Event event : mMatchedEvent) {
            mItems.add(new ListEntryAdapter.Entry(event.getTitle(), event.getDescription()));
        }

        ListEntryAdapter adapter = new ListEntryAdapter(this, mItems);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView arg0, View arg1, int position, long arg3) {

        ListEntryAdapter.Entry item = (ListEntryAdapter.Entry)mItems.get(position);
        Toast.makeText(this, "You clicked " + item.getTitle(), Toast.LENGTH_SHORT).show();
    }


}
