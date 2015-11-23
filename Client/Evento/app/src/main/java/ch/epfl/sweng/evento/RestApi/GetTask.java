package ch.epfl.sweng.evento.RestApi;

/**
 * Created by cerschae on 15/10/2015.
 */

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ch.epfl.sweng.evento.NetworkProvider;

/**
 * An AsyncTask implementation for performing GETs.
 */
public class GetTask extends AsyncTask<String, Void, String> {
    private static final String TAG = "GetTask";
    private static final int HTTP_SUCCESS_START = 200;
    private static final int HTTP_SUCCESS_END = 299;
    private String mRestUrl;
    private RestTaskCallback mCallback;
    private NetworkProvider mNetworkProvider;

    public GetTask(String restUrl, NetworkProvider networkProvider, RestTaskCallback callback) {

        this.mRestUrl = restUrl;
        this.mCallback = callback;
        this.mNetworkProvider = networkProvider;
    }

    @Override
    protected String doInBackground(String... params) {
        String response = null;
        try {
            // prepare URL and parameter
            URL url = new URL(mRestUrl);
            HttpURLConnection conn = mNetworkProvider.getConnection(url);
            // set server connection
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            // get HTTP response code and get the event ONLY in case of success
            int responseCode = 0;
            responseCode = conn.getResponseCode();
            Log.v(TAG, "responseCode " + Integer.toString(responseCode));
            if (responseCode < HTTP_SUCCESS_START || responseCode > HTTP_SUCCESS_END) {
                throw new RestException("Invalid HTTP response code");
            }
            // get the event
            response = fetchContent(conn);

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
        //super.onPostExecute(result);
    }

    /**
     * fetch the HTTP response and parse it into readable JSON String
     *
     * @param conn
     * @return
     * @throws IOException
     */
    private String fetchContent(HttpURLConnection conn) throws IOException {
        StringBuilder out = new StringBuilder();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line + "\n");
            }

            return out.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}