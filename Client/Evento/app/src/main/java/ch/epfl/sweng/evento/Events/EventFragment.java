package ch.epfl.sweng.evento.Events;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ch.epfl.sweng.evento.DefaultNetworkProvider;
import ch.epfl.sweng.evento.EventDatabase;
import ch.epfl.sweng.evento.MainActivity;
import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.RestApi.PutCallback;
import ch.epfl.sweng.evento.RestApi.RestApi;
import ch.epfl.sweng.evento.Settings;

/**
 * Created by Tago on 13/11/2015.
 */
public class EventFragment extends Fragment {

    public static final String KEYCURRENTEVENT = "CurrentEvent";
    private RestApi mRestAPI;
    private Event mEvent;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event, container, false);

        Bundle bundle = getArguments();
        long currentEventSignature = bundle.getLong(KEYCURRENTEVENT);

        mEvent = EventDatabase.INSTANCE.getEvent(currentEventSignature);

        updateFields(rootView);

        return rootView;
    }

    private void updateFields(View rootView) {
        TextView titleView = (TextView) rootView.findViewById(R.id.titleView);
        TextView creatorView = (TextView) rootView.findViewById(R.id.creatorView);
        TextView startDateView = (TextView) rootView.findViewById(R.id.startDateView);
        TextView endDateView = (TextView) rootView.findViewById(R.id.endDateView);
        TextView addressView = (TextView) rootView.findViewById(R.id.addressView);
        TextView descriptionView = (TextView) rootView.findViewById(R.id.descriptionView);
        TextView participantView = (TextView) rootView.findViewById(R.id.listParticipantView);

        titleView.setText(mEvent.getTitle());
        creatorView.setText(getString(R.string.eventFrag_createdBy, mEvent.getCreator()));
        startDateView.setText(getString(R.string.eventFrag_from, mEvent.getStartDate().toString()));
        endDateView.setText(getString(R.string.eventFrag_to, mEvent.getEndDate().toString()));
        addressView.setText(getString(R.string.eventFrag_at, mEvent.getAddress()));
        descriptionView.setText(mEvent.getDescription());
        participantView.setText(mEvent.getListParticipantString(", "));

        ImageView pictureView = (ImageView) rootView.findViewById(R.id.eventPictureView);
        pictureView.setImageBitmap(mEvent.getPicture());

        Button joinEvent = (Button) rootView.findViewById(R.id.joinEvent);
        joinEvent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "Joined", Toast.LENGTH_SHORT).show();
                String listOfParticipant = mEvent.getListParticipantString();
                MainActivity.getUser(1).addMatchedEvent(mEvent);
                if(!mEvent.addParticipant(MainActivity.getUser(1))) {
                    Log.d("EventFragment.upd.", "addParticipant just returned false");
                } else {
                    mRestAPI = new RestApi(new DefaultNetworkProvider(), Settings.getServerUrl());
                    mRestAPI.updateEvent(mEvent,new PutCallback() {
                        @Override
                        public void onPostSuccess(String response) {
                            Log.d("EventFrag.upd.", "Response" + response);
                        }
                    });
                }
                getActivity().finish();
            }
        });
    }
}
