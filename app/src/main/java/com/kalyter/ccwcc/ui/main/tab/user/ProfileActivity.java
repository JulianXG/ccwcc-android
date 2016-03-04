package com.kalyter.ccwcc.ui.main.tab.user;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.data.LoginSP;

public class ProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView textUserName,textNickname,textCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initialViews();
        initialToolbar();
        initialProfiles();
    }

    private void initialProfiles() {
        SharedPreferences sharedPreferences = getSharedPreferences(LoginSP.PREFERENCES_NAME, MODE_PRIVATE);
        textUserName.setText(sharedPreferences.getString("username",""));
        textNickname.setText(sharedPreferences.getString("nickname",""));
        textCity.setText(sharedPreferences.getString("checkpoint",""));
    }

    private void initialToolbar() {
        toolbar.setTitle(getResources().getString(R.string.title_toolbar_profile));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initialViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        textUserName = (TextView) findViewById(R.id.text_profile_user_name);
        textNickname = (TextView) findViewById(R.id.text_profile_nickname);
        textCity = (TextView) findViewById(R.id.text_profile_city);
    }


}
