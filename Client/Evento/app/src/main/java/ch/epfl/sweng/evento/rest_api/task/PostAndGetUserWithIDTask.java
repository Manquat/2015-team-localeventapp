package ch.epfl.sweng.evento.rest_api.task;

import java.io.IOException;
import java.net.HttpURLConnection;

import ch.epfl.sweng.evento.rest_api.callback.RestTaskCallback;
import ch.epfl.sweng.evento.rest_api.network_provider.NetworkProvider;

/**
 * Created by thomas on 03/12/15.
 */
public class PostAndGetUserWithIDTask extends RestTask {
    private static final String TAG = "PostTask";


    public PostAndGetUserWithIDTask(String restUrl, NetworkProvider networkProvider,
                                    String requestBody, RestTaskCallback callback) {
        super("POST", restUrl, networkProvider, callback, requestBody);
    }


    @Override
    protected void communicateWithServer(HttpURLConnection connection) throws IOException {
        requestWithBody(connection);
    }

    @Override
    protected String setResponse(int responseCode, HttpURLConnection conn) throws IOException {
        String response = fetchContent(conn);
        return response;
    }


}
