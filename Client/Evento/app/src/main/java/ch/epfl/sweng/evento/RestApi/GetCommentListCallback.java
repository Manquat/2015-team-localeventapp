package ch.epfl.sweng.evento.RestApi;

import java.util.List;

import ch.epfl.sweng.evento.Comment;

/**
 * Created by joachimmuth on 30.11.15.
 */
public abstract class GetCommentListCallback {

        public abstract void onCommentListReceived(List<Comment> commentList);
}
