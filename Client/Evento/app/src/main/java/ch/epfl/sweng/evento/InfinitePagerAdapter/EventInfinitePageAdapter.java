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
public class EventInfinitePageAdapter extends InfinitePagerAdapter<Long> {
    Activity mActivity;

    public EventInfinitePageAdapter(Long initialEventSignature, Activity activity) {
        super(initialEventSignature);

        mActivity = activity;
    }

    @Override
    public Long getNextIndicator() {
        return EventDatabase.INSTANCE.getNextSignature(getCurrentIndicator());
    }

    @Override
    public Long getPreviousIndicator() {
        return EventDatabase.INSTANCE.getPreviousSignature(getCurrentIndicator());
    }

    @Override
    public ViewGroup instantiateItem(Long signature) {
        // getting the event
        Event event = EventDatabase.INSTANCE.getEvent(signature);

        // inflating  the layout
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        GridLayout layout = (GridLayout) inflater.inflate(R.layout.fragment_event,
                (ViewGroup) mActivity.getWindow().getDecorView().getRootView(), false);

        updateFields(layout, event);

        return layout;
    }

    private void updateFields(ViewGroup rootView, Event event) {
        TextView titleView = (TextView) rootView.findViewById(R.id.event_title_view);
        TextView creatorView = (TextView) rootView.findViewById(R.id.event_creator_view);
        TextView startDateView = (TextView) rootView.findViewById(R.id.event_start_date_view);
        TextView endDateView = (TextView) rootView.findViewById(R.id.event_end_date_view);
        TextView addressView = (TextView) rootView.findViewById(R.id.event_address_view);
        TextView descriptionView = (TextView) rootView.findViewById(R.id.event_description_view);

        titleView.setText(event.getTitle());
        creatorView.setText(event.getCreator());
        startDateView.setText(event.getStartDateAsString());
        endDateView.setText(event.getEndDateAsString());
        addressView.setText(event.getAddress());
        descriptionView.setText(event.getDescription());

        ImageView pictureView = (ImageView) rootView.findViewById(R.id.eventPictureView);
        pictureView.setImageBitmap(event.getPicture());
        Button joinEvent = (Button) rootView.findViewById(R.id.joinEvent);
        joinEvent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(mActivity, "Submitted", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
