package com.kalyter.ccwcc.flag;

import com.kalyter.ccwcc.data.source.FlagService;
import com.kalyter.ccwcc.data.source.SplashSource;
import com.kalyter.ccwcc.model.Flag;
import com.kalyter.ccwcc.model.Response;
import com.kalyter.ccwcc.model.User;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Kalyter on 2017-5-20 0020.
 */

class FlagPresenter implements FlagContract.Presenter {
    private FlagContract.View mView;
    private SplashSource mSplashSource;
    private FlagService mFlagService;
    private User mUser;

    public FlagPresenter(FlagContract.View view, SplashSource splashSource, FlagService flagService) {
        mView = view;
        mSplashSource = splashSource;
        mFlagService = flagService;
    }

    @Override
    public void start() {
        mUser = mSplashSource.getCurrentUser();
    }

    @Override
    public void upload(Flag flag) {
        mView.showLoading();
        flag.setUserId(mUser.getId());
        mFlagService.postFlag(flag)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response>() {
                    @Override
                    public void onCompleted() {
                        mView.closeLoading();
                        mView.showMain();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.closeLoading();
                    }

                    @Override
                    public void onNext(Response response) {
                        mView.showUploadSuccess();
                    }
                });
    }
}
