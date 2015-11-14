package ch.epfl.sweng.evento.RestApi;

/**
 * Created by joachimmuth on 22.10.15.
 * Tool allowing to set every type of URL, according with the django server convention
 */
public final class UrlMaker {

    private UrlMaker() {
        // private constructor
    }

    public static String get(String urlServer, int noEvent) {
        return urlServer + "events/" + String.valueOf(noEvent) + ".json";
    }

    public static String post(String urlServer) {
        return urlServer + "events/";
    }

    public static String put(String urlServer, int id) {
        return urlServer + "events/" + Integer.toString(id);
    }

    public static String delete(String urlServer, int id) {
        return urlServer + "events/" + Integer.toString(id);
    }

}
