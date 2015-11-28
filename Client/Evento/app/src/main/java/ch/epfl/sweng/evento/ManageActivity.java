package ch.epfl.sweng.evento;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.evento.Events.Event;

public class ManageActivity extends AppCompatActivity {

    public final static String ITEM_TITLE = "title";
    public final static String ITEM_CAPTION = "caption";

    public Map<String,?> createItem(String title, String caption) {
        Map<String,String> item = new HashMap<String,String>();
        item.put(ITEM_TITLE, title);
        item.put(ITEM_CAPTION, caption);
        return item;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Map<String,?>> hostedEvent = new LinkedList<Map<String,?>>();
        for(Event event: MainActivity.getUser(1).getHostedEvent()){
            hostedEvent.add(createItem(event.getTitle(), event.getDescription()));
        }

        List<Map<String,?>> matchedEvent = new LinkedList<Map<String,?>>();
        for(Event event: MainActivity.getUser(1).getMatchedEvent()){
            matchedEvent.add(createItem(event.getTitle(), event.getDescription()));
        }

        // create our list and custom adapter
        SeparatedListAdapter adapter = new SeparatedListAdapter(this);
        /*adapter.addSection("Hosted Event", new ArrayAdapter<String>(this,
                R.layout.list_item_manage_event, hostedEvent));*/
        adapter.addSection("Hosted Event", new SimpleAdapter(this, hostedEvent, R.layout.list_complex_manage_event,
                new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.list_complex_title, R.id.list_complex_caption }));
        adapter.addSection("Matched Event", new SimpleAdapter(this, matchedEvent, R.layout.list_complex_manage_event,
                new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.list_complex_title, R.id.list_complex_caption }));

        ListView list = new ListView(this);
        list.setAdapter(adapter);
        this.setContentView(list);

    }


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
