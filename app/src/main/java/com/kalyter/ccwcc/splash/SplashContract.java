package com.kalyter.ccwcc.splash;

import com.kalyter.ccwcc.common.BasePresenter;
import com.kalyter.ccwcc.common.BaseView;

/**
 * Created by Kalyter on 2017-4-30 0030.
 */

public interface SplashContract {
    interface View extends BaseView {
        void showLogin();

        void showMain();
    }

    interface Presenter extends BasePresenter {

    }
}
