package ch.epfl.sweng.evento.RestApi;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import ch.epfl.sweng.evento.NetworkProvider;

/**
 * Created by joachimmuth on 30.11.15.
 */
public abstract class RestTask extends AsyncTask<String, Void, String> {
    private static final String TAG = "RestTask";
    protected static final int HTTP_SUCCESS_START = 200;
    protected static final int HTTP_SUCCESS_END = 299;
    protected String mRestUrl;
    protected RestTaskCallback mCallback;
    protected NetworkProvider mNetworkProvider;
    protected HttpURLConnection conn;
    protected String response;
    protected int responseCode;

    public RestTask(String restUrl, NetworkProvider networkProvider, RestTaskCallback callback) {
        this.mRestUrl = restUrl;
        this.mCallback = callback;
        this.mNetworkProvider = networkProvider;
        this.response = null;
        this.responseCode = 0;
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            // prepare URL and parameter
            setHttpUrlConnection();
            // set server connection
            communicateWithServer();
            // get HTTP response code and get the event ONLY in case of success
            getResponseCode();
            // get the event
            setResponse();

        } catch (IOException e) {
            Log.e(TAG, "Exception thrown in doInBackground", e);
        } catch (RestException e) {
            Log.e(TAG, "Exception thrown in doInBackground", e);
        }
        return response;
    }

    protected void getResponseCode() throws IOException, RestException {
        responseCode = conn.getResponseCode();
        Log.v(TAG, "responseCode " + Integer.toString(responseCode));
        if (responseCode < HTTP_SUCCESS_START || responseCode > HTTP_SUCCESS_END) {
            throw new RestException("Invalid HTTP response code");
        }
    }


    @Override
    protected void onPostExecute(String result) {
        mCallback.onTaskComplete(result);
        super.onPostExecute(result);
    }

    protected void setHttpUrlConnection() throws IOException {
        URL url = new URL(mRestUrl);
        conn = mNetworkProvider.getConnection(url);
    }


    /**
     * By default, the task return to the user the HttpResponseCode that it got from the server
     * this method can by override in case we want to return other strings (such as even in JSON, ...)
     * @return
     * @throws IOException
     */
    protected void setResponse() throws IOException{
        response =  Integer.toString(responseCode);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected abstract void communicateWithServer() throws IOException;

}
