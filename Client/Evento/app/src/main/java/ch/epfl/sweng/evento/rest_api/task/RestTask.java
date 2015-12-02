package ch.epfl.sweng.evento.rest_api.task;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import ch.epfl.sweng.evento.rest_api.RestException;
import ch.epfl.sweng.evento.rest_api.callback.RestTaskCallback;
import ch.epfl.sweng.evento.rest_api.network_provider.NetworkProvider;

/**
 * Created by joachimmuth on 30.11.15.
 */
public abstract class RestTask extends AsyncTask<String, Void, String> {
    private static final String TAG = "RestTask";
    private static final int HTTP_SUCCESS_START = 200;
    private static final int HTTP_SUCCESS_END = 299;
    private String mRestUrl;
    private RestTaskCallback mCallback;
    private NetworkProvider mNetworkProvider;
    private String mRequestBody;
    private final String mMethodType;


    public RestTask(String methodType, String restUrl, NetworkProvider networkProvider, RestTaskCallback callback,
                    String requestBody) {
        this.mRestUrl = restUrl;
        this.mCallback = callback;
        this.mNetworkProvider = networkProvider;
        this.mRequestBody = requestBody;
        this.mMethodType = methodType;
    }

    public RestTask(String methodType, String restUrl, NetworkProvider networkProvider, RestTaskCallback callback) {
        this(methodType, restUrl, networkProvider, callback, null);
    }


    @Override
    protected String doInBackground(String... params) {
        String response = null;
        try {
            // prepare URL and parameter
            HttpURLConnection conn = setHttpUrlConnection();
            // set server connection
            communicateWithServer(conn, mMethodType);
            // get HTTP response code and get the event ONLY in case of success
            int responseCode = getResponseCode(conn);
            // get the event
            response = setResponse(responseCode, conn);

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


    protected int getResponseCode(HttpURLConnection conn) throws IOException, RestException {
        int responseCode = conn.getResponseCode();
        Log.v(TAG, "responseCode " + Integer.toString(responseCode));
        if (responseCode < HTTP_SUCCESS_START || responseCode > HTTP_SUCCESS_END) {
            throw new RestException("Invalid HTTP response code");
        }
        return responseCode;
    }


    protected HttpURLConnection setHttpUrlConnection() throws IOException {
        URL url = new URL(mRestUrl);
        HttpURLConnection conn = mNetworkProvider.getConnection(url);
        return conn;
    }


    /**
     * By default, the task return to the user the HttpResponseCode that it got from the server
     * this method can by override in case we want to return other strings (such as even in JSON, ...)
     *
     * @param responseCode
     * @param conn
     * @return
     * @throws IOException
     */
    protected String setResponse(int responseCode, HttpURLConnection conn) throws IOException {
        String response = Integer.toString(responseCode);
        return response;
    }


    /**
     * Must be set by the type of task we are invoking: Get, Post, Put or Delete
     *
     * @throws IOException
     */
    protected abstract void communicateWithServer(HttpURLConnection conn, String methodType) throws IOException;


    /**
     * fetch the HTTP response and parse it into readable JSON String
     *
     * @param conn
     * @return
     * @throws IOException
     */
    protected String fetchContent(HttpURLConnection conn) throws IOException {
        StringBuilder out = new StringBuilder();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line).append("\n");
            }

            return out.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected void requestWithBody(HttpURLConnection conn, String method) throws IOException {
        // set connexion
        int postDataLength = mRequestBody.length();
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("charset", "utf-8");
        conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
        conn.setUseCaches(false);

        // send data
        try (OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream())) {
            wr.write(mRequestBody);
        }
    }

    protected void requestWithoutBody(HttpURLConnection conn, String method) throws IOException {
        conn.setRequestMethod(method);
        conn.setDoInput(true);
        conn.connect();
    }

}



