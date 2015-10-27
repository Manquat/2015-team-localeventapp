package ch.epfl.sweng.evento;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.evento.Events.Event;

public class EventActivity extends AppCompatActivity implements
    GestureDetector.OnGestureListener{


    private GestureDetectorCompat movementDetector;
    private Event[] events;
    private Event currentEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        movementDetector = new GestureDetectorCompat(this,this);

        Event.Date start = new Event.Date(2015,10,12,18,30);
        Event.Date end = new Event.Date(2015,10,12,20,30);

        /*Those are mock events before getting them from the database*/
        events = new Event[5];
        Set<String> tag = new HashSet<String>();
        events[0] = new Event(1,"Event1","This is a first event",1.1,1.1,"1 long street","alfredo", tag, start,end);
        events[1] = new Event(2,"Event2","This is a second event",2.2,2.2,"2 long street","bob", tag, start,end);
        events[2] = new Event(3,"Event3","This is a third event",3.3,3.3,"3 long street","charlie", tag, start,end);
        events[3] = new Event(4,"Event4","This is a fourth event",4.4,4.4,"4 long street","dinesh", tag, start,end);
        events[4] = new Event(5,"Event5","This is a fifth event",5.5,5.5,"5 long street","ethan", tag, start,end);

        currentEvent = events[2];

        updateFields();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void updateFields() {
        TextView titleView = (TextView) findViewById(R.id.titleView);
        TextView creatorView = (TextView) findViewById(R.id.creatorView);
        TextView startDateView = (TextView) findViewById(R.id.startDateView);
        TextView endDateView = (TextView) findViewById(R.id.endDateView);
        TextView addressView = (TextView) findViewById(R.id.addressView);
        TextView descriptionView = (TextView) findViewById(R.id.descriptionView);

        titleView.setText(currentEvent.getTitle());
        creatorView.setText("Created by " + currentEvent.getCreator());
        startDateView.setText("From  " + currentEvent.getStartDate().toString());
        endDateView.setText(  "to    " + currentEvent.getEndDate().toString());
        addressView.setText(  "at    " + currentEvent.getAddress());
        descriptionView.setText(currentEvent.getDescription());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.movementDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {}

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float sensitivity = 50;
        if((e1.getX() - e2.getX()) > sensitivity){
            onSwipeLeft();
        }else if((e2.getX() - e1.getX()) > sensitivity) {
            onSwipeRight();
        }
        return false;
    }

    private void onSwipeLeft() {
        if(currentEvent.getID()<events.length-1)
        {
            currentEvent = events[currentEvent.getID()];
            updateFields();
        }
    }

    private void onSwipeRight() {
        if(currentEvent.getID()>1)
        {
            currentEvent = events[currentEvent.getID()-2];
            updateFields();
        }
    }
}
