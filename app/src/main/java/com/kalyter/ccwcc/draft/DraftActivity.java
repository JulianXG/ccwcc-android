package com.kalyter.ccwcc.draft;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.common.App;
import com.kalyter.ccwcc.common.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class DraftActivity extends BaseActivity implements DraftContract.View {
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.draft_list)
    SwipeMenuListView mDraftList;
    @BindView(R.id.select_all)
    CheckBox mSelectAll;
    @BindView(R.id.all_upload)
    Button mAllUpload;
    @BindView(R.id.all_delete)
    Button mAllDelete;

    @OnClick(R.id.all_upload)
    void allUpload() {
        if (mPresenter.checkUsability()) {
            mPresenter.uploadSelectedData();
        } else {
            showNotSelectData();
        }
    }

    @OnClick(R.id.all_delete)
    void allDelete() {
        if (mPresenter.checkUsability()) {
            mPresenter.deleteSelectedData();
        } else {
            showNotSelectData();
        }
    }

    private DraftContract.Presenter mPresenter;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTitle.setText(R.string.draft_box);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.loading));
        mProgressDialog.setCanceledOnTouchOutside(false);

        mSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPresenter.toggleAllSelectStatus(isChecked);
            }
        });
        mPresenter.start();
    }

    @Override
    protected void setupPresenter() {
        mPresenter = new DraftPresenter(this,
                App.getInjectClass().getDraftSource(),
                App.getInjectClass().getRecordService(),
                this,
                App.getInjectClass().getSplashSource());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_draft;
    }

    @Override
    public void setAdapter(BaseAdapter adapter) {
        mDraftList.setAdapter(adapter);
    }

    @Override
    public void setCreator(SwipeMenuCreator creator) {
        mDraftList.setMenuCreator(creator);
    }

    @Override
    public void showConfirmUpload(final int index) {
        new MaterialDialog.Builder(this)
                .title("确认上传？")
                .positiveText(R.string.confirm)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mPresenter.upload(index);
                    }
                })
                .negativeText(R.string.caption_cancel)
                .build().show();
    }

    @Override
    public void showConfirmDelete(final int index) {
        new MaterialDialog.Builder(this)
                .title("确认删除？")
                .positiveText(R.string.confirm)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mPresenter.delete(index);
                    }
                })
                .negativeText(R.string.caption_cancel)
                .build().show();
    }

    @Override
    public void showLoading() {
        mProgressDialog.show();
    }

    @Override
    public void closeLoading() {
        mProgressDialog.dismiss();
    }

    @Override
    public void showActionSuccess() {
        Toast.makeText(this, R.string.action_success, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showActionFail() {
        Toast.makeText(this, R.string.action_fail, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNotSelectData() {
        Toast.makeText(this, "请勾选草稿，再进行操作", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setOnMenuItemClickListener(SwipeMenuListView.OnMenuItemClickListener listener) {
        mDraftList.setOnMenuItemClickListener(listener);
    }
}
