package ch.epfl.sweng.evento;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import ch.epfl.sweng.evento.Events.Event;
import ch.epfl.sweng.evento.Events.EventPageAdapter;

/**
 * Created by Gaffinet on 23/11/2015.
 */
public class UserProfilActivity extends AppCompatActivity {

//---------------------------------------------------------------------------------------------
//----Attributes-------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------

    private static final String TAG = "UserProfilIndormation";

    private String mUsername;
    private String mEmail;
    //private Event.CustomDate mDateOfBirth;
    private LatLng mHomeAddress;
    //private Event.CustomDate mStartOfMembership;

    private TextView mUsernameView;
    private TextView mEmailView;
    private TextView mDateOfBirthView;



//---------------------------------------------------------------------------------------------
//----Methods----------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_userprofil);

        // Creating the Toolbar and setting it as the Toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

    }

}
