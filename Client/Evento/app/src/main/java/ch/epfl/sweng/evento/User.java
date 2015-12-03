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
 	private Set<Event> mMatchedEvent;
    private Set<Event> mHostedEvent;
 	private int mID;

	public User(int id, String username, String email){
	    mID = id;
	    mUsername = username;
	    mEmail = email;
	    mMatchedEvent = new HashSet<>();
	    mHostedEvent = new HashSet<>();
	}
	
	public User(String username,Set<Event> matchedEvent, Set<Event> hostedEvent){
	    mUsername = username;
	    mMatchedEvent = new HashSet<>(matchedEvent);
	    mHostedEvent = new HashSet<>(hostedEvent);
    }
    public User(){
        mUsername = "Alfred";
        mMatchedEvent = new HashSet<>();
        mHostedEvent = new HashSet<>();
    }

//---------------------------------------------------------------------------------------------
//----Methods----------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------


    public User(String mUserId, String mUsername, String mEmail) {
        this.mUserId = mUserId;
        this.mUsername = mUsername;
        this.mEmail = mEmail;
    }

    public Set<Event> getMatchedEvent() { return mMatchedEvent;}

    public Set<Event> getHostedEvent() { return mHostedEvent; }

    public int getID(){
        return mID;
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

    public boolean addHostedEvent(Event event) {
        if (event != null) {
            mHostedEvent.add(event);
        }
        return false;
    }

    public boolean addMatchedEvent(Event event) {
        if (event != null) {
            mMatchedEvent.add(event);
        }
        return false;
    }

    public String getMatchedEventString(String separator) {
        String res = "";
        if(!mMatchedEvent.isEmpty()){
            for(Event event: mMatchedEvent){
                res += event.getTitle() + separator;
            }
        }
        return res;
    }

    public String getMatchedEventString() {
        return getMatchedEventString("\n");
    }

    public String getHostedEventString(String separator) {
        String res = "";
        if(!mHostedEvent.isEmpty()){
            for(Event event: mHostedEvent){
                res += event.getTitle() + separator;
            }
        }
        return res;
    }

    public String getHostedEventString() {
        return getHostedEventString("\n");
    }


}
