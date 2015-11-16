package ch.epfl.sweng.evento;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import ch.epfl.sweng.evento.RestApi.RestException;

/**
 * Created by Pujima on 14/11/2015.
 */
public class GetUsernameTask extends AsyncTask<Void, Void,Integer> {
        Activity mActivity;
        String mScope;
        String mEmail;

        GetUsernameTask(Activity activity, String name, String scope) {
        this.mActivity = activity;
        this.mScope = scope;
        this.mEmail = name;
        }
    private static final int HTTP_SUCCESS_START = 200;
    private static final int HTTP_SUCCESS_END = 299;
    private static final String TAG = "GetUsernameTask";

    /**
     * Executes the asynchronous job. This runs when you call execute()
     * on the AsyncTask instance.
     */
    @Override
    protected Integer doInBackground(Void... params) {
        String token = null;
        int responseCode = 0;
            try {
            token = fetchToken();
            if (token != null) {
                Log.v(TAG, token);
            // **Insert the good stuff here.**
            // Use the token to access the user's Google data.
                try {
                    // prepare URL and parameter
                    //URL url = new URL("https://protected-hamlet-4797.herokuapp.com/events/1");
                    URL url = new URL("http://192.168.178.22:8000/user/"+token);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    // set server connection
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();
                    // get HTTP response code
                    responseCode = conn.getResponseCode();

                } catch (IOException e) {
                    Log.e("RestException", "Exception thrown in GetTask", e);
                }
                return responseCode;

            }
            } catch (IOException e) {
            // The fetchToken() method handles Google-specific exceptions,
            // so this indicates something went wrong at a higher level.
            // TIP: Check for network connectivity before starting the AsyncTask.

            }
            return responseCode;
            }

    @Override
    protected void onPostExecute(Integer responseCode) {
        Log.v(TAG, "token");
        if (responseCode > HTTP_SUCCESS_START && responseCode < HTTP_SUCCESS_END) {
            Log.v(TAG, "NOOOO");
        }
    }

    /**
     * Gets an authentication token from Google and handles any
     * GoogleAuthException that may occur.
     */
    protected String fetchToken() throws IOException {
            try {
            return GoogleAuthUtil.getToken(mActivity, mEmail, mScope);
            } catch (UserRecoverableAuthException userRecoverableException) {

            // GooglePlayServices.apk is either old, disabled, or not present
            // so we need to show the user some UI in the activity to recover.
            ((LoginActivity)mActivity).handleException(userRecoverableException);
            } catch (GoogleAuthException fatalException) {
            // Some other type of unrecoverable exception has occurred.
            // Report and log the error as appropriate for your app.
            }
            return null;
            }

            }