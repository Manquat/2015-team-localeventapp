package ch.epfl.sweng.evento.RestApi;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import ch.epfl.sweng.evento.NetworkProvider;

/**
 * Created by joachimmuth on 22.10.15.
 */
public class DeleteTask extends AsyncTask<String, Void, String> {
    private String restUrl;
    private RestTaskCallback callback;
    private NetworkProvider networkProvider;
    private static final int HTTP_SUCCESS_START = 200;
    private static final int HTTP_SUCCESS_END = 299;

    /**
     * Creates a new instance of PostTask with the specified URL, callback, and
     * request body.
     *  @param restUrl The URL for the REST API.
     * @param callback The callback to be invoked when the HTTP request
     *            completes.
     *
     */
    public DeleteTask(String restUrl, NetworkProvider networkProvider, RestTaskCallback callback){
        this.restUrl = restUrl;
        this.callback = callback;
        this.networkProvider = networkProvider;
    }

    @Override
    protected String doInBackground(String... arg0) {
        String response = null;
        //Use HTTP Client APIs to make the call.
        //Return the HTTP Response body here.
        //TODO
        try {
            URL url = new URL(restUrl);
            HttpURLConnection conn = networkProvider.getConnection(url);
            conn.setDoOutput(true);
            conn.setRequestProperty(
                    "Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("DELETE");
            conn.connect();

            int responseCode = 0;
            responseCode = conn.getResponseCode();
            if (responseCode < HTTP_SUCCESS_START || responseCode > HTTP_SUCCESS_END) {
                throw new RestException("Invalid HTTP response code");
            } else {
                response = Integer.toString(responseCode);
            }

        } catch (IOException e) {
            Log.e("RestException", "Exception thrown in GetTask", e);
        } catch (RestException e) {
            Log.e("RestException", "Exception thrown in GetTask", e);
        }




        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        callback.onTaskComplete(result);
        super.onPostExecute(result);
    }
}
