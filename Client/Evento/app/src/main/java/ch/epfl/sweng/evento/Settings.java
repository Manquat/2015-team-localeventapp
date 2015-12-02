package ch.epfl.sweng.evento;

/**
 * Created by joachimmuth on 12.11.15.
 */

import ch.epfl.sweng.evento.User;

public enum Settings {
    INSTANCE;
    private String mIdToken = "No Token";
    private User mUser = null;

    private Settings() {
        // private constructor
    }

/*    public static String getServerUrl() {
        return "http://128.179.182.154:8000/";
    }*/
    public static String getServerUrl() {
        return "http://128.179.133.221:8000/";
    }



    public String getIdToken() {
        return mIdToken;
    }
    public void setIdToken(String IdToken) {
        this.mIdToken = IdToken;
    }

    public User getUser() {return mUser;}
    public void setUser(User user) {
        this.mUser = user;
    }
}
