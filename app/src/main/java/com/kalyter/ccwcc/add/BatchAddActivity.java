package com.kalyter.ccwcc.add;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.common.App;
import com.kalyter.ccwcc.common.BaseActivity;
import com.kalyter.ccwcc.common.Config;
import com.kalyter.ccwcc.model.Bird;
import com.kalyter.ccwcc.util.Util;

import java.util.List;

import butterknife.BindView;

public class BatchAddActivity extends BaseActivity implements BatchAddContract.View {
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.expand_batch_add)
    ExpandableListView mExpandBatchAdd;
    private BatchAddContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle.setText(R.string.caption_batch_add);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.inflateMenu(R.menu.confirm);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent();
                List<Bird> selectData = mPresenter.getSelectData();
                intent.putExtra(Config.KEY_BUNDLE_SELECT_BIRDS, Util.serialize(selectData));
                BatchAddActivity.this.setResult(RESULT_OK, intent);
                finish();
                return false;
            }
        });

        mPresenter.start();
    }

    @Override
    protected void setupPresenter() {
        mPresenter = new BatchAddPresenter(this, this,
                App.getInjectClass().getDBSource());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_batch_add;
    }

    @Override
    public void setAdapter(ExpandableListAdapter adapter) {
        mExpandBatchAdd.setAdapter(adapter);
    }
}
