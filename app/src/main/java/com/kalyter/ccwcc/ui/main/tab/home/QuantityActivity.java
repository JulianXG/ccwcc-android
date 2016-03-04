package com.kalyter.ccwcc.ui.main.tab.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.data.LoginSP;


public class QuantityActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button buttonSaveAsExcel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quantity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        buttonSaveAsExcel = (Button) findViewById(R.id.button_quantity_save_as_excel);
        initialToolbar();
    }

    private void initialToolbar() {
        toolbar.setTitle(getResources().getString(R.string.title_toolbar_quantity));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
