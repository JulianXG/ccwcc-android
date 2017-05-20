package com.kalyter.ccwcc.login;

import android.content.Context;

import com.kalyter.ccwcc.data.source.SplashSource;
import com.kalyter.ccwcc.data.source.UserService;
import com.kalyter.ccwcc.model.Checkpoint;
import com.kalyter.ccwcc.model.LoginResult;
import com.kalyter.ccwcc.model.Response;
import com.kalyter.ccwcc.model.User;
import com.kalyter.ccwcc.util.Util;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Kalyter on 2017-4-30 0030.
 */

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View mView;
    private UserService mUserService;
    private SplashSource mSplashSource;
    private Context mContext;

    public LoginPresenter(LoginContract.View view,
                          UserService userService,
                          SplashSource splashSource,
                          Context context) {
        mView = view;
        mUserService = userService;
        mSplashSource = splashSource;
        mContext = context;
    }

    @Override
    public void start() {

    }

    @Override
    public void login(final User user, final int checkpointId) {
        if (user.getUsername().equals("") || user.getPassword().equals("")) {
            mView.showValidateError();
        } else {
            mView.showLogining();
            user.setPassword(Util.md5(user.getPassword()));
            mUserService.login(user)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Response<LoginResult>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.showLoginFail();
                        }

                        @Override
                        public void onNext(Response<LoginResult> userResponse) {
                            Checkpoint checkpoint = Util.getCheckpointById(mContext, checkpointId);
                            mSplashSource.updateUser(userResponse.getData().getUser());
                            mSplashSource.updateToken(userResponse.getData().getToken());
                            mSplashSource.updateCheckpoint(checkpoint);
                            mSplashSource.updateIsLogin(true);
                            mView.showMain();
                        }
                    });
        }
    }
}
