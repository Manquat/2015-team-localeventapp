package ch.epfl.sweng.evento.RestApi;

import android.util.Log;

import java.util.GregorianCalendar;
import java.lang.ref.SoftReference;

/**
 * Created by joachimmuth on 22.10.15.
 * Tool allowing to set every type of URL, according with the django server convention
 */
public abstract class UrlMaker {

    private static final String TAG = "UrlMaker";
    protected static String access;

    abstract protected String getAccess();
    public UrlMaker() {
        access = getAccess();
    }

    public static String getAll(String urlServer) {
        String url = urlServer + access ;//+ "1212300400/1483225200/46.8/7.1/1500";
        return url;
    }

    public static String post(String urlServer) {
        return urlServer + access ;
    }

    public static String put(String urlServer, int id) {
        return urlServer + access  + id;
    }

    public static String delete(String urlServer, int id) {
        return urlServer + access  + id;
    }

}
