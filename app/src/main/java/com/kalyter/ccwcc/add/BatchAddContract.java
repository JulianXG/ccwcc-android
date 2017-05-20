package com.kalyter.ccwcc.add;

import android.widget.ExpandableListAdapter;

import com.kalyter.ccwcc.common.BasePresenter;
import com.kalyter.ccwcc.common.BaseView;
import com.kalyter.ccwcc.model.Bird;

import java.util.List;

/**
 * Created by Kalyter on 2017-5-14 0014.
 */

public interface BatchAddContract {
    interface View extends BaseView {
        void setAdapter(ExpandableListAdapter adapter);
    }

    interface Presenter extends BasePresenter {
        List<Bird> getSelectData();
    }
}
