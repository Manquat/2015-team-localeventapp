package ch.epfl.sweng.evento.RestApi;

import java.util.GregorianCalendar;

/**
 * Created by thomas on 28/11/15.
 */
public class UrlMakerUser extends UrlMaker{

    public String getAccess(){
        return "events/user/";
    }

    public static String get(String urlServer, int noUser) {
        return urlServer + access  + noUser;
    }

}
