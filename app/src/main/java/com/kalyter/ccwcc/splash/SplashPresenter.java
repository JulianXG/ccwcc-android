package com.kalyter.ccwcc.splash;

import com.kalyter.ccwcc.common.Config;
import com.kalyter.ccwcc.data.source.SplashSource;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Kalyter on 2017-4-30 0030.
 */

public class SplashPresenter implements SplashContract.Presenter {
    private SplashSource mSplashSource;
    private SplashContract.View mView;

    public SplashPresenter(SplashSource splashSource, SplashContract.View view) {
        mSplashSource = splashSource;
        mView = view;
    }

    @Override
    public void start() {
        mSplashSource.updateIsFirstRun(false);
        Observable.just(0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .delay(Config.SPLASH_TIME, TimeUnit.SECONDS)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        if (!mSplashSource.getIsLogin()) {
                            mView.showLogin();
                        } else {
                            mView.showMain();
                        }
                    }
                });
    }
}
