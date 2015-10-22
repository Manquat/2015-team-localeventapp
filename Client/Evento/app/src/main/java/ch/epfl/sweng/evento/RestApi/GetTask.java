package ch.epfl.sweng.evento.RestApi;

/**
 * Created by cerschae on 15/10/2015.
 */
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ch.epfl.sweng.evento.NetworkProvider;

/**
 * An AsyncTask implementation for performing GETs.
 */
public class GetTask extends AsyncTask<String, Void, String>{
    private static final int HTTP_SUCCESS_START = 200;
    private static final int HTTP_SUCCESS_END = 299;
    private String mRestUrl;
    private RestTaskCallback mCallback;
    private NetworkProvider myNetworkProvider;

    /**
     * Creates a new instance of GetTask with the specified URL and callback.
     *
     * @param restUrl The URL for the REST API.
     * @param callback The callback to be invoked when the HTTP request
     *            completes.
     *
     */
    public GetTask(String restUrl, NetworkProvider networkProvider, RestTaskCallback callback){
        this.mRestUrl = restUrl;
        this.mCallback = callback;
        this.myNetworkProvider = networkProvider;
    }

    @Override
    protected String doInBackground(String... params) {
        String response = null;
        try {
            URL url = new URL(mRestUrl);
            HttpURLConnection conn = myNetworkProvider.getConnection(url);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode < HTTP_SUCCESS_START || responseCode > HTTP_SUCCESS_END) {
                throw new RestException("Invalid HTTP response code");
            }

            response = fetchContent(conn);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: make something with that exception !
        } catch (RestException e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        mCallback.onTaskComplete(result);
        super.onPostExecute(result);
    }

    private String fetchContent(HttpURLConnection conn) throws IOException {
        StringBuilder out = new StringBuilder();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line + "\n");
            }

            String result = out.toString();
            Log.d("HTTPFetchContent", "Fetched string of length "
                    + result.length());
            return result;
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}