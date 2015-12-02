package ch.epfl.sweng.evento;

/**
 * Created by joachimmuth on 12.11.15.
 */


public final class Settings {
    private Settings() {
        // private constructor
    }

    public static String getServerUrl() {
        return "https://protected-hamlet-4797.herokuapp.com/";
        //return "http://128.179.190.98:8000/";
    }
}
