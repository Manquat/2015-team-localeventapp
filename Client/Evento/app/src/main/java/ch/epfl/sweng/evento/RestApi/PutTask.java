package ch.epfl.sweng.evento.RestApi;

/**
 * Created by cerschae on 15/10/2015.
 */

import java.io.IOException;

import ch.epfl.sweng.evento.NetworkProvider;

/**
 * An AsyncTask implementation for performing PUTs.
 */
public class PutTask extends RestTask {
    private static final String TAG = "PutTask";

    public PutTask(String restUrl, NetworkProvider networkProvider, String requestBody, RestTaskCallback callback) {
        super(restUrl, networkProvider, callback, requestBody);
    }

    @Override
    protected void communicateWithServer() throws IOException {
        requestWithBody("PUT");
    }
}