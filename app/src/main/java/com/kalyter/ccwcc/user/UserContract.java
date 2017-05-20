package com.kalyter.ccwcc.user;

import com.kalyter.ccwcc.common.BasePresenter;
import com.kalyter.ccwcc.common.BaseView;
import com.kalyter.ccwcc.model.User;

/**
 * Created by Kalyter on 2017-5-14 0014.
 */

public interface UserContract {
    interface View extends BaseView {
        void showUser(User user);

        void showProfile();

        void showSettings();

        void showAbout();

        void showLogin();
    }

    interface Presenter extends BasePresenter {
        void loadUser();

        void logout();
    }
}
