package ch.epfl.sweng.evento.gui;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import ch.epfl.sweng.evento.EventDatabase;
import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.Settings;
import ch.epfl.sweng.evento.event.Event;
import ch.epfl.sweng.evento.rest_api.RestApi;
import ch.epfl.sweng.evento.rest_api.callback.HttpResponseCodeCallback;
import ch.epfl.sweng.evento.rest_api.network_provider.DefaultNetworkProvider;

/**
 * Created by gautier on 07/12/2015.
 */
public class UpdateEventActivity extends CreatingEventActivity {
    public static final String EVENT_TO_UPDATE = "event_to_update";

    private Event mCurrentEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int currentEventId;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currentEventId = bundle.getInt(EVENT_TO_UPDATE);
        } else {
            throw new IllegalArgumentException("You must pass as extra a valid Event id");
        }

        mCurrentEvent = EventDatabase.INSTANCE.getEvent(currentEventId);

        EditText title = (EditText) findViewById(R.id.title);
        title.setText(mCurrentEvent.getTitle());

        AutoCompleteTextView address = (AutoCompleteTextView) findViewById(R.id.eventAddress);
        address.setText(mCurrentEvent.getAddress());

        EditText description = (EditText) findViewById(R.id.eventDescription);
        description.setText(mCurrentEvent.getDescription());

        ImageView image = (ImageView) findViewById(R.id.pictureView);
        image.setImageBitmap(mCurrentEvent.getPicture());

        mStartDate = mCurrentEvent.getStartDate();
        mStartDateView.setText(Event.asNiceString(mStartDate));

        mEndDate = mCurrentEvent.getEndDate();
        mEndDateView.setText(Event.asNiceString(mEndDate));

        mTag = mCurrentEvent.getTags();
    }

    @Override
    protected void sendToServer(Event event) {
        RestApi restApi = new RestApi(new DefaultNetworkProvider(), Settings.getServerUrl());

        Event eventWithTheGoodId = new Event(mCurrentEvent.getID(),
                event.getTitle(),
                event.getDescription(),
                event.getLatitude(),
                event.getLongitude(),
                event.getAddress(),
                Settings.getUser().getUserId(),
                event.getTags(),
                event.getStartDate(),
                event.getEndDate(),
                event.getPictureAsString());

        restApi.updateEvent(eventWithTheGoodId, new HttpResponseCodeCallback() {
            @Override
            public void onSuccess(String httpResponseCode) {
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
            }
        });
        Toast.makeText(getApplicationContext(), "Updated " + eventWithTheGoodId.getTitle(),
                Toast.LENGTH_SHORT).show();
    }
}
