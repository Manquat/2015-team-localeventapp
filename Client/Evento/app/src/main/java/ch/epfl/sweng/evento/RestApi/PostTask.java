package ch.epfl.sweng.evento.RestApi;

/**
 * Created by cerschae on 15/10/2015.
 */

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import ch.epfl.sweng.evento.NetworkProvider;

/**
 * An AsyncTask implementation for performing POSTs.
 */
public class PostTask extends AsyncTask<String, String, String> {
    private static final String TAG = "PostTask";
    private static final int HTTP_SUCCESS_START = 200;
    private static final int HTTP_SUCCESS_END = 299;
    private final NetworkProvider mNetworkProvider;
    private String mRestUrl;
    private RestTaskCallback mCallback;
    private String mRequestBody;


    public PostTask(String restUrl, NetworkProvider networkProvider, String requestBody, RestTaskCallback callback){
        this.mNetworkProvider = networkProvider;
        this.mRestUrl = restUrl;
        this.mRequestBody = requestBody;
        this.mCallback = callback;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected String doInBackground(String... arg0) {
        String response = null;
        try {
            // prepare URL and parameter
            String urlParameters = mRequestBody;
            Log.d("HERE", urlParameters);
            String postData = urlParameters;
            int postDataLength = postData.length();
            URL url = new URL(mRestUrl);
            HttpURLConnection conn = mNetworkProvider.getConnection(url);
            // set connexion
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            conn.setUseCaches(false);
            // send data
            try (OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream())) {
                wr.write(postData);
            }
            // get back response code and put it in response string (in case of success)
            int responseCode = 0;
            responseCode = conn.getResponseCode();
            Log.v(TAG, "responseCode " + Integer.toString(responseCode));
            if (responseCode < HTTP_SUCCESS_START || responseCode > HTTP_SUCCESS_END) {
                throw new RestException("Invalid HTTP response code");
            } else {
                response = Integer.toString(responseCode);
            }

        } catch (MalformedURLException e) {
            Log.e(TAG, "Exception thrown in doInBackground", e);
        } catch (ProtocolException e) {
            Log.e(TAG, "Exception thrown in doInBackground", e);
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