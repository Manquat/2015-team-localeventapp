package ch.epfl.sweng.evento.rest_api.task;

import java.io.IOException;
import java.net.HttpURLConnection;

import ch.epfl.sweng.evento.rest_api.callback.RestTaskCallback;
import ch.epfl.sweng.evento.rest_api.network_provider.NetworkProvider;

/**
 * Created by joachimmuth on 22.10.15.
 */
public class DeleteTask extends RestTask
{
    private static final String TAG = "DeleteTask";

    public DeleteTask(String restUrl, NetworkProvider networkProvider, RestTaskCallback callback)
    {
        super("DELETE", restUrl, networkProvider, callback);
    }

    @Override
    protected void communicateWithServer(HttpURLConnection connection) throws IOException
    {
        requestWithoutBody(connection);
    }

}
