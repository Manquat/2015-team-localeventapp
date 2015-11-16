package ch.epfl.sweng.evento;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import ch.epfl.sweng.evento.Events.Event;
import ch.epfl.sweng.evento.RestApi.PostCallback;
import ch.epfl.sweng.evento.RestApi.RestApi;

public class CreatingEventActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    private static final NetworkProvider networkProvider = new DefaultNetworkProvider();
    private static final String urlServer = ServerUrl.get();

    private TextView mStartDateView;
    private TextView mEndDateView;
    private Event.Date startDate;
    private Event.Date endDate;
    private boolean mStartOrEndDate;
    private DialogFragment mDateFragment;
    private DialogFragment mTimeFragment;
    private ExpendableList mListAdapter;
    private ExpandableListView mExpListView;
    private List<String> mListDataHeader;
    private HashMap<String, List<String>> mListDataChild;


    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {
        if (!mStartOrEndDate) startDate = new Event.Date(year, monthOfYear, dayOfMonth, 0, 0);
        else endDate = new Event.Date(year, monthOfYear, dayOfMonth, 0, 0);
        mTimeFragment = new TimePickerDialogFragment();
        mTimeFragment.show(getFragmentManager(), "timePicker");
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (!mStartOrEndDate) {
            startDate.setTime(hourOfDay, minute);
            String s = Integer.toString(startDate.getMonth()) + "/" + Integer.toString(startDate.getDay()) + "/" + Integer.toString(startDate.getYear()) +
                    " at " + Integer.toString(startDate.getHour()) + ":" + Integer.toString(startDate.getMinutes());
            mStartDateView.setText(s);
        } else {
            endDate.setTime(hourOfDay, minute);
            String s = Integer.toString(endDate.getMonth()) + "/" + Integer.toString(endDate.getDay()) + "/" + Integer.toString(endDate.getYear()) +
                    " at " + Integer.toString(endDate.getHour()) + ":" + Integer.toString(endDate.getMinutes());
            mEndDateView.setText(s);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating_event);
        Button validateButton = (Button) findViewById(R.id.submitEvent);
        mDateFragment = new DatePickerDialogFragment();

        //START DATE
        mStartDateView = (TextView) findViewById(R.id.startDate);
        mStartDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStartOrEndDate = false;
                mDateFragment.show(getFragmentManager(), "datePicker");
            }
        });

        //END DATE
        mEndDateView = (TextView) findViewById(R.id.endDate);
        mEndDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStartOrEndDate = true;
                mDateFragment.show(getFragmentManager(), "datePicker");
            }
        });

        //CATEGORIES
        // get the ListView
        mExpListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        mListAdapter = new ExpendableList(getApplicationContext(), mListDataHeader, mListDataChild);

        // setting list adapter
        mExpListView.setAdapter(mListAdapter);

        // ListView Group click listener
        mExpListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // ListView Group expanded listener
        mExpListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        mListDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // ListView on child click listener
        mExpListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getApplicationContext(),
                        mListDataHeader.get(groupPosition)
                                + " : "
                                + mListDataChild.get(
                                mListDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });


        validateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                TextView title = (TextView) findViewById(R.id.title);
                final String titleString = title.getText().toString();
                TextView description = (TextView) findViewById(R.id.eventDescription);
                final String descriptionString = description.getText().toString();
                TextView address = (TextView) findViewById(R.id.eventAddress);
                final String addressString = address.getText().toString();


                double latitude = 0.0;
                double longitude = 0.0;
                String creator = "Jack Henri";
                Random rand = new Random();
                int id = rand.nextInt(10000);

                Event e = new Event(id,
                        titleString,
                        descriptionString,
                        latitude,
                        longitude,
                        addressString,
                        creator,
                        new HashSet<String>(),
                        startDate,
                        endDate);
                RestApi restApi = new RestApi(networkProvider, urlServer);

                restApi.postEvent(e, new PostCallback() {
                    @Override
                    public void onPostSuccess(String response) {
                        // nothing
                    }
                });
                Toast.makeText(getApplicationContext(), "Submitting " + titleString, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void prepareListData() {
        mListDataHeader = new ArrayList<String>();
        mListDataChild = new HashMap<String, List<String>>();

        // Adding child data
        mListDataHeader.add("Sport");
        mListDataHeader.add("Party");
        mListDataHeader.add("Stuff");

        // Adding child data
        List<String> sport = new ArrayList<String>();
        sport.add("Football");
        sport.add("Basketball");

        List<String> party = new ArrayList<String>();
        party.add("Bal");
        party.add("Boule");
        party.add("Bill");

        List<String> stuff = new ArrayList<String>();
        stuff.add("Penguin");
        stuff.add("Smurfs");


        mListDataChild.put(mListDataHeader.get(0), sport);
        mListDataChild.put(mListDataHeader.get(1), party);
        mListDataChild.put(mListDataHeader.get(2), stuff);
    }

}
