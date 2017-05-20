package com.kalyter.ccwcc.user;

import com.kalyter.ccwcc.data.source.SplashSource;
import com.kalyter.ccwcc.model.User;

/**
 * Created by Kalyter on 2017-5-14 0014.
 */

public class UserPresenter implements UserContract.Presenter {
    private UserContract.View mView;
    private SplashSource mSplashSource;


    public UserPresenter(UserContract.View view,
                         SplashSource splashSource) {
        mView = view;
        mSplashSource = splashSource;
    }

    @Override
    public void start() {
        loadUser();
    }

    @Override
    public void loadUser() {
        User user = mSplashSource.getCurrentUser();
        mView.showUser(user);
    }

    @Override
    public void logout() {
        mView.showLogin();
        mSplashSource.updateIsLogin(false);
    }
}
