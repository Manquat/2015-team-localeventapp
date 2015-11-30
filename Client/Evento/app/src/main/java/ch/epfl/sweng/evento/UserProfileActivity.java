package ch.epfl.sweng.evento;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;


/**
 * Created by Gaffinet on 30/11/2015.
 */
public class UserProfileActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {
    //---------------------------------------------------------------------------------------------
//----Attributes-------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------

    private static final String TAG = "UserProfileActivity";

    private String mUsername = "Elon Musk";
    private String mEmail = "abc@alphabetlovers.com";


//---------------------------------------------------------------------------------------------
//----Methods----------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_userprofile);


        TextView mUsernameView = (TextView) (findViewById(R.id.Username));
        mUsernameView.setText("Username : " + mUsername);
        TextView mEmailView = (TextView) (findViewById(R.id.Email));
        mEmailView.setText("Email Adress : " + mEmail);


    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
