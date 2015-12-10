package ch.epfl.sweng.evento.gui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.Settings;
import ch.epfl.sweng.evento.event.Event;
import ch.epfl.sweng.evento.rest_api.RestApi;
import ch.epfl.sweng.evento.rest_api.callback.HttpResponseCodeCallback;
import ch.epfl.sweng.evento.rest_api.network_provider.DefaultNetworkProvider;
import ch.epfl.sweng.evento.rest_api.network_provider.NetworkProvider;

public class CreatingEventActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener,
        GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "CreatingEventActivity";

    protected Calendar mStartDate;
    protected Calendar mEndDate;

    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));
    protected TextView mStartDateView;
    protected TextView mEndDateView;
    private boolean mStartOrEndDate;
    private boolean mDisplayTimeFragment;
    private DatePickerDialogFragment mDateFragment;
    private List<String> mListDataHeader;
    private HashMap<String, List<String>> mListDataChild;
    private GoogleApiClient mGoogleApiClient;
    private TextView mPlaceDetailsText;
    private TextView mPlaceDetailsAttribution;
    private PlaceAutocompleteAdapter mAdapter;
    protected Set<String> mTag = new HashSet<>();
    private double latitude = 0.0;
    private double longitude = 0.0;
    private TimePickerDialogFragment mTimeFragment;


    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);

            // set longitude and latitude
            latitude = place.getLatLng().latitude;
            longitude = place.getLatLng().longitude;

            // Format details of the place for display and show it in a TextView.
            mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),
                    place.getId(), place.getAddress(), place.getPhoneNumber(),
                    place.getWebsiteUri()));

            // Display the third party attributions if set.
            final CharSequence thirdPartyAttribution = places.getAttributions();
            if (thirdPartyAttribution == null) {
                mPlaceDetailsAttribution.setVisibility(View.GONE);
            } else {
                mPlaceDetailsAttribution.setVisibility(View.VISIBLE);
                mPlaceDetailsAttribution.setText(Html.fromHtml(thirdPartyAttribution.toString()));
            }

            Log.i(TAG, "Place details received: " + place.getName());

            places.release();
        }
    };
    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data API
     * to retrieve more details about the place.
     *
     * @see com.google.android.gms.location.places.GeoDataApi#getPlaceById(com.google.android.gms.common.api.GoogleApiClient,
     * String...)
     */
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i(TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

//            Toast.makeText(getApplicationContext(), "Clicked: " + primaryText,
//                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };

    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        Log.e(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {
        if (!mStartOrEndDate)
            mStartDate = new GregorianCalendar(year, monthOfYear, dayOfMonth, 0, 0);
        else mEndDate = new GregorianCalendar(year, monthOfYear, dayOfMonth, 0, 0);
        mTimeFragment = new TimePickerDialogFragment();
        mTimeFragment.show(getFragmentManager(), "timePicker");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (!mStartOrEndDate) {
            mStartDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
            mStartDate.set(Calendar.MINUTE, minute);
            String s = Event.asNiceString(mStartDate);
            mStartDateView.setText(s);
        } else {
            mEndDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
            mEndDate.set(Calendar.MINUTE, minute);
            String s = Event.asNiceString(mEndDate);
            mEndDateView.setText(s);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // display the layout and prepare the configuration
        mDisplayTimeFragment = false;
        setContentView(R.layout.activity_creating_event);
        Button validateButton = (Button) findViewById(R.id.submitEvent);
        mDateFragment = new DatePickerDialogFragment();
        mDateFragment.setListener(this);
        final Button pictureButton = (Button) findViewById(R.id.pictureButton);

        // listener for date picker mStartDate
        mStartDateView = (TextView) findViewById(R.id.startDate);
        mStartDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDisplayTimeFragment = true;
                mStartOrEndDate = false;
                mDateFragment.show(getFragmentManager(), "datePicker");
            }
        });

        // listener for date picker mEndDate
        mEndDateView = (TextView) findViewById(R.id.endDate);
        mEndDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDisplayTimeFragment = true;
                mStartOrEndDate = true;
                mDateFragment.show(getFragmentManager(), "datePicker");
            }
        });

        // listener for address place picker (googlePlaceAPI)
        setPlacePickerField();


        // Expendable list for category choice
        setTagExpandableList();

        setValidateButtonAndSend(validateButton);

        setPictureButton(pictureButton);
    }

    /**
     * prepare button parameter, put all texts present in the TextViews into variable to prepare
     * a new event.
     * Also check if some field are empty and complete with default parameter to avoid crash
     * Send the created event through restAPI
     *
     * @param validateButton
     */
    private void setValidateButtonAndSend(Button validateButton) {
        validateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                TextView title = (TextView) findViewById(R.id.title);
                TextView description = (TextView) findViewById(R.id.eventDescription);
                TextView address = (TextView) findViewById(R.id.eventAddress);
                String titleString = title.getText().toString();
                String descriptionString = description.getText().toString();
                String addressString = address.getText().toString();
                ImageView pictureView = (ImageView) findViewById(R.id.pictureView);
                Drawable drawable = pictureView.getDrawable();
                Bitmap picture;

                // default value completion
                if (mStartDate == null) {
                    mStartDate = new GregorianCalendar();
                }
                if (mEndDate == null) {
                    mEndDate = new GregorianCalendar();
                }

                if (mEndDate.compareTo(mStartDate) < 0) {
                    Toast.makeText(getApplicationContext(), "Please pick a date of end after the starting time ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (titleString.isEmpty()) {
                    titleString = "No Title";
                }
                if (descriptionString.isEmpty()) {
                    descriptionString = "No description";
                }
                if (addressString.isEmpty()) {
                    addressString = "No address";
                }
                if (drawable == null) {
                    //Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
                    picture = Event.stringToBitMap(Event.samplePicture());
                } else {
                    picture = ((BitmapDrawable) drawable).getBitmap();
                }

                // mock getCreator and random id (ID will be assigned server side)

                int creator = Settings.getUser().getUserId();

                Random rand = new Random();
                int id = rand.nextInt(10000);

                // event creation and send
                Event e = new Event(id, titleString, descriptionString, latitude,
                        longitude, addressString, Settings.getUser().getUserId(),
                        mTag, mStartDate, mEndDate, picture);


                sendToServer(e);

                finish();

            }
        });
    }

    protected void sendToServer(Event e) {
        RestApi restApi = new RestApi(new DefaultNetworkProvider(), Settings.getServerUrl());
        restApi.postEvent(e, new HttpResponseCodeCallback() {
            @Override
            public void onSuccess(String response) {
                // assert submission
                Toast.makeText(getApplicationContext(), "Submitted", Toast.LENGTH_SHORT).show();
            }
        });
        Toast.makeText(getApplicationContext(), "Submitting " + e.getTitle(), Toast.LENGTH_SHORT).show();
    }

    private void setPictureButton(Button pictureButton) {
        pictureButton.setOnClickListener(new View.OnClickListener() {

                                             @Override
                                             public void onClick(View view) {
                                                 Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                                 startActivityForResult(intent, 2);
                                             }
                                         }
        );
    }

    private void setTagExpandableList() {
        // get the list_view
        ExpandableListView mExpListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();
        ExpendableList mListAdapter = new ExpendableList(getApplicationContext(), mListDataHeader, mListDataChild);

        // setting list adapter
        mExpListView.setAdapter(mListAdapter);

        // list_view Group click listener
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

        // list_view Group expanded listener
        mExpListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        mListDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // list_view on child click listener
        mExpListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                final int groupPosTmp = groupPosition;
                final int childPosTmp = childPosition;
                mTag = new HashSet<String>() {{
                    add(mListDataChild.get(
                            mListDataHeader.get(groupPosTmp)).get(
                            childPosTmp));
                }};

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
    }

    /**
     * Construct a GoogleApiClient for the {@link Places#GEO_DATA_API} using AutoManage
     * functionality, which automatically sets up the API client to handle Activity lifecycle
     * events. If your activity does not extend FragmentActivity, make sure to call connect()
     * and disconnect() explicitly.
     */

    private void setPlacePickerField() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();

        // Retrieve the AutoCompleteTextView that will display Place suggestions.
        AutoCompleteTextView mAutocompleteView = (AutoCompleteTextView)
                findViewById(R.id.eventAddress);

        // Register a listener that receives callbacks when a suggestion has been selected
        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);

        // Retrieve the TextViews that will display details and attributions of the selected place.
        mPlaceDetailsText = (TextView) findViewById(R.id.place_details);
        mPlaceDetailsText.setVisibility(View.GONE);
        mPlaceDetailsAttribution = (TextView) findViewById(R.id.place_attribution);
        mPlaceDetailsAttribution.setVisibility(View.GONE);

        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, BOUNDS_GREATER_SYDNEY,
                null);
        mAutocompleteView.setAdapter(mAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        ImageView viewImage = (ImageView) findViewById(R.id.pictureView);
        Uri selectedImage = data.getData();
        String[] filePath = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(selectedImage, filePath, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePath[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();

        Bitmap picture = (BitmapFactory.decodeFile(picturePath));

        int size = 300;
        Bitmap scaledPicture = Bitmap.createScaledBitmap(picture, size, (int) (size * ((double) picture.getHeight() / (double) picture.getWidth())), false);
        //75k
        viewImage.setImageBitmap(scaledPicture);
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

    /**
     * Called when the Activity could not connect to Google Play services and the auto manager
     * could resolve the error automatically.
     * In this case the API is not available and notify the user.
     *
     * @param connectionResult can be inspected to determine the cause of the failure
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }

}
