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
import java.net.ProtocolException;
import java.net.URL;

import ch.epfl.sweng.evento.NetworkProvider;

/**
 * An AsyncTask implementation for performing GETs.
 */
public class GetTask extends RestTask {
    private static final String TAG = "GetTask";
    private static final int HTTP_SUCCESS_START = 200;
    private static final int HTTP_SUCCESS_END = 299;

    public GetTask(String restUrl, NetworkProvider networkProvider, RestTaskCallback callback) {
        super(restUrl, networkProvider, callback);
    }

    @Override
    protected void setResponse() throws IOException {
            response =  fetchContent(conn);
    }

    @Override
    protected void communicateWithServer() throws IOException {
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
    }


    /**
     * fetch the HTTP response and parse it into readable JSON String
     *
     * @param conn
     * @return
     * @throws IOException
     */
    private String fetchContent(HttpURLConnection conn) throws IOException {
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
}