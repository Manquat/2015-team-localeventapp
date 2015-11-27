package ch.epfl.sweng.evento.Events;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ch.epfl.sweng.evento.EventDatabase;
import ch.epfl.sweng.evento.R;

/**
 * Fragment that display an Event with an ID passed as an Extra with the key KEYCURRENTEVENT.
 * After that allow to swipe left or right to explore the events actually loaded.
 */
public class EventFragment extends Fragment {

    public static final String KEYCURRENTEVENT = "CurrentEvent";

    private Event mEvent;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event, container, false);

        Bundle bundle = getArguments();
        int currentEventID = bundle.getInt(KEYCURRENTEVENT);

        mEvent = EventDatabase.INSTANCE.getEvent(currentEventID);

        updateFields(rootView);

        return rootView;
    }

    private void updateFields(View rootView) {
        TextView titleView = (TextView) rootView.findViewById(R.id.event_title_view);
        TextView creatorView = (TextView) rootView.findViewById(R.id.event_creator_view);
        TextView startDateView = (TextView) rootView.findViewById(R.id.event_start_date_view);
        TextView endDateView = (TextView) rootView.findViewById(R.id.event_end_date_view);
        TextView addressView = (TextView) rootView.findViewById(R.id.event_address_view);
        TextView descriptionView = (TextView) rootView.findViewById(R.id.event_description_view);

        titleView.setText(mEvent.getTitle());
        creatorView.setText(mEvent.getCreator());
        startDateView.setText(mEvent.getStartDateAsString());
        endDateView.setText(mEvent.getEndDateAsString());
        addressView.setText(mEvent.getAddress());
        descriptionView.setText(mEvent.getDescription());

        ImageView pictureView = (ImageView) rootView.findViewById(R.id.eventPictureView);
        pictureView.setImageBitmap(mEvent.getPicture());
        Button joinEvent = (Button) rootView.findViewById(R.id.joinEvent);
        joinEvent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "Submitted", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });
    }
}
