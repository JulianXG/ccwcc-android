package com.kalyter.ccwcc.ui.main.tab.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.util.BarChartUtilActivity;
import com.kalyter.ccwcc.util.SaveFileUtilActivity;
import com.kalyter.ccwcc.util.StatisticsUtil;


public class QuantityActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private LinearLayout layoutChart;
    private Button buttonSaveAsExcel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quantity);
        findViews();
        initialActions();
        initialViews();
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        layoutChart= (LinearLayout) findViewById(R.id.layout_quantity_chart);
        buttonSaveAsExcel= (Button) findViewById(R.id.button_quantity_save_as_excel);
    }

    private void initialViews() {
        initialToolbar();
        setListenerSaveAsExcel();
    }

    private void initialActions() {
        BarChartUtilActivity barChartUtil = new BarChartUtilActivity(QuantityActivity.this);
        StatisticsUtil statisticsUtil = new StatisticsUtil(QuantityActivity.this);
        JSONArray array = statisticsUtil.getAllQuantityArray();
        if (array != null) {
            layoutChart.addView(barChartUtil.getBarChartView(array));
        }else {
            Toast.makeText(QuantityActivity.this, R.string.prompt_sysytem_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void initialToolbar() {
        toolbar.setTitle(getResources().getString(R.string.caption_quantity));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setListenerSaveAsExcel(){
        buttonSaveAsExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("SAVE_FILE_UTIL_ACTIVITY");
                StatisticsUtil statisticsUtil = new StatisticsUtil(QuantityActivity.this);
                intent.putExtra(SaveFileUtilActivity.STRING_EXTRA_KEY, statisticsUtil.getAllQuantityArray().toJSONString());
                startActivity(intent);
            }
        });
    }

}