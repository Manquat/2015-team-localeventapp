package ch.epfl.sweng.evento.rest_api.callback;

import java.util.List;

import ch.epfl.sweng.evento.User;

/**
 * Created by thomas on 04/12/15.
 */
public abstract class GetUserListCallback {
    public abstract void onUserListReceived(List<User> userArrayList);
}
