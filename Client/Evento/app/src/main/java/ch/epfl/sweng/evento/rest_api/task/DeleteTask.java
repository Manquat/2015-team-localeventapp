package ch.epfl.sweng.evento.rest_api.task;

import java.io.IOException;

import ch.epfl.sweng.evento.rest_api.network_provider.NetworkProvider;
import ch.epfl.sweng.evento.rest_api.callback.RestTaskCallback;

/**
 * Created by joachimmuth on 22.10.15.
 */
public class DeleteTask extends RestTask {
    private static final String TAG = "DeleteTask";

    public DeleteTask(String restUrl, NetworkProvider networkProvider, RestTaskCallback callback) {
        super(restUrl, networkProvider, callback);
    }

    @Override
    protected void communicateWithServer() throws IOException {
        requestWithoutBody("DELETE");
    }

}
