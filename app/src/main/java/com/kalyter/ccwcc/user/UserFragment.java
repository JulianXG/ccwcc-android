package com.kalyter.ccwcc.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.common.App;
import com.kalyter.ccwcc.common.BaseFragment;
import com.kalyter.ccwcc.login.LoginActivity;
import com.kalyter.ccwcc.model.User;
import com.kalyter.ccwcc.profile.ProfileActivity;
import com.leon.lib.settingview.LSettingItem;

import butterknife.BindView;
import butterknife.OnClick;

public class UserFragment extends BaseFragment implements UserContract.View {

    @BindView(R.id.avatar)
    ImageView mAvatar;
    @BindView(R.id.username)
    TextView mUsername;
    @BindView(R.id.profile_container)
    LinearLayout mProfileContainer;
    @BindView(R.id.profile)
    LSettingItem mProfile;
    @BindView(R.id.settings)
    LSettingItem mSettings;
    @BindView(R.id.about)
    LSettingItem mAbout;
    @BindView(R.id.logout)
    Button mLogout;

    private UserContract.Presenter mPresenter;

    @OnClick(R.id.logout)
    void logout() {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.confirm)
                .setMessage(R.string.confirm_logout)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.logout();
                    }
                })
                .setNegativeButton(R.string.caption_cancel, null)
                .show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mProfile.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click() {
                showProfile();
            }
        });

        mSettings.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click() {
                showSettings();
            }
        });

        mAbout.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click() {
                showAbout();
            }
        });

        mPresenter.start();
    }

    @Override
    protected void setupPresenter() {
        mPresenter = new UserPresenter(
                this,
                App.getInjectClass().getSplashSource());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user;
    }

    @Override
    public void showUser(User user) {
        mUsername.setText(user.getNickname());
    }

    @Override
    public void showProfile() {
        startActivity(new Intent(getContext(), ProfileActivity.class));
    }

    @Override
    public void showSettings() {
        startActivity(new Intent());
    }

    @Override
    public void showAbout() {
        startActivity(new Intent(getContext(), AboutActivity.class));
    }

    @Override
    public void showLogin() {
        startActivity(new Intent(getContext(), LoginActivity.class));
        getActivity().finish();
    }
}
