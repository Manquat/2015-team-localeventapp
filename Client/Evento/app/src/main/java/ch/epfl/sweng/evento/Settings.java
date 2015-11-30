package ch.epfl.sweng.evento;

/**
 * Created by joachimmuth on 12.11.15.
 */


public enum Settings {
    INSTANCE;
    private String mIdToken = "No Token";
    private String mUserId = "No ID";

    private Settings() {
        // private constructor
    }

    public static String getServerUrl() {
        return "http://128.179.182.154:8000/";
    }


    public String getIdToken() {
        return mIdToken;
    }
    public void setIdToken(String IdToken) {
        this.mIdToken = IdToken;
    }

    public String getUserId() {return mUserId;}
    public void setmUserId(String UserId) {
        this.mUserId = UserId;
    }
}
