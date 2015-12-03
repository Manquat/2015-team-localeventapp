package ch.epfl.sweng.evento.rest_api;

import java.util.GregorianCalendar;

/**
 * Created by joachimmuth on 22.10.15.
 * Tool allowing to set every type of URL, according with the django server convention
 */
public abstract class UrlMaker {

    private static final String TAG = "UrlMaker";
    private final static String event = "events/";
    private final static String comment = "comments/";
    private final static String accessMaster = "Gandalf/";
    protected static String access;

    abstract protected String getAccess();

    public UrlMaker() {
        access = getAccess();
    }


    public static String get(String urlServer, int noEvent) {
        return urlServer + event + noEvent + ".json";
    }

    public static final String getAll(String urlServer) {
        String url = urlServer + event;//+ "1212300400/1483225200/46.8/7.1/1500";
        return url;
    }

    public static String post(String urlServer) {
        return urlServer + access ;
    }

    public static String post(String urlServer, String access) {
        return urlServer + access ;
    }

    public static String put(String urlServer, int id) {
        return urlServer + access  + id;
    }

    public static String putParticipant(String urlServer, int idEvent, int idParticipant) {
        return urlServer + access  + idEvent + "/" + idParticipant;
    }

    public static String deleteParticipant(String urlServer, int idEvent, int idParticipant) {
        return urlServer + access  + idEvent + "/" + idParticipant;
    }

    public static String delete(String urlServer, int id) {
        return urlServer + access  + id;
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

    public static String postComment(String urlServer) {
        return urlServer + event + accessMaster;
    }

    public static String getComment(String urlServer, int eventId) {
        return urlServer + event + accessMaster + "/" + comment + eventId;
    }
}
