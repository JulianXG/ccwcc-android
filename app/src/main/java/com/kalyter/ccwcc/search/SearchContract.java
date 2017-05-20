package com.kalyter.ccwcc.search;

import android.support.v7.widget.RecyclerView;

import com.kalyter.ccwcc.common.BasePresenter;
import com.kalyter.ccwcc.common.BaseView;
import com.kalyter.ccwcc.model.Bird;

/**
 * Created by Kalyter on 2017-5-14 0014.
 */

public interface SearchContract {
    interface View extends BaseView {
        void setAdapter(RecyclerView.Adapter adapter);

        void showResult(Bird bird);

        void showConfirmBirdQuantity(Bird bird);
    }

    interface Presenter extends BasePresenter {
        void search(String keyword);
    }
}
