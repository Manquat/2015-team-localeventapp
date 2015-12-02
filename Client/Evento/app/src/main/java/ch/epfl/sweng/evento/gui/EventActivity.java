package ch.epfl.sweng.evento.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import ch.epfl.sweng.evento.EventDatabase;
import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.infinite_pager_adapter.EventInfinitePageAdapter;
import ch.epfl.sweng.evento.infinite_pager_adapter.InfiniteViewPager;

public class EventActivity extends AppCompatActivity {

    public static final String KEYCURRENTEVENT = "CurrentEventKey";


    private InfiniteViewPager mPager;
    private EventInfinitePageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event);

        // Creating the Toolbar and setting it as the Toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        // get the signature of the current event

        int currentEventSignature = EventDatabase.INSTANCE.getFirstEvent().getID();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currentEventSignature = bundle.getInt(KEYCURRENTEVENT);
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


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        if (id == R.id.action_createAnEvent) {
            Intent intent = new Intent(this, CreatingEventActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_logout) {

        }

        return super.onOptionsItemSelected(item);

    }
}
