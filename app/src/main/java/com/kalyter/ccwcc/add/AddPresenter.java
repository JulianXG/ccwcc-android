package com.kalyter.ccwcc.add;

import android.Manifest;
import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.common.Config;
import com.kalyter.ccwcc.data.source.DraftSource;
import com.kalyter.ccwcc.data.source.RecordService;
import com.kalyter.ccwcc.data.source.SplashSource;
import com.kalyter.ccwcc.model.Bird;
import com.kalyter.ccwcc.model.BirdRecord;
import com.kalyter.ccwcc.model.Record;
import com.kalyter.ccwcc.model.Response;
import com.kalyter.ccwcc.model.User;
import com.kalyter.ccwcc.util.RecordListAdapter;
import com.kalyter.ccwcc.util.Util;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Kalyter on 2017-4-30 0030.
 */

public class AddPresenter implements AddContract.Presenter, BDLocationListener,
        SwipeMenuListView.OnMenuItemClickListener{
    private static final String TAG = "AddPresenter";
    private RecordService mRecordService;
    private AddContract.View mView;
    private DraftSource mDraftSource;
    private SplashSource mSplashSource;
    private Context mContext;
    private LocationClient mLocationClient;
    private LocationClientOption mOption;
    private Double mLongitude;
    private Double mLatitude;
    private String[] mLocatePermissions;
    private SwipeMenuCreator mSwipeMenuCreator;
    private RecordListAdapter mRecordListAdapter;

    public AddPresenter(RecordService recordService,
                        AddContract.View view,
                        DraftSource draftSource,
                        Context context,
                        LocationClient locationClient,
                        SplashSource splashSource) {
        mRecordService = recordService;
        mView = view;
        mDraftSource = draftSource;
        mContext = context;
        mSplashSource = splashSource;
        mLocationClient = locationClient;
        mOption = new LocationClientOption();
        mLocatePermissions = new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };
        mSwipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem openItem = new SwipeMenuItem(mContext);
                openItem.setBackground(android.R.color.holo_blue_light);
                openItem.setWidth(Util.dip2px(mContext, 72));
                openItem.setIcon(R.drawable.ic_edit_black_24dp);
                menu.addMenuItem(openItem);

                SwipeMenuItem deleteItem = new SwipeMenuItem(mContext);
                deleteItem.setBackground(android.R.color.holo_red_light);
                deleteItem.setWidth(Util.dip2px(mContext, 72));
                deleteItem.setIcon(R.drawable.ic_delete_black_24dp);
                menu.addMenuItem(deleteItem);
            }
        };
        mRecordListAdapter = new RecordListAdapter(mContext);
    }

    @Override
    public void start() {
        mView.setMenuCreator(mSwipeMenuCreator);
        mView.setConfirmAdapter(mRecordListAdapter);
        mView.setOnMenuItemClickListener(this);

        mOption.setIsNeedLocationDescribe(true);
        mLocationClient.setLocOption(mOption);
        mLocationClient.registerLocationListener(this);
        locate();
    }

    @Override
    public void upload(final Record record) {
        mView.showLoading();
        User user = mSplashSource.getCurrentUser();
        record.setUserId(user.getId());
        record.setLatitude(mLatitude);
        record.setLongitude(mLongitude);

        List<BirdRecord> birdRecords = new ArrayList<>();

        List<Bird> birds = mRecordListAdapter.getData();
        for (Bird bird : birds) {
            BirdRecord birdRecord = new BirdRecord();
            birdRecord.setId(bird.getId());
            birdRecord.setCount(bird.getBirdCount());
            birdRecords.add(birdRecord);
        }
        record.setBirds(birdRecords);

        mRecordService.postBirdRecord(record)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response>() {
                    @Override
                    public void onCompleted() {
                        mView.closeLoading();
                        mView.refreshUI();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.closeLoading();
                        mView.showSaveToDraft();
                        saveAsDraft(record);
                    }

                    @Override
                    public void onNext(Response response) {
                        mView.showActionSuccess();
                    }
                });
    }

    @Override
    public void saveAsDraft(Record record) {
        mDraftSource.save(record);
        mView.showActionSuccess();
        mView.refreshUI();
    }

    @Override
    public void locate() {
        mView.showLocating();
        mLocationClient.stop();
        mLocationClient.start();
    }

    @Override
    public void addBird(Bird bird) {
        mRecordListAdapter.addData(bird);
    }

    @Override
    public void addBirds(List<Bird> birds) {
        mRecordListAdapter.addData(birds);
    }

    @Override
    public void deleteBird(int position) {
        mRecordListAdapter.deleteData(position);
    }

    @Override
    public void editBird(int position, int count) {
        mRecordListAdapter.changeCount(position, count);
    }

    @Override
    public List<Bird> getSelectBirds() {
        return mRecordListAdapter.getData();
    }

    @Override
    public void clearData() {
        mRecordListAdapter.clearAllData();
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        Observable.just(bdLocation)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BDLocation>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: ", e);
                    }

                    @Override
                    public void onNext(BDLocation location) {
                        if (location.getLocType() == Config.LOCATE_DENIED) {
                            mView.showLocateDenied();
                            mView.requestPermissions(mLocatePermissions);
                        } else {
                            mView.showLocateResult(location.getLocationDescribe());
                            mLongitude = location.getLongitude();
                            mLatitude = location.getLatitude();
                        }
                    }
                });
    }

    @Override
    public void onConnectHotSpotMessage(String s, int i) {

    }

    @Override
    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
        if (index == 0) {
            // 更改
            mView.showConfirmBirdCount(position);
        } else if (index == 1) {
            mView.showDeleteBird(position);
        }
        return false;
    }
}
