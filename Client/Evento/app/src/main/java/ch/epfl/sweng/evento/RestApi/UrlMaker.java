package ch.epfl.sweng.evento.RestApi;

import java.lang.ref.SoftReference;

/**
 * Created by joachimmuth on 22.10.15.
 * Tool allowing to set every type of URL, according with the django server convention
 */
public final class UrlMaker {

    private final static String event = "events/";
    private final static String accessMaster = "Gandalf/";

    private UrlMaker() {
        // private constructor
    }

    public static String get(String urlServer, int noEvent) {
        return urlServer + event + accessMaster + String.valueOf(noEvent) + ".json";
    }

    public static String post(String urlServer) {
        return urlServer + event + accessMaster;
    }

    public static String put(String urlServer, int id) {
        return urlServer + event + accessMaster + Integer.toString(id);
    }

    public static String delete(String urlServer, int id) {
        return urlServer + event + accessMaster + Integer.toString(id);
    }

}
