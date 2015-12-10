package ch.epfl.sweng.evento.rest_api.callback;

import java.util.List;

import ch.epfl.sweng.evento.Comment;

/**
 * Created by joachimmuth on 30.11.15.
 */
public interface GetCommentListCallback
{

    void onCommentListReceived(List<Comment> commentList);
}
