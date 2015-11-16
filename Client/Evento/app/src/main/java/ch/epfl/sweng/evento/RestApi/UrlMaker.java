package ch.epfl.sweng.evento.RestApi;

import java.util.GregorianCalendar;

import ch.epfl.sweng.evento.common.logger.Log;

/**
 * Created by joachimmuth on 22.10.15.
 * Tool allowing to set every type of URL, according with the django server convention
 */
public final class UrlMaker {

    private static final String TAG = "UrlMaker";

    private UrlMaker() {
        // private constructor
    }

    public static final String get(String urlServer, int noEvent) {
        String url = urlServer + "events/" + String.valueOf(noEvent) + ".json";
        return url;
    }

    public static final String getLots(String urlServer) {
        String url = urlServer + "events/" + "1212300400/1483225200/46.8/7.1/1500";
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

    public static String getByDate(String urlServer, GregorianCalendar startDate, GregorianCalendar endDate) {
        long startTimeInSec = startDate.getTimeInMillis()/1000;
        long endTimeInSec = endDate.getTimeInMillis()/1000;
        String url = urlServer + "events/" + Long.toString(startTimeInSec) +
                "/" + Long.toString(endTimeInSec) +"/46.8/7.1/1500";

        Log.d(TAG, "url : " + url);
        return url;
    }
}
