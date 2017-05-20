package com.kalyter.ccwcc.profile;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.common.App;
import com.kalyter.ccwcc.common.BaseActivity;
import com.kalyter.ccwcc.common.Config;
import com.kalyter.ccwcc.model.Role;
import com.kalyter.ccwcc.model.User;

import java.util.Date;

import butterknife.BindView;

public class ProfileActivity extends BaseActivity implements ProfileContract.View {
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.username)
    TextView mUsername;
    @BindView(R.id.username_container)
    LinearLayout mUsernameContainer;
    @BindView(R.id.nickname)
    TextView mNickname;
    @BindView(R.id.nickname_container)
    LinearLayout mNicknameContainer;
    @BindView(R.id.gender)
    TextView mGender;
    @BindView(R.id.gender_container)
    LinearLayout mGenderContainer;
    @BindView(R.id.email)
    TextView mEmail;
    @BindView(R.id.email_container)
    LinearLayout mEmailContainer;
    @BindView(R.id.role)
    TextView mRole;
    @BindView(R.id.role_container)
    LinearLayout mRoleContainer;
    @BindView(R.id.birthday)
    TextView mBirthday;
    @BindView(R.id.birthday_container)
    LinearLayout mBirthdayContainer;
    @BindView(R.id.tel)
    TextView mTel;
    @BindView(R.id.tel_container)
    LinearLayout mTelContainer;

    private ProfileContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle.setText(R.string.profile);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mPresenter.start();
    }

    @Override
    protected void setupPresenter() {
        mPresenter = new ProfilePresenter(
                App.getInjectClass().getSplashSource(),
                this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_profile;
    }

    @Override
    public void showUser(User user) {
        mUsername.setText(user.getUsername());
        mNickname.setText(user.getNickname());

        String gender = "";
        if (user.getSex().equals(Config.MALE)) {
            gender = "男";
        } else if (user.getSex().equals(Config.FEMALE)) {
            gender = "女";
        }
        mGender.setText(gender);
        mEmail.setText(user.getEmail());

        Role role = Role.getRoleById(user.getRoleId());
        if (role != null) {
            mRole.setText(role.getDescription());
        }

        Date birthday = user.getBirthday();
        if (birthday != null) {
            mBirthday.setText(Config.yyyyMMddHHmmss.format(birthday));
        }

        mTel.setText(user.getTel());
    }
}
