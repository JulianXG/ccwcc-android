package com.kalyter.ccwcc.add;

import android.widget.BaseAdapter;

import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.kalyter.ccwcc.common.BasePresenter;
import com.kalyter.ccwcc.common.BaseView;
import com.kalyter.ccwcc.model.Bird;
import com.kalyter.ccwcc.model.Record;

import java.util.List;

/**
 * Created by Kalyter on 2017-4-30 0030.
 */

public interface AddContract {
    interface View extends BaseView {
        void refreshUI();

        void setConfirmAdapter(BaseAdapter adapter);

        void showLoading();

        void closeLoading();

        void showActionSuccess();

        void showActionFail();

        void showLocateResult(String result);

        void showLocateDenied();

        void requestPermissions(String[] permissions);

        void showLocating();

        void setMenuCreator(SwipeMenuCreator creator);

        void setOnMenuItemClickListener(SwipeMenuListView.OnMenuItemClickListener listener);

        void showConfirmBirdCount(int position);

        void showDeleteBird(int position);

        void showSaveToDraft();
    }

    interface Presenter extends BasePresenter {
        void upload(Record record);

        void saveAsDraft(Record record);

        void locate();

        void addBird(Bird bird);

        void addBirds(List<Bird> birds);

        void deleteBird(int position);

        void editBird(int position, int count);

        List<Bird> getSelectBirds();

        void clearData();
    }
}
