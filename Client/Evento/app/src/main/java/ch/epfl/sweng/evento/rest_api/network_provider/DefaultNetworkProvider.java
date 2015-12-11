package ch.epfl.sweng.evento.rest_api.network_provider;

/**
 * Created by joachimmuth on 23.10.15.
 */

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A default implementation of the {@link NetworkProvider} interface that uses
 * the mechanism available in the {@link URL} object to create
 * {@link HttpURLConnection} objects.
 */
public class DefaultNetworkProvider implements NetworkProvider {

    /**
     * The default constructor.
     */
    public DefaultNetworkProvider() {
    }

    @Override
    public HttpURLConnection getConnection(URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }

}