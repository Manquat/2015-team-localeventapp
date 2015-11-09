package ch.epfl.sweng.evento.RestApi;

/**
 * Created by joachimmuth on 22.10.15.
 * Tool allowing to set every type of URL, according with the django server convention
 */
public final class UrlMaker {

    private UrlMaker() {
        // private constructor
    }

    public static final String get(String urlServer, int noEvent) {
        String url = urlServer + "events/" + String.valueOf(noEvent) + ".json";
        return url;
    }

    public static final String post(String urlServer) {
        String url = urlServer + "events/";
        return url;
    }

    public static final String put(String urlServer, int id) {
        String url = urlServer + "events/" + Integer.toString(id);
        return url;
    }

    public static final String delete(String urlServer, int id) {
        String url = urlServer + "events/" + Integer.toString(id);
        return url;
    }

}
