package ch.epfl.sweng.evento;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import ch.epfl.sweng.evento.Events.EventPageAdapter;

public class EventActivity extends AppCompatActivity {

    public static final String KEYCURRENTEVENT = "CurrentEventKey";


    private ViewPager mPager;
    private EventPageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event);

        // Creating the Toolbar and setting it as the Toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        // Creating the EventPageAdapter
        mAdapter = new EventPageAdapter(getSupportFragmentManager());

        // Assigning ViewPager View and setting the adapter
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        // get the signature of the current event
        long currentEventSignature = EventDatabase.INSTANCE.getFirstEvent().getSignature();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currentEventSignature = bundle.getLong(KEYCURRENTEVENT);
        }


        // Set the position of the page viewer at the correct event
        mPager.setCurrentItem(EventDatabase.INSTANCE.getPosition(currentEventSignature));
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
