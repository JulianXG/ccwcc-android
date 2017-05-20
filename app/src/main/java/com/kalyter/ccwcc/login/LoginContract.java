package com.kalyter.ccwcc.login;

import com.kalyter.ccwcc.common.BasePresenter;
import com.kalyter.ccwcc.common.BaseView;
import com.kalyter.ccwcc.model.User;

/**
 * Created by Kalyter on 2017-4-30 0030.
 */

public interface LoginContract {
    interface View extends BaseView {
        void showMain();

        void showLoginFail();

        void showLogining();

        void showValidateError();
    }

    interface Presenter extends BasePresenter {
        void login(User user, int checkpointId);
    }
}
