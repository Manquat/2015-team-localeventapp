package ch.epfl.sweng.evento.RestApi;

/**
 * Created by cerschae on 15/10/2015.
 */

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;

import ch.epfl.sweng.evento.NetworkProvider;

/**
 * An AsyncTask implementation for performing POSTs.
 */
public class PostTask extends RestTask {
    private static final String TAG = "PostTask";
    private static final int HTTP_SUCCESS_START = 200;
    private static final int HTTP_SUCCESS_END = 299;
    private String mRequestBody;


    public PostTask(String restUrl, NetworkProvider networkProvider, String requestBody, RestTaskCallback callback) {
        super(restUrl, networkProvider, callback);
        this.mRequestBody = requestBody;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected String doInBackground(String... arg0) {
        String response = null;
        try {
            // prepare URL and parameter
            setHttpUrlConnection();

            communicateWithServer();

            getResponseCode();

            setResponse();

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




    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void communicateWithServer() throws IOException {
        // set connexion
        int postDataLength = mRequestBody.length();
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("charset", "utf-8");
        conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
        conn.setUseCaches(false);

        // send data
        try (OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream())) {
            wr.write(mRequestBody);
        }

    }
}