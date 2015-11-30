package ch.epfl.sweng.evento.rest_api.task;

/**
 * Created by cerschae on 15/10/2015.
 */

import android.annotation.TargetApi;
import android.os.Build;

import java.io.IOException;

import ch.epfl.sweng.evento.rest_api.network_provider.NetworkProvider;
import ch.epfl.sweng.evento.rest_api.callback.RestTaskCallback;

/**
 * An AsyncTask implementation for performing POSTs.
 */
public class PostTask extends RestTask {
    private static final String TAG = "PostTask";
    private static final int HTTP_SUCCESS_START = 200;
    private static final int HTTP_SUCCESS_END = 299;


    public PostTask(String restUrl, NetworkProvider networkProvider, String requestBody, RestTaskCallback callback) {
        super(restUrl, networkProvider, callback, requestBody);
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void communicateWithServer() throws IOException {
        requestWithBody("POST");
    }
}