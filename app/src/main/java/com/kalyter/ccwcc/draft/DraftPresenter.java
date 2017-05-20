package com.kalyter.ccwcc.draft;

import android.content.Context;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.data.source.DraftSource;
import com.kalyter.ccwcc.data.source.RecordService;
import com.kalyter.ccwcc.data.source.SplashSource;
import com.kalyter.ccwcc.model.Record;
import com.kalyter.ccwcc.model.Response;
import com.kalyter.ccwcc.model.User;
import com.kalyter.ccwcc.util.DraftAdapter;
import com.kalyter.ccwcc.util.Util;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Kalyter on 2017-5-20 0020.
 */

class DraftPresenter implements DraftContract.Presenter, SwipeMenuListView.OnMenuItemClickListener {
    private DraftContract.View mView;
    private DraftSource mDraftSource;
    private RecordService mRecordService;
    private SplashSource mSplashSource;
    private Context mContext;
    private DraftAdapter mDraftAdapter;
    private SwipeMenuCreator mSwipeMenuCreator;
    private User mUser;

    public DraftPresenter(DraftContract.View view,
                          DraftSource draftSource,
                          RecordService recordService,
                          Context context,
                          SplashSource splashSource) {
        mView = view;
        mDraftSource = draftSource;
        mRecordService = recordService;
        mContext = context;
        mSplashSource = splashSource;
        mUser = mSplashSource.getCurrentUser();
        mDraftAdapter = new DraftAdapter(mContext);
        mSwipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem upload = new SwipeMenuItem(mContext);
                upload.setBackground(android.R.color.holo_blue_light);
                upload.setWidth(Util.dip2px(mContext, 80));
                upload.setIcon(R.drawable.ic_file_upload_black_24dp);
                menu.addMenuItem(upload);

                SwipeMenuItem deleteItem = new SwipeMenuItem(mContext);
                deleteItem.setBackground(android.R.color.holo_red_light);
                deleteItem.setWidth(Util.dip2px(mContext, 80));
                deleteItem.setIcon(R.drawable.ic_delete_black_24dp);
                menu.addMenuItem(deleteItem);
            }
        };
    }

    @Override
    public void start() {
        mView.setAdapter(mDraftAdapter);
        mView.setCreator(mSwipeMenuCreator);
        mView.setOnMenuItemClickListener(this);
        loadDraft();
    }

    @Override
    public void loadDraft() {
        List<Record> records = mDraftSource.getAllRecords();
        mDraftAdapter.setData(records);
    }

    @Override
    public void toggleAllSelectStatus(boolean status) {
        mDraftAdapter.toggleAllStatus(status);
    }

    @Override
    public void upload(final int index) {
        Record record = mDraftAdapter.getRecordByIndex(index);
        record.setUserId(mUser.getId());
        mView.showLoading();
        mRecordService.postBirdRecord(record)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response>() {
                    @Override
                    public void onCompleted() {
                        mDraftSource.delete(index);
                        loadDraft();
                        mView.closeLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showActionFail();
                        mView.closeLoading();
                    }

                    @Override
                    public void onNext(Response response) {
                        mView.showActionSuccess();
                    }
                });
    }

    @Override
    public void delete(int index) {
        mDraftSource.delete(index);
        loadDraft();
    }

    @Override
    public void deleteSelectedData() {
        List<Record> records = mDraftAdapter.getSelectedData();
        mDraftSource.delete(records);
        loadDraft();
    }

    @Override
    public void uploadSelectedData() {
        final List<Record> records = mDraftAdapter.getSelectedData();
        mView.showLoading();
        mRecordService.postBirdRecords(records)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response>() {
                    @Override
                    public void onCompleted() {
                        mView.closeLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showActionFail();
                        mView.closeLoading();
                    }

                    @Override
                    public void onNext(Response response) {
                        mView.showActionSuccess();
                        mDraftSource.delete(records);
                        loadDraft();
                    }
                });
    }

    @Override
    public boolean checkUsability() {
        return mDraftAdapter.getSelectedData().size() != 0;
    }

    @Override
    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
        if (index == 0) {
            mView.showConfirmUpload(position);
        } else if (index == 1) {
            mView.showConfirmDelete(position);
        }
        return false;
    }
}
