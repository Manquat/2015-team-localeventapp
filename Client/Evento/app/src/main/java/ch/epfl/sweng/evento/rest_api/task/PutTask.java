package ch.epfl.sweng.evento.rest_api.task;

/**
 * Created by cerschae on 15/10/2015.
 */

import java.io.IOException;
import java.net.HttpURLConnection;

import ch.epfl.sweng.evento.rest_api.callback.RestTaskCallback;
import ch.epfl.sweng.evento.rest_api.network_provider.NetworkProvider;


/**
 * An AsyncTask implementation for performing PUTs.
 */

public class PutTask extends RestTask {
    private static final String TAG = "PutTask";

    public PutTask(String restUrl, NetworkProvider networkProvider, String requestBody, RestTaskCallback callback) {
        super("PUT", restUrl, networkProvider, callback, requestBody);
    }

    @Override
    protected void communicateWithServer(HttpURLConnection connection) throws IOException {
        requestWithBody(connection);
    }
}
