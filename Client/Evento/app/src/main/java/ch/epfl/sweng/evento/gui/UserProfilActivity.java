package ch.epfl.sweng.evento.gui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.User;

/**
 * Created by Gaffinet on 23/11/2015.
 */
public class UserProfilActivity extends AppCompatActivity {

    private static final String TAG = "UserProfilIndormation";

    private User mUser;

    private TextView mUsernameView;
    private TextView mEmailView;
    private TextView mDateOfBirthView;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_userprofil);

        // Creating the Toolbar and setting it as the Toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

    }

}
