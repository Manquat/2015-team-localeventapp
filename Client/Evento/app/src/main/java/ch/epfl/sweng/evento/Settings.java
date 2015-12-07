package ch.epfl.sweng.evento;

/**
 * Created by joachimmuth on 12.11.15.
 */

public enum Settings {
    INSTANCE;
    private String mIdToken = "No Token";
    private User mUser = null;
    private int mUserId = 0;

    private Settings() {
        // private constructor
    }

    public static String getServerUrl() {
        //return "http://128.179.177.242:8000/";
        return "https://protected-hamlet-4797.herokuapp.com/";
    }


    public String getIdToken() {
        return mIdToken;
    }

    public void setIdToken(String IdToken) {
        this.mIdToken = IdToken;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        this.mUser = user;
    }

    public void setUserId(int id) {
        mUserId = id;
    }
}
