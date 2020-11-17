package edu.uw.group1app.ui.contacts;

import java.util.ArrayList;


/**
 * Contact object class that holds information of contact
 * @author Ford Nguyen
 * @version 2.0
 */
public class Contact {

    private final String mEmail;
    private final String mFirstName;
    private final String mLastName;
    private final String mUsername;
    private final int mMemberID;

    /**
     *
     * @param email email address
     * @param fName first name
     * @param lName last name
     * @param uName username
     * @param id userID
     */
    public Contact(final String email, final String fName, final String lName, final String uName,
                   final int id) {
        this.mEmail = email;
        this.mFirstName = fName;
        this.mLastName = lName;
        this.mUsername = uName;
        this.mMemberID = id;
    }

    /**
     * Get contact email
     * @return user's email address
     */
    public String getEmail() {
        return mEmail;
    }

    /**
     * Get contact first name
     * @return the user's First name
     */
    public String getFirstName() {
        return mFirstName;
    }

    /**
     * Get contact last name
     * @return the user's last name
     */
    public String getLastName() {
        return mLastName;
    }

    /**
     * Get contact username
     * @return the username
     */
    public String getUsername() {
        return mUsername;
    }

    /**
     * Get contact member ID
     * @return memberID
     */
    public int getMemberID() {
        return mMemberID;
    }

}
