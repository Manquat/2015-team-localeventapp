package ch.epfl.sweng.evento;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.evento.Events.Event;
import ch.epfl.sweng.evento.ListView.Adapter;
import ch.epfl.sweng.evento.ListView.Entry;
import ch.epfl.sweng.evento.ListView.Item;
import ch.epfl.sweng.evento.ListView.Section;
import ch.epfl.sweng.evento.RestApi.GetMultipleResponseCallback;
import ch.epfl.sweng.evento.RestApi.RestApi;

public class ManageActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "ManageActivity";
    private ArrayList<Item> mItems = new ArrayList<Item>();
    private ListView mListView;
    private RestApi mRestAPI;
    private List<Event> mHostedEvent;
    private List<Event> mMatchedEvent;
    private SeparatedListAdapter mAdapter;
    private Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        mActivity = this;
        mRestAPI = new RestApi(new DefaultNetworkProvider(), Settings.getServerUrl());
        mListView=(ListView)findViewById(R.id.listViewManage);

        mItems.add(new Section("Hosted Event"));
        for (Event event : mHostedEvent) {
            mItems.add(new Entry(event.getTitle(), event.getDescription()));
        }

        mItems.add(new Section("Matched Event"));
        for (Event event : mMatchedEvent) {
            mItems.add(new Entry(event.getTitle(), event.getDescription()));
        }
        /*//Hosted event
        mHostedEvent = new ArrayList<Event>();;
        mRestAPI.getHostedEvent(new GetMultipleResponseCallback() {
            public void onDataReceived(List<Event> eventArrayList) {
                mHostedEvent = eventArrayList;
                List<Map<String, ?>> hostedEvent = new LinkedList<Map<String, ?>>();
                for (Event event : mHostedEvent) {
                    hostedEvent.add(createItem(event.getTitle(), event.getDescription()));
                }

            }

        }, 8);

        //Matched event
        mMatchedEvent = new ArrayList<Event>();;
        mRestAPI.getMatchedEvent(new GetMultipleResponseCallback() {
            public void onDataReceived(List<Event> eventArrayList) {
                mMatchedEvent = eventArrayList;
                List<Map<String, ?>> matchedEvent = new LinkedList<Map<String, ?>>();
                for (Event event : mMatchedEvent) {
                    matchedEvent.add(createItem(event.getTitle(), event.getDescription()));
                }

            }


        }, 8);  */

        Adapter adapter = new Adapter(this, mItems);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView arg0, View arg1, int position, long arg3) {

        Entry item = (Entry)mItems.get(position);
        Toast.makeText(this, "You clicked " + item.getTitle(), Toast.LENGTH_SHORT).show();
    }
    /*public final static String ITEM_TITLE = "title";
    public final static String ITEM_CAPTION = "caption";

    public Map<String,String> createItem(String title, String caption) {
        Map<String,String> item = new HashMap<String,String>();
        item.put(ITEM_TITLE, title);
        item.put(ITEM_CAPTION, caption);
        return item;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Map<String,String>> hostedEvent = new LinkedList<Map<String,String>>();
        for(Event event: MainActivity.getUser(1).getHostedEvent()){
            hostedEvent.add(createItem(event.getTitle(), event.getDescription()));
        }

        List<Map<String,String>> matchedEvent = new LinkedList<Map<String,String>>();
        for(Event event: MainActivity.getUser(1).getMatchedEvent()){
            matchedEvent.add(createItem(event.getTitle(), event.getDescription()));
        }

        // create our list and custom adapter
        SeparatedListAdapter adapter = new SeparatedListAdapter(this);
        /*adapter.addSection("Hosted Event", new ArrayAdapter<String>(this,
                R.layout.list_item_manage_entry, hostedEvent));*/
     /*   adapter.addSection("Hosted Event", new SimpleAdapter(this, hostedEvent, R.layout.list_item_manage_section,
                new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.list_complex_title, R.id.list_complex_caption }));
        adapter.addSection("Matched Event", new SimpleAdapter(this, matchedEvent, R.layout.list_item_manage_section,
                new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.list_complex_title, R.id.list_complex_caption }));

        ListView list = new ListView(this);
        list.setAdapter(adapter);
        this.setContentView(list);

    }*/


    /*private TextView mMatchedEvent;
    private TextView mHostedEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        mMatchedEvent = (TextView) findViewById(R.id.matchedEvent);
        mHostedEvent = (TextView) findViewById(R.id.hostedEvent);
        mMatchedEvent.setText(MainActivity.getUser(1).getMatchedEventString());
        mHostedEvent.setText(MainActivity.getUser(1).getHostedEventString());
    }*/

}
