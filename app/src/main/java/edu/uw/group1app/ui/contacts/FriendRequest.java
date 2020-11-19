package edu.uw.group1app.ui.contacts;

/**
 * Object class that hold request information
 *
 * @author Ford Nguyen
 * @version 1.0
 */
public class FriendRequest {

    private final String mUserName;

    public FriendRequest(String username) {
        this.mUserName = username;
    }

    public String getUsername() {
        return mUserName;
    }
}
