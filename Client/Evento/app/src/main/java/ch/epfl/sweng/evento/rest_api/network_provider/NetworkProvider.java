package ch.epfl.sweng.evento.rest_api.network_provider;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by joachimmuth on 21.10.15.
 * Constructs {@link HttpURLConnection} objects that can be used to
 * retrieve data from a given {@link URL}.
 */
public interface NetworkProvider {

    /**
     * @param url a valid HTTP or HTTPS URL
     * @return a new (@link HttpURLConnection) object successful connections
     * @throws IOException if the connection could not be established or if the
     *                     URL is not HTTP/HTTPS
     */
    HttpURLConnection getConnection(URL url) throws IOException;

}
