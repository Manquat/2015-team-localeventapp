package ch.epfl.sweng.evento.RestApi;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import ch.epfl.sweng.evento.NetworkProvider;

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
