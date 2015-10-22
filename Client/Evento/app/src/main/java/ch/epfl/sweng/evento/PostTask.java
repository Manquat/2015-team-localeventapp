package ch.epfl.sweng.evento;

/**
 * Created by cerschae on 15/10/2015.
 */

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * An AsyncTask implementation for performing POSTs.
 */
public class PostTask extends AsyncTask<String, String, String> {
    private final NetworkProvider mNetworkProvider;
    private String mRestUrl;
    private RestTaskCallback mCallback;
    private String mRequestBody;

    /**
     * Creates a new instance of PostTask with the specified URL, callback, and
     * request body.
     *
     * @param mNetworkProvider
     * @param restUrl The URL for the REST API.
     * @param requestBody The body of the POST request.
     * @param callback The callback to be invoked when the HTTP request
*            completes.
     *
     */
    public PostTask(NetworkProvider mNetworkProvider, String restUrl, String requestBody, RestTaskCallback callback){
        this.mNetworkProvider = mNetworkProvider;
        this.mRestUrl = restUrl;
        this.mRequestBody = requestBody;
        this.mCallback = callback;
    }

    @Override
    protected String doInBackground(String... arg0) {
        URL url = null;
        String response = null;
        try {
            url = new URL(mRestUrl);


            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            HttpURLConnection conn = mNetworkProvider.getConnection(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //TODO
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        mCallback.onTaskComplete(result);
        super.onPostExecute(result);
    }
}