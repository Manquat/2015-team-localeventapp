package ch.epfl.sweng.evento;

/**
 * Created by joachimmuth on 12.11.15.
 */

public class Settings {
    private static String mIdToken = "No Token";
    private static User mUser;
    private static int mUserId;

    private Settings() {
        // private constructor
    }

    public static String getServerUrl() {
        //return "http://128.179.177.242:8000/";
        return "https://protected-hamlet-4797.herokuapp.com/";
    }


    public static String getIdToken() {
        return mIdToken;
    }

    public static void setIdToken(String IdToken) {
        mIdToken = IdToken;
    }

    public static User getUser() {
        return mUser;
    }

    public static void setUser(User user) {
        mUser = user;
    }

    public static void setUserId(int id) {
        mUserId = id;
    }
}
