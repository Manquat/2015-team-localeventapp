package ch.epfl.sweng.evento.RestApi;

/**
 * Created by cerschae on 15/10/2015.
 */

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import ch.epfl.sweng.evento.NetworkProvider;

/**
 * An AsyncTask implementation for performing POSTs.
 */
public class PostTask extends AsyncTask<String, String, String> {
    private static final String charset = "UTF-8";
    private static final int HTTP_SUCCESS_START = 200;
    private static final int HTTP_SUCCESS_END = 299;
    private final NetworkProvider networkProvider;
    private String restUrl;
    private RestTaskCallback callback;
    private String requestBody;

    /**
     * Creates a new instance of PostTask with the specified URL, callback, and
     * request body.
     *
     * @param networkProvider
     * @param restUrl The URL for the REST API.
     * @param requestBody The body of the POST request.
     * @param callback The callback to be invoked when the HTTP request
*            completes.
     *
     */
    public PostTask(String restUrl, NetworkProvider networkProvider, String requestBody, RestTaskCallback callback){
        this.networkProvider = networkProvider;
        this.restUrl = restUrl;
        this.requestBody = requestBody;
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... arg0) {
        String response = null;
        try {
            // prepare URL and parameter
            String urlParameters = requestBody;
            String postData = urlParameters;
            int postDataLength = postData.length();
            URL url = new URL(restUrl);
            HttpURLConnection conn = networkProvider.getConnection(url);
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
            // get back response code and put it in response string
            int responseCode = 0;
            responseCode = conn.getResponseCode();
            if (responseCode < HTTP_SUCCESS_START || responseCode > HTTP_SUCCESS_END) {
                throw new RestException("Invalid HTTP response code");
            } else {
                response = Integer.toString(responseCode);
            }


        } catch (MalformedURLException e) {
            Log.e("RestException", "Exception thrown in PostTask", e);
        } catch (ProtocolException e) {
            Log.e("RestException", "Exception thrown in PostTask", e);
        } catch (IOException e) {
            Log.e("RestException", "Exception thrown in PostTask", e);
        } catch (RestException e) {
            Log.e("RestException", "Exception thrown in PostTask", e);
        }
        return response;
    }

        @Override
    protected void onPostExecute(String result) {
        callback.onTaskComplete(result);
        super.onPostExecute(result);
    }
}