package com.kalyter.ccwcc.home;

import android.content.Context;

import com.kalyter.ccwcc.data.source.SplashSource;
import com.kalyter.ccwcc.model.Checkpoint;
import com.kalyter.ccwcc.util.Util;

/**
 * Created by Kalyter on 2017-4-30 0030.
 */

public class HomePresenter implements HomeContract.Presenter {
    private SplashSource mSplashSource;
    private HomeContract.View mView;
    private Context mContext;

    public HomePresenter(SplashSource splashSource, HomeContract.View view, Context context) {
        mSplashSource = splashSource;
        mView = view;
        mContext = context;
    }

    @Override
    public void start() {
        mView.showCheckpoint(mSplashSource.getCheckpoint());
    }

    @Override
    public void switchCheckpoint(int checkpointId) {
        Checkpoint checkpoint = Util.getCheckpointById(mContext, checkpointId);
        mSplashSource.updateCheckpoint(checkpoint);
        mView.showSwitchSuccess();
        mView.showCheckpoint(checkpoint);
    }
}
