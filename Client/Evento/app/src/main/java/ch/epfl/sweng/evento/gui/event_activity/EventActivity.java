package ch.epfl.sweng.evento.gui.event_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import ch.epfl.sweng.evento.EventDatabase;
import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.gui.AutoRefreshToolbar;
import ch.epfl.sweng.evento.gui.CreatingEventActivity;
import ch.epfl.sweng.evento.gui.LoginActivity;
import ch.epfl.sweng.evento.gui.ManageActivity;
import ch.epfl.sweng.evento.gui.SearchActivity;
import ch.epfl.sweng.evento.gui.infinite_pager_adapter.EventInfinitePageAdapter;
import ch.epfl.sweng.evento.gui.infinite_pager_adapter.InfiniteViewPager;

public class EventActivity extends AppCompatActivity {

    public static final String CURRENT_EVENT_KEY = "CurrentEventKey";


    private InfiniteViewPager mPager;
    private EventInfinitePageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event);

        // Creating the Toolbar and setting it as the Toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        new AutoRefreshToolbar(this, toolbar);

        // get the signature of the current event

        int currentEventSignature = EventDatabase.INSTANCE.getFirstEvent().getID();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currentEventSignature = bundle.getInt(CURRENT_EVENT_KEY);
        }

        // Creating the EventInfinitePageAdapter at the current position
        mAdapter = new EventInfinitePageAdapter(currentEventSignature, this);

        // Assigning ViewPager View and setting the adapter
        mPager = (InfiniteViewPager) findViewById(R.id.event_infinite_pager);
        mPager.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
