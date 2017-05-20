package com.kalyter.ccwcc.splash;

import android.content.Intent;
import android.os.Bundle;

import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.common.App;
import com.kalyter.ccwcc.common.BaseActivity;
import com.kalyter.ccwcc.login.LoginActivity;
import com.kalyter.ccwcc.main.MainActivity;
import com.kalyter.ccwcc.util.Util;

public class SplashActivity extends BaseActivity implements SplashContract.View {
    private SplashContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Util.fullScreen(this);
        super.onCreate(savedInstanceState);
        mPresenter.start();
    }

    @Override
    protected void setupPresenter() {
        mPresenter = new SplashPresenter(App.getInjectClass().getSplashSource(), this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void showLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void showMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
