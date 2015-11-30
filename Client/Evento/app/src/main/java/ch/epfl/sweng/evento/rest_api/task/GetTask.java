package ch.epfl.sweng.evento.rest_api.task;

/**
 * Created by cerschae on 15/10/2015.
 */

import java.io.IOException;

import ch.epfl.sweng.evento.rest_api.callback.RestTaskCallback;
import ch.epfl.sweng.evento.rest_api.network_provider.NetworkProvider;

/**
 * An AsyncTask implementation for performing GETs.
 */
public class GetTask extends RestTask {
    private static final String TAG = "GetTask";

    public GetTask(String restUrl, NetworkProvider networkProvider, RestTaskCallback callback) {
        super(restUrl, networkProvider, callback);
    }

    @Override
    protected void setResponse() throws IOException {
        response = fetchContent(conn);
    }

    @Override
    protected void communicateWithServer() throws IOException {
        requestWithoutBody("GET");
    }


}