package ch.epfl.sweng.evento.InfinitePagerAdapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import ch.epfl.sweng.evento.EventDatabase;
import ch.epfl.sweng.evento.Events.Event;

import ch.epfl.sweng.evento.R;

/**
 * An infinite page adapter for the event activity
 */
public class EventInfinitePageAdapter extends InfinitePagerAdapter<Integer> {
    Activity mActivity;

    public EventInfinitePageAdapter(Integer initialEventSignature, Activity activity) {
        super(initialEventSignature);

        mActivity = activity;
    }

    @Override
    public Integer getNextIndicator() {
        Event currentEvent = EventDatabase.INSTANCE.getEvent(getCurrentIndicator());
        return EventDatabase.INSTANCE.getNextEvent(currentEvent).getID();
    }

    @Override
    public Integer getPreviousIndicator() {
        Event currentEvent = EventDatabase.INSTANCE.getEvent(getCurrentIndicator());
        return EventDatabase.INSTANCE.getPreviousEvent(currentEvent).getID();
    }

    @Override
    public ViewGroup instantiateItem(Integer currentEventId) {
        // getting the event
        Event event = EventDatabase.INSTANCE.getEvent(currentEventId);

        // inflating  the layout
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        GridLayout layout = (GridLayout) inflater.inflate(R.layout.fragment_event,
                (ViewGroup) mActivity.getWindow().getDecorView().getRootView(), false);

        updateFields(layout, event);

        return layout;
    }

    private void updateFields(ViewGroup rootView, Event currentEvent) {
        TextView titleView = (TextView) rootView.findViewById(R.id.event_title_view);
        TextView creatorView = (TextView) rootView.findViewById(R.id.event_creator_view);
        TextView startDateView = (TextView) rootView.findViewById(R.id.event_start_date_view);
        TextView endDateView = (TextView) rootView.findViewById(R.id.event_end_date_view);
        TextView addressView = (TextView) rootView.findViewById(R.id.event_address_view);
        TextView descriptionView = (TextView) rootView.findViewById(R.id.event_description_view);

        titleView.setText(currentEvent.getTitle());
        creatorView.setText(currentEvent.getCreator());
        startDateView.setText(currentEvent.getStartDateAsString());
        endDateView.setText(currentEvent.getEndDateAsString());
        addressView.setText(currentEvent.getAddress());
        descriptionView.setText(currentEvent.getDescription());

        ImageView pictureView = (ImageView) rootView.findViewById(R.id.eventPictureView);
        pictureView.setImageBitmap(currentEvent.getPicture());
        Button joinEvent = (Button) rootView.findViewById(R.id.joinEvent);
        joinEvent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(mActivity, "Submitted", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
