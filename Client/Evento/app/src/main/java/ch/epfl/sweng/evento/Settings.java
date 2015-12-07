package ch.epfl.sweng.evento;

/**
 * Created by joachimmuth on 12.11.15.
 */


public enum Settings {
    INSTANCE;
    private String mIdToken = "No Token";

    Settings() {
        // private constructor
    }

    public static String getServerUrl() {
        return "https://protected-hamlet-4797.herokuapp.com/";
    }

    public String getIdToken() {
        return mIdToken;
    }

    public void setIdToken(String IdToken) {
        this.mIdToken = IdToken;
    }
}
