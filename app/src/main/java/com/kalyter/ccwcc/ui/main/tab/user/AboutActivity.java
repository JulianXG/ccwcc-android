package com.kalyter.ccwcc.ui.main.tab.user;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.kalyter.ccwcc.R;

public class AboutActivity extends AppCompatActivity{

    private Toolbar toolbar;
    private TextView textContactUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.titile_toolbar_about));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        textContactUs = (TextView) findViewById(R.id.text_myprofile_contact_us);
        textContactUs.setMovementMethod(LinkMovementMethod.getInstance());
    }

}