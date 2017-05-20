package com.kalyter.ccwcc.main;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.view.KeyEvent;
import android.widget.Toast;

import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.common.BaseActivity;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements MainContract.View {
    @BindView(R.id.bottom_navigation)
    BottomNavigationView mBottomNavigation;
    private long mExitTime = 0;//用来判断按下两次返回键退出程序的时间值

    private MainContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.start();
    }

    @Override
    protected void setupPresenter() {
        mPresenter = new MainPresenter(this, getSupportFragmentManager());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - mExitTime > 2000) {
                Toast.makeText(MainActivity.this, R.string.prompt_exit_confirm, Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void setBottomItemSelectListener(BottomNavigationView.OnNavigationItemSelectedListener listener) {
        mBottomNavigation.setOnNavigationItemSelectedListener(listener);
    }

    @Override
    public void showDefaultSection() {
        mPresenter.toggleFragment(R.id.home);
    }
}