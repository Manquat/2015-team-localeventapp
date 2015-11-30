package ch.epfl.sweng.evento;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

/**
 * Testing the base functionality of a conversation
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ConversationTest {
    private Conversation mConversation;
    private Comment mComment;

    @Before
    public void init() {
        mConversation = new Conversation();
        mComment = new Comment(new MockUser(), "plop");
    }

    @Test
    public void isEmptyAtTheCreation() {
        try {
            mConversation.getComment(0);
        } catch (IndexOutOfBoundsException e) {
            //pass
            return;
        }
        fail();
    }

    @Test
    public void canAddComment() {
        mConversation.addComment(mComment);
        assertEquals(mComment, mConversation.getComment(0));
    }

    @Test
    public void canRemoveAComment() {
        mConversation.addComment(mComment);
        assertTrue(mConversation.deleteComment(mComment));
    }

    @Test
    public void cannotRemoveACommentThatIsNotPresent() {
        assertFalse(mConversation.deleteComment(mComment));
    }
}
