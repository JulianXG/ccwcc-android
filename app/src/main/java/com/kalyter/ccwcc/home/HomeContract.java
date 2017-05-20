package com.kalyter.ccwcc.home;

import com.kalyter.ccwcc.common.BasePresenter;
import com.kalyter.ccwcc.common.BaseView;
import com.kalyter.ccwcc.model.Checkpoint;

/**
 * Created by Kalyter on 2017-4-30 0030.
 */

public interface HomeContract {
    interface View extends BaseView {
        void showSwitchSuccess();

        void showCheckpoint(Checkpoint checkpoint);

        void showSwitchint();

        void closeSwitching();

        void showSwitchFail();
    }

    interface Presenter extends BasePresenter {
        void switchCheckpoint(int checkpointId);
    }
}
