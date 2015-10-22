package ch.epfl.sweng.evento;

/**
 * Created by cerschae on 15/10/2015.
 */

import android.os.AsyncTask;

/**
 * An AsyncTask implementation for performing PUTs.
 */
public class PutTask extends AsyncTask<String, String, String> {
    private String mRestUrl;
    private RestTaskCallback mCallback;
    private String mRequestBody;

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
    public PutTask(String restUrl, String requestBody, RestTaskCallback callback){
        this.mRestUrl = restUrl;
        this.mRequestBody = requestBody;
        this.mCallback = callback;
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
        mCallback.onTaskComplete(result);
        super.onPostExecute(result);
    }
}