/**
 * Created by Julian on 2016/9/12.
 */
package com.kalyter.ccwcc.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        setupPresenter();
    }

    protected abstract void setupPresenter();

    protected abstract int getLayoutId();

}
