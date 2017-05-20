package com.kalyter.ccwcc.profile;

import com.kalyter.ccwcc.common.BasePresenter;
import com.kalyter.ccwcc.common.BaseView;
import com.kalyter.ccwcc.model.User;

/**
 * Created by Kalyter on 2017-5-14 0014.
 */

public interface ProfileContract {
    interface View extends BaseView {
        void showUser(User user);
    }

    interface Presenter extends BasePresenter {
        void loadUser();
    }
}
