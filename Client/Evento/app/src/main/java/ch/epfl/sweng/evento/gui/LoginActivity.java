package ch.epfl.sweng.evento.gui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import ch.epfl.sweng.evento.User;
import ch.epfl.sweng.evento.rest_api.RestApi;

import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.Settings;
import ch.epfl.sweng.evento.rest_api.callback.GetUserCallback;
import ch.epfl.sweng.evento.rest_api.callback.HttpResponseCodeCallback;
import ch.epfl.sweng.evento.rest_api.network_provider.DefaultNetworkProvider;
import ch.epfl.sweng.evento.rest_api.network_provider.NetworkProvider;

public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private static final NetworkProvider networkProvider = new DefaultNetworkProvider();
    private static final String urlServer = Settings.getServerUrl();
    // Request code used to invoke sign in user interactions.
    private static final int RC_SIGN_IN = 0;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // make sure there is a valid server client ID.
        validateServerClientID();
        // [START configure_signin]
        // Request only the user's ID token, which can be used to identify the
        // user securely to your backend. This will contain the user's basic
        // profile (name, profile picture URL, etc)
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .build();
        // [END configure_signin]

        // Build GoogleAPIClient with the Google Sign-In API and the above options.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addOnConnectionFailedListener(this)
                .build();


        // The Color Scheme of the sign in button and setup of the OnClickListener to Sign in if clicked.
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setColorScheme(SignInButton.COLOR_DARK);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.d(TAG, "onConnectionFailed:" + result);
    }

    //@Override
    public void onClick(View v) {

        if (v.getId() == R.id.sign_in_button) {
            //Call sign in function
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                String idToken = acct.getIdToken();
                Log.d(TAG, "idToken:" + idToken);
                Settings.INSTANCE.setIdToken(idToken);

                String personName = acct.getDisplayName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                User user = new User(personId, personName, personEmail);
                Settings.INSTANCE.setUser(user);
                RestApi restApi = new RestApi(new DefaultNetworkProvider(), urlServer);

                restApi.postUser(user, new GetUserCallback() {
                    @Override
                    public void onDataReceived(User user) {
                        Settings.INSTANCE.setUser(user);
                        Log.d(TAG, "Attributed UserId: " + Settings.INSTANCE.getUser().getUserId());
                        // assert submission
                        Toast.makeText(getApplicationContext(), "User Information sent to Server.", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "User information sent to server");
                    }
                });

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Could not connect, please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {

                }
            });
        }
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                String idToken = acct.getIdToken();
                Log.d(TAG, "idToken:" + idToken);
                Settings.INSTANCE.setIdToken(idToken);

                String personName = acct.getDisplayName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                User user = new User(personId, personName, personEmail);
                Settings.INSTANCE.setUser(user);
                RestApi restApi = new RestApi(new DefaultNetworkProvider(), urlServer);

                restApi.postUser(user, new GetUserCallback() {
                    @Override
                    public void onDataReceived(User user) {
                        Settings.INSTANCE.setUser(user);
                        Log.d(TAG, "Attributed UserId: " + Settings.INSTANCE.getUser().getUserId());
                        // assert submission
                        Toast.makeText(getApplicationContext(), "User Information sent to Server.", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "User information sent to server");
                    }
                });


                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

            } else {
                Toast.makeText(this, "Could not connect, please try again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void validateServerClientID() {
        String serverClientId = getString(R.string.server_client_id);
        String suffix = ".apps.googleusercontent.com";
        if (!serverClientId.trim().endsWith(suffix)) {
            String message = "Invalid server client ID in strings.xml, must end with " + suffix;

            Log.w(TAG, message);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }
}
