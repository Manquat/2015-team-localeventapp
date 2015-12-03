package ch.epfl.sweng.evento.rest_api.task;

/**
 * Created by cerschae on 15/10/2015.
 */

import java.io.IOException;
import java.net.HttpURLConnection;

import ch.epfl.sweng.evento.rest_api.callback.RestTaskCallback;
import ch.epfl.sweng.evento.rest_api.network_provider.NetworkProvider;

/**
 * An AsyncTask implementation for performing POSTs.
 */
public class PostTask extends RestTask {
    private static final String TAG = "PostTask";


    public PostTask(String restUrl, NetworkProvider networkProvider, String requestBody, RestTaskCallback callback) {
        super("POST", restUrl, networkProvider, callback, requestBody);
    }


    @Override
    protected void communicateWithServer(HttpURLConnection connection) throws IOException {
        requestWithBody(connection);
    }
}