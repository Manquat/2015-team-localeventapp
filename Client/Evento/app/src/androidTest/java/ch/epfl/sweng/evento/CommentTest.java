package ch.epfl.sweng.evento;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static junit.framework.Assert.assertEquals;

/**
 * Testing the base functionality of a comment
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class CommentTest {
    private Comment mComment;
    private User mUser;
    private String mMessage;
    private Calendar mTimeStamp;

    @Before
    public void init() {
        mUser = new User(1, "MockJo", "mockjo@plop.ch");
        mMessage = "Testing the comment message";

        mComment = new Comment(mUser.getUserId(), mUser.getUsername(), mMessage, -1);
        mTimeStamp = new GregorianCalendar();
    }

    @Test
    public void messageCorrectlyCreated() {
        assertEquals(mMessage, mComment.getMessage());
    }

    @Test
    public void creatorIdCorrectlyCreated() {
        assertEquals(mUser.getUserId(), mComment.getUserId());
    }

    @Test
    public void ownerNameCorrectlyAssociated() {
        assertEquals(mUser.getUsername(), mComment.getCreatorName());
    }

    @Test
    public void timeStampCorrectlyCreated() {

        // verify the date of creation is correct down to the minute
        assertEquals(mTimeStamp.get(Calendar.YEAR),
                mComment.getTimeOfCreation().get(Calendar.YEAR));
        assertEquals(mTimeStamp.get(Calendar.MONTH),
                mComment.getTimeOfCreation().get(Calendar.MONTH));
        assertEquals(mTimeStamp.get(Calendar.DAY_OF_MONTH),
                mComment.getTimeOfCreation().get(Calendar.DAY_OF_MONTH));
        assertEquals(mTimeStamp.get(Calendar.HOUR_OF_DAY),
                mComment.getTimeOfCreation().get(Calendar.HOUR_OF_DAY));
        assertEquals(mTimeStamp.get(Calendar.MINUTE),
                mComment.getTimeOfCreation().get(Calendar.MINUTE));
    }
}
