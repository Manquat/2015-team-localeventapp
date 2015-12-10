package ch.epfl.sweng.evento.event;

import android.support.v4.app.Fragment;

/**
 * Fragment that display an Event with an ID passed as an Extra with the key KEYCURRENTEVENT.
 * After that allow to swipe left or right to explore the events actually loaded.
 */
public class EventFragment extends Fragment {

    private static final String TAG = "EventFragment";
    public static final String KEYCURRENTEVENT = "CurrentEvent";
   /* private RestApi mRestAPI;
    private Event mEvent;
    private ArrayList<String> mListDataHeader;
    private HashMap<String, List<String>> mListDataChild;
    private View mRootView;


    @Override
    public void onCreate(Bundle savedInstanceState){
        Bundle bundle = getArguments();
        int currentEventID = bundle.getInt(KEYCURRENTEVENT);
        mRestAPI = new RestApi(new DefaultNetworkProvider(), Settings.getServerUrl());
<<<<<<< HEAD

=======
>>>>>>> 3236a57983a9577df19acf749e4a7823aa1c95b3
        mEvent = EventDatabase.INSTANCE.getEvent(currentEventID);
    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_event, container, false);
        updateFields();

<<<<<<< HEAD
        return mRootView;
=======
        //getParticipant(currentEventID);
        updateFields(rootView);

        return rootView;
>>>>>>> 3236a57983a9577df19acf749e4a7823aa1c95b3
    }



    private void getParticipant(int signature){
        mParticipants = new ArrayList<User>();
        mEvent = EventDatabase.INSTANCE.getEvent(signature);

<<<<<<< HEAD
        RestApi restAPI = new RestApi(new DefaultNetworkProvider(), Settings.getServerUrl());
        restAPI.getUser(new GetEventListCallback() {
            public void onUserListReceived(List<User> userArrayList) {
                mParticipants = userArrayList;
            }
            public void onEventListReceived(List<Event> eventArrayList){

            }

        }, mEvent.getID());

        //Hosted event
        /*hostedEvent = new ArrayList<Event>();;
        restAPI.getHostedEvent(new GetMultipleResponseCallback() {
            public void onDataReceived(List<Event> eventArrayList) {
                hostedEvent = eventArrayList;
                Log.d(TAG, hostedEvent.get(0).getTitle());
                Log.d(TAG, hostedEvent.get(1).getTitle());
            }

        }, 8);*/

    /*    //Matched event
        hostedEvent = new ArrayList<Event>();;
        restAPI.getMatchedEvent(new GetEventListCallback() {
            public void onEventListReceived(List<Event> eventArrayList) {
                hostedEvent = eventArrayList;
                Log.d(TAG, hostedEvent.get(0).getTitle());
                Log.d(TAG, hostedEvent.get(1).getTitle());
            }
            public void onUserListReceived(List<User> userArrayList){

            }
=======
>>>>>>> 3236a57983a9577df19acf749e4a7823aa1c95b3


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
<<<<<<< HEAD
        setTagExpandableList();
=======
>>>>>>> 3236a57983a9577df19acf749e4a7823aa1c95b3

        ImageView pictureView = (ImageView) mRootView.findViewById(R.id.eventPictureView);
        pictureView.setImageBitmap(mEvent.getPicture());
<<<<<<< HEAD
        final Button joinEvent = (Button) mRootView.findViewById(R.id.joinEvent);
=======

        Button joinEvent = (Button) rootView.findViewById(R.id.joinEvent);
>>>>>>> 3236a57983a9577df19acf749e4a7823aa1c95b3
        joinEvent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
<<<<<<< HEAD
                MainActivity.getUser(1).addMatchedEvent(mEvent);
                if(mEvent.addParticipant(MainActivity.getUser(1))) {
                    Toast.makeText(getActivity().getApplicationContext(), "Joined", Toast.LENGTH_SHORT).show();
                    if(mEvent.isFull()) {
                        joinEvent.setClickable(false);
                    }
                    mRestAPI.updateEvent(mEvent,new HttpResponseCodeCallback() {
=======
                if(!mEvent.addParticipant(Settings.INSTANCE.getUser())) {
                    Log.d(TAG, "addParticipant just returned false");
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Joined", Toast.LENGTH_SHORT).show();
                    Settings.INSTANCE.getUser().addMatchedEvent(mEvent);
                    mRestAPI.addParticipant(mEvent.getID(), Settings.INSTANCE.getUser().getUserId(), new HttpResponseCodeCallback() {
>>>>>>> 3236a57983a9577df19acf749e4a7823aa1c95b3
                        @Override
                        public void onSuccess(String response) {
                            Log.d(TAG, "Response" + response);
                        }
                    });
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Sorry but this event just got completed by another guy.", Toast.LENGTH_SHORT).show();
                }
                getActivity().finish();
            }
        });

        Button removeUserFromEvent = (Button) rootView.findViewById(R.id.remove_user_from_event);
        if(mEvent.checkIfParticipantIsIn(Settings.INSTANCE.getUser())){
            removeUserFromEvent.setVisibility(View.VISIBLE);
        } else {
            removeUserFromEvent.setVisibility(View.INVISIBLE);
        }
        removeUserFromEvent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "Removed from the event", Toast.LENGTH_SHORT).show();
                mRestAPI.removeParticipant(mEvent.getID(), Settings.INSTANCE.getUser().getUserId(), new HttpResponseCodeCallback() {
                    @Override
                    public void onSuccess(String response) {
                        Log.d(TAG, "Response" + response);
                    }
                });
                getActivity().finish();
            }
        });
    }

    private void setTagExpandableList() {
        // get the list_view
        ExpandableListView mExpListView = (ExpandableListView) mRootView.findViewById(R.id.listParticipantExp);

        // preparing list data
        prepareListData();
        ExpendableList mListAdapter = new ExpendableList(getActivity().getApplicationContext(), mListDataHeader, mListDataChild);

        // setting list adapter
        mExpListView.setAdapter(mListAdapter);

    }

    private void prepareListData() {
        mListDataHeader = new ArrayList<String>();
        mListDataChild = new HashMap<String, List<String>>();

        // Adding child data
        mListDataHeader.add("Host of the event");
        mListDataHeader.add("Participant of the event (" + mEvent.getAllParticipant().size() + ")");


        // Adding child data
        String host = mEvent.getCreator();

        List<String> participant = new ArrayList<String>();
        for (User user: mEvent.getAllParticipant()) {
            participant.add(user.getUsername());
        }


        mListDataChild.put(mListDataHeader.get(0), new ArrayList<String>(Arrays.asList(host)));
        mListDataChild.put(mListDataHeader.get(1), participant);
    }*/
}
