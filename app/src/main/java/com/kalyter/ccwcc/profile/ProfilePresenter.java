package com.kalyter.ccwcc.profile;

import com.kalyter.ccwcc.data.source.SplashSource;
import com.kalyter.ccwcc.model.User;

/**
 * Created by Kalyter on 2017-5-14 0014.
 */

class ProfilePresenter implements ProfileContract.Presenter {
    private SplashSource mSplashSource;
    private ProfileContract.View mView;

    public ProfilePresenter(SplashSource splashSource, ProfileContract.View view) {
        mSplashSource = splashSource;
        mView = view;
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
}
