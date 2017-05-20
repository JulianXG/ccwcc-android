package com.kalyter.ccwcc.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.common.App;
import com.kalyter.ccwcc.common.BaseActivity;
import com.kalyter.ccwcc.main.MainActivity;
import com.kalyter.ccwcc.model.User;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginContract.View {

    @BindView(R.id.login)
    Button buttonLogin;
    @BindView(R.id.username)
    EditText editUsername;
    @BindView(R.id.password)
    EditText editPassword;
    @BindView(R.id.checkpoints)
    AppCompatSpinner mCheckpoints;

    private ProgressDialog mProgressDialog;
    private LoginContract.Presenter mPresenter;

    @OnClick(R.id.login)
    void login() {
        User user = new User();
        user.setUsername(editUsername.getText().toString());
        user.setPassword(editPassword.getText().toString());
        int checkpointId = mCheckpoints.getSelectedItemPosition();
        mPresenter.login(user, checkpointId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCheckpoints.setSelection(11);
    }

    @Override
    protected void setupPresenter() {
        mPresenter = new LoginPresenter(this, App.getInjectClass().getUserService(),
                App.getInjectClass().getSplashSource(), this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void showMain() {
        mProgressDialog.dismiss();
        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void showLoginFail() {
        mProgressDialog.dismiss();
        Toast.makeText(this, "登录失败，用户或密码错误！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLogining() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.requesting));
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }

    @Override
    public void showValidateError() {
        Toast.makeText(this, "请填写用户名和密码", Toast.LENGTH_SHORT).show();
    }
}
