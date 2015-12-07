package ch.epfl.sweng.evento.rest_api;

import java.util.GregorianCalendar;

/**
 * Created by thomas on 28/11/15.
 */
public class UrlMakerEvent extends UrlMaker {


    public String getAccess() {
        return "events/";
    }

    public static String get(String urlServer, int noEvent) {
        return urlServer + access + noEvent + ".json";
    }

    public static String getByDate(String urlServer, GregorianCalendar startDate, GregorianCalendar endDate) {
        return getWithFilter(urlServer + access, startDate, endDate, 46.8, 7.1, 1500);
    }

    public static String getWithFilter(String urlServer, GregorianCalendar startTime, GregorianCalendar endTime,
                                       double latitude, double longitude, double radius) {
        long startTimeInSec = startTime.getTimeInMillis() / 1000;
        long endTimeInSec = endTime.getTimeInMillis() / 1000;
        String url = urlServer + access + Long.toString(startTimeInSec)
                + "/" + Long.toString(endTimeInSec) + "/" + Double.toString(latitude) + "/"
                + Double.toString(longitude) + "/" + Double.toString(radius);
        return url;
    }
}
