package ch.epfl.sweng.evento;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ManageActivity extends AppCompatActivity {

    private TextView mMatchedEvent;
    private TextView mHostedEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        mMatchedEvent = (TextView) findViewById(R.id.matchedEvent);
        mHostedEvent = (TextView) findViewById(R.id.hostedEvent);
        mMatchedEvent.setText(MainActivity.getUser(1).getMatchedEventString());
        mHostedEvent.setText(MainActivity.getUser(1).getHostedEventString());
    }

}
