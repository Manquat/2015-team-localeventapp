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

    public GetTask(String restUrl, NetworkProvider networkProvider, RestTaskCallback callback) {
        super(restUrl, networkProvider, callback);
    }

    @Override
    protected void setResponse() throws IOException {
            response =  fetchContent(conn);
    }

    @Override
    protected void communicateWithServer() throws IOException {
        requestWithoutBody("GET");
    }



}