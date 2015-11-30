package ch.epfl.sweng.evento;

/**
 * Created by Gaffinet on 30/11/2015.
 */
public class User {


//---------------------------------------------------------------------------------------------
//----Attributes-------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------

    private static final String TAG = "UserProfilIndormation";


    //A unique Id for each Google Account
    private String mUserId;
    private String mUsername;
    private String mEmail;



//---------------------------------------------------------------------------------------------
//----Methods----------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------


    public User(String mUserId, String mUsername, String mEmail) {
        this.mUserId = mUserId;
        this.mUsername = mUsername;
        this.mEmail = mEmail;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public void setUserId(String mUserId) {
        this.mUserId = mUserId;
    }


}
