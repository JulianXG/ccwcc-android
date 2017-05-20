package com.kalyter.ccwcc.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.common.App;
import com.kalyter.ccwcc.common.BaseFragment;
import com.kalyter.ccwcc.draft.DraftActivity;
import com.kalyter.ccwcc.flag.FlagActivity;
import com.kalyter.ccwcc.model.Checkpoint;

import butterknife.BindView;
import butterknife.OnClick;


public class HomeFragment extends BaseFragment implements HomeContract.View {
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.layout_home_local)
    LinearLayout mLayoutHomeLocal;
    @BindView(R.id.layout_home_flag)
    LinearLayout mLayoutHomeFlag;

    private HomeContract.Presenter mPresenter;

    @OnClick(R.id.layout_home_local)
    void showLocal() {
        startActivity(new Intent(getContext(), DraftActivity.class));
    }

    @OnClick(R.id.layout_home_flag)
    void showFlag() {
        startActivity(new Intent(getContext(), FlagActivity.class));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mToolbar.inflateMenu(R.menu.switch_checkpoint);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                new AlertDialog.Builder(getContext())
                        .setTitle("选择检查点")
                        .setItems(R.array.checkpoints, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPresenter.switchCheckpoint(which);
                            }})
                        .show();
                return true;
            }
        });
        mPresenter.start();
    }

    @Override
    protected void setupPresenter() {
        mPresenter = new HomePresenter(App.getInjectClass().getSplashSource(), this, getContext());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void showSwitchSuccess() {
        Toast.makeText(getContext(), "切换检查地成功！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showCheckpoint(Checkpoint checkpoint) {
        mTitle.setText(checkpoint.getName());
    }

    @Override
    public void showSwitchint() {

    }

    @Override
    public void closeSwitching() {

    }

    @Override
    public void showSwitchFail() {

    }
}
