package com.kalyter.ccwcc.ui.launch;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.data.LoginSP;

import java.util.Timer;
import java.util.TimerTask;

public class LaunchActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        SharedPreferences sharedPreferences = getSharedPreferences(LoginSP.PREFERENCES_NAME, MODE_PRIVATE);
        final Intent intent = new Intent();
        if(sharedPreferences.getString("status","").equals("success")){
            intent.setAction( "MAIN_ACTIVITY");
        }else {
            intent.setAction("LOGIN_ACTIVITY");
        }
        final int SPLASH_DISPLAY_TIME=1000;//单位毫秒,测试时设为0
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(intent);
                LaunchActivity.this.finish();
            }
        }, SPLASH_DISPLAY_TIME);
    }
}
