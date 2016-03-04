package com.kalyter.ccwcc.ui.launch;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.data.LoginSP;

public class LoginActivity extends AppCompatActivity {

    private Button buttonLogin;
    private EditText editUsername,editPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();
        addListenerToLogin();
    }

    private void findViews() {
        buttonLogin = (Button) findViewById(R.id.button_login_login);
        editPassword = (EditText) findViewById(R.id.edit_login_password);
        editUsername = (EditText) findViewById(R.id.edit_login_user);
    }

    private void addListenerToLogin() {
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginSP loginSP = new LoginSP(LoginActivity.this);
                String userName= String.valueOf(editUsername.getText());
                String password= String.valueOf(editPassword.getText());
                if (!userName.equals("") && !password.equals("")) {
                    if (loginSP.login(userName,password)){
                        //登陆成功，进入主界面
                        Intent startMainActivity = new Intent("MAIN_ACTIVITY");
                        startActivity(startMainActivity);
                        finish();
                    }else {
                        new AlertDialog.Builder(buttonLogin.getContext()).setTitle("登录失败")
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .setMessage("用户名或密码错误，请重新登录！")
                                .setPositiveButton("我知道了",null).create().show();
                    }
                }else {
                    new AlertDialog.Builder(buttonLogin.getContext())
                            .setTitle("输入有误")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setMessage("用户名或密码没有输入，请重新输入")
                            .setPositiveButton("我知道了",null).create().show();
                }
            }
        });
    }
}
