package ch.epfl.sweng.evento.RestApi;

import android.os.AsyncTask;

import ch.epfl.sweng.evento.NetworkProvider;

/**
 * Created by joachimmuth on 22.10.15.
 */
public class DeleteTask extends AsyncTask<String, Void, String> {
    private String restUrl;
    private RestTaskCallback callback;
    private String requestBody;
    private NetworkProvider networkProvider;

    /**
     * Creates a new instance of PostTask with the specified URL, callback, and
     * request body.
     *
     * @param restUrl     The URL for the REST API.
     * @param callback    The callback to be invoked when the HTTP request
     *                    completes.
     * @param requestBody The body of the POST request.
     */
    public DeleteTask(String restUrl, NetworkProvider networkProvider, String requestBody, RestTaskCallback callback) {
        this.requestBody = restUrl;
        this.requestBody = requestBody;
        this.callback = callback;
        this.networkProvider = networkProvider;
    }

    @Override
    protected String doInBackground(String... arg0) {
        String response = null;
        //Use HTTP Client APIs to make the call.
        //Return the HTTP Response body here.
        //TODO
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        callback.onTaskComplete(result);
        super.onPostExecute(result);
    }
}
