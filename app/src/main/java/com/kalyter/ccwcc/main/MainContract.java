package com.kalyter.ccwcc.main;

import android.support.design.widget.BottomNavigationView;

import com.kalyter.ccwcc.common.BasePresenter;
import com.kalyter.ccwcc.common.BaseView;

/**
 * Created by Kalyter on 2017-4-30 0030.
 */

public interface MainContract {
    interface View extends BaseView {
        void setBottomItemSelectListener(BottomNavigationView.OnNavigationItemSelectedListener listener);

        void showDefaultSection();
    }

    interface Presenter extends BasePresenter {
        void toggleFragment(int resId);
    }
}
