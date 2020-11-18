package edu.uw.group1app.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class UserInfoViewModel extends ViewModel {

    private final String mEmail;
    private final String mJwt;
    private final int mMemberId;
    private final String mUserName;

    private UserInfoViewModel(String email, String jwt, int memberid, String username) {
        mEmail = email;
        mJwt = jwt;
        mMemberId = memberid;
        mUserName = username;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getmJwt() { return mJwt; }

    public int getmMeberId() { return mMemberId; }

    public String getmUserName() { return mUserName; }

    public static class UserInfoViewModelFactory implements ViewModelProvider.Factory {

        private final String email;
        private final String jwt;
        private final int memberid;
        private final String username;

        public UserInfoViewModelFactory(String email, String jwt, int memberid, String username) {
            this.email = email;
            this.jwt = jwt;
            this.memberid = memberid;
            this.username = username;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == UserInfoViewModel.class) {
                return (T) new UserInfoViewModel(email, jwt, memberid, username);
            }
            throw new IllegalArgumentException(
                    "Argument must be: " + UserInfoViewModel.class);
        }
    }


}