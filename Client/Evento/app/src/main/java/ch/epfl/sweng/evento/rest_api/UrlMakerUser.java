package ch.epfl.sweng.evento.rest_api;

/**
 * Created by thomas on 28/11/15.
 */
public class UrlMakerUser extends UrlMaker {

    public UrlMakerUser() {
        access = "events/user/";
    }

    public UrlMakerUser(String access) {
        this.access = access;
    }

    public String getAccess() {
        return access;
    }

    public static String get(String urlServer, int noUser) {
        return urlServer + access + noUser;
    }

}
