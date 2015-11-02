package ch.epfl.sweng.evento.RestApi;

/**
 * Created by cerschae on 15/10/2015.
 */

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import ch.epfl.sweng.evento.NetworkProvider;

/**
 * An AsyncTask implementation for performing PUTs.
 */
public class PutTask extends AsyncTask<String, String, String> {
    private String mRestUrl;
    private RestTaskCallback mCallback;
    private String mRequestBody;
    private NetworkProvider networkProvider;
    private static final int HTTP_SUCCESS_START = 200;
    private static final int HTTP_SUCCESS_END = 299;

    /**
     * Creates a new instance of PostTask with the specified URL, callback, and
     * request body.
     *
     * @param restUrl The URL for the REST API.
     * @param callback The callback to be invoked when the HTTP request
     *            completes.
     * @param requestBody The body of the POST request.
     *
     */
    public PutTask(String restUrl, NetworkProvider networkProvider, String requestBody, RestTaskCallback callback){
        this.mRestUrl = restUrl;
        this.mRequestBody = requestBody;
        this.mCallback = callback;
        this.networkProvider = networkProvider;
    }

    @Override
    protected String doInBackground(String... arg0) {
        String response = null;
        try {
            // prepare URL and parameter
            String urlParameters = mRequestBody;
            String postData = urlParameters;
            int postDataLength = postData.length();
            URL url = new URL(mRestUrl);
            HttpURLConnection conn = networkProvider.getConnection(url);
            // set connexion
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            conn.setUseCaches(false);
            // send data
            try (OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream())) {
                wr.write(postData);
            }
            // get back response code and put it in response string
            int responseCode = 0;
            responseCode = conn.getResponseCode();
            if (responseCode < HTTP_SUCCESS_START || responseCode > HTTP_SUCCESS_END) {
                throw new RestException("Invalid HTTP response code");
            } else {
                response = Integer.toString(responseCode);
            }
        } catch (IOException e) {
            Log.e("RestException", "Exception thrown in PutTask", e);
        } catch (RestException e) {
            Log.e("RestException", "Exception thrown in PutTask", e);
        }


        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        mCallback.onTaskComplete(result);
        super.onPostExecute(result);
    }
}