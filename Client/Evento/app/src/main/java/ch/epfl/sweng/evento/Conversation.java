package ch.epfl.sweng.evento;

import java.util.ArrayList;
import java.util.List;

/**
 * A collection of comment
 */
public class Conversation {
    private List<Comment> mComments;

    public Conversation() {
        mComments = new ArrayList<>();
    }

    public void addComment(Comment comment) {
        mComments.add(comment);
    }

    public boolean deleteComment(Comment comment) {
        return mComments.remove(comment);
    }

    public Comment getComment(int position) {
        return mComments.get(position);
    }

    public int size() {
        return mComments.size();
    }
}
