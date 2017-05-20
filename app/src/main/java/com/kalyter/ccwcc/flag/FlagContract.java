package com.kalyter.ccwcc.flag;

import com.kalyter.ccwcc.common.BasePresenter;
import com.kalyter.ccwcc.common.BaseView;
import com.kalyter.ccwcc.model.Flag;

/**
 * Created by Kalyter on 2017-5-20 0020.
 */

public interface FlagContract {
    interface View extends BaseView {
        void showLoading();

        void closeLoading();

        void showUploadSuccess();

        void showUploadFail();

        void showMain();

        boolean checkIntegrity();
    }

    interface Presenter extends BasePresenter {
        void upload(Flag flag);
    }
}
