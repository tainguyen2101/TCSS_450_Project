package edu.uw.group1app.ui.contacts;

/**
 * Object class that hold request information
 *
 * @author Ford Nguyen
 * @version 1.0
 */
public class FriendRequest {

    private final String mUserName;

    private final int mMemberID;

    public FriendRequest(String username, int mMemberID) {
        this.mUserName = username;
        this.mMemberID = mMemberID;
    }

    public String getUsername() {
        return mUserName;
    }

    public int getMemberID() {
        return mMemberID;
    }
}
