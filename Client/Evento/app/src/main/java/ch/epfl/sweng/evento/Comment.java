package ch.epfl.sweng.evento;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * A comment about an Event made by one user
 */
public class Comment {
    private int mID;
    private String mMessage;
    private String mCreator;
    private int mUserID;
    private final Calendar mDateOfCreation;

    public Comment(int creatorId, String ownerName, String message, int commentId) {
        mUserID = creatorId;
        mCreator = ownerName;
        mMessage = message;
        mDateOfCreation = new GregorianCalendar();
        mID = commentId;
    }

    public String getMessage(){
        return mMessage;
    }

    public String getOwnerName() {
        // defensive copy
        return new String(mCreator);
    }

    public int getUserId() {
        return mUserID;
    }

    public int getID() {
        return mID;
    }

    public Calendar getTimeOfCreation() {
        //defensive copy
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(mDateOfCreation.getTime());
        return calendar;
    }
}
