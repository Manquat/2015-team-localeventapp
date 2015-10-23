package ch.epfl.sweng.evento.RestApi;

/**
 * Created by joachimmuth on 22.10.15.
 * Tool allowing to set every type of URL, according with the django server convention
 * TODO: each of these function has to be setted!
 */
public final class UrlMaker {

    public static final String get(String urlServer) {
        String url = urlServer + "events/";
        return url;
    }

    public static final String post(String urlServer) {
        return urlServer;
    }

    public static final String put(String urlServer) {
        return urlServer;
    }

    public static final String delete(String urlServer) {
        return urlServer;
    }

}
