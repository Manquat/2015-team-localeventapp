package ch.epfl.sweng.evento.RestApi;

import java.util.GregorianCalendar;

/**
 * Created by joachimmuth on 22.10.15.
 * Tool allowing to set every type of URL, according with the django server convention
 */
public final class UrlMaker {

    private static final String TAG = "UrlMaker";
    private final static String event = "events/";
    private final static String accessMaster = "Gandalf/";

    private UrlMaker() {
        // private constructor
    }


    public static String get(String urlServer, int noEvent) {
        return urlServer + event  + noEvent + ".json";
    }

    public static final String getAll(String urlServer) {
        String url = urlServer + event ;//+ "1212300400/1483225200/46.8/7.1/1500";
        return url;
    }


    public static String post(String urlServer) {
        return urlServer + event ;
    }

    public static String post(String urlServer, String access) {
        return urlServer + access ;
    }

    public static String put(String urlServer, int id) {
        return urlServer + event  + id;
    }

    public static String delete(String urlServer, int id) {
        return urlServer + event  + id;
    }

    public static String getByDate(String urlServer, GregorianCalendar startDate, GregorianCalendar endDate) {
        return getWithFilter(urlServer, startDate, endDate, 46.8, 7.1, 1500);
    }

    public static String getWithFilter(String urlServer, GregorianCalendar startTime, GregorianCalendar endTime,
                                       double latitude, double longitude, double radius) {
        long startTimeInSec = startTime.getTimeInMillis() / 1000;
        long endTimeInSec = endTime.getTimeInMillis() / 1000;
        String url = urlServer + event + accessMaster + Long.toString(startTimeInSec)
                + "/" + Long.toString(endTimeInSec) + "/" + Double.toString(latitude) + "/"
                + Double.toString(longitude) + "/" + Double.toString(radius);
        return url;
    }
}
