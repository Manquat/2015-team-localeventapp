package ch.epfl.sweng.evento.RestApi;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import ch.epfl.sweng.evento.NetworkProvider;
import ch.epfl.sweng.evento.Settings;

/**
 * Created by joachimmuth on 22.10.15.
 */
public class DeleteTask extends AsyncTask<String, Void, String> {
    private static final String TAG = "DeleteTask";
    private static final int HTTP_SUCCESS_START = 200;
    private static final int HTTP_SUCCESS_END = 299;
    private final String mRestUrl;
    private final RestTaskCallback mCallback;
    private final NetworkProvider mNetworkProvider;

    public DeleteTask(String restUrl, NetworkProvider networkProvider, RestTaskCallback callback) {
        this.mRestUrl = restUrl;
        this.mCallback = callback;
        this.mNetworkProvider = networkProvider;
    }


    @Override
    protected String doInBackground(String... arg0) {
        String response = null;
        try {
            // prepare URL and parameter
            URL url = new URL(mRestUrl);
            HttpURLConnection conn = mNetworkProvider.getConnection(url);
            conn.setDoOutput(true);
            // set connection with server
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("token", Settings.INSTANCE.getIdToken());
            conn.setRequestMethod("DELETE");
            conn.connect();
            // get back HTTP response code and store it in response (in case of success)
            int responseCode = 0;
            responseCode = conn.getResponseCode();
            Log.v(TAG, "responseCode " + Integer.toString(responseCode));
            if (responseCode < HTTP_SUCCESS_START || responseCode > HTTP_SUCCESS_END) {
                throw new RestException("Invalid HTTP response code");
            } else {
                response = Integer.toString(responseCode);
            }

        } catch (IOException e) {
            Log.e(TAG, "Exception thrown in doInBackground", e);
        } catch (RestException e) {
            Log.e(TAG, "Exception thrown in doInBackground", e);
        }

        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        mCallback.onTaskComplete(result);
        super.onPostExecute(result);
    }
}
