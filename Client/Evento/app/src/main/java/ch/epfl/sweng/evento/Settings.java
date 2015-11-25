package ch.epfl.sweng.evento;

/**
 * Created by joachimmuth on 12.11.15.
 */


public enum Settings {
    INSTANCE;
    private String mIdToken;

    private Settings() {
        // private constructor
    }

    public static String getServerUrl() {
        return "http://192.168.178.22:8000/";
    }

    public String getIdToken() {
        return mIdToken;
    }
    public void setIdToken(String IdToken) {
        this.mIdToken = IdToken;
    }
}
