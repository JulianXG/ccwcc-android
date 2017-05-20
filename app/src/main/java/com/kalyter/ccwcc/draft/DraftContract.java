package com.kalyter.ccwcc.draft;

import android.widget.BaseAdapter;

import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.kalyter.ccwcc.common.BasePresenter;
import com.kalyter.ccwcc.common.BaseView;

/**
 * Created by Kalyter on 2017-5-20 0020.
 */

public interface DraftContract {
    interface View extends BaseView {
        void setAdapter(BaseAdapter adapter);

        void setCreator(SwipeMenuCreator creator);

        void showConfirmUpload(int index);

        void showConfirmDelete(int index);

        void showLoading();

        void closeLoading();

        void showActionSuccess();

        void showActionFail();

        void showNotSelectData();

        void setOnMenuItemClickListener(SwipeMenuListView.OnMenuItemClickListener listener);
    }

    interface Presenter extends BasePresenter {
        void loadDraft();

        void toggleAllSelectStatus(boolean status);

        void upload(int index);

        void delete(int index);

        void deleteSelectedData();

        void uploadSelectedData();

        boolean checkUsability();
    }
}
