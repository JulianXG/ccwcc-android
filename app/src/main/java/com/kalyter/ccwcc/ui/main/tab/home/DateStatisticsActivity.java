package com.kalyter.ccwcc.ui.main.tab.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.util.BarChartUtilActivity;
import com.kalyter.ccwcc.util.SaveFileUtilActivity;
import com.kalyter.ccwcc.util.StatisticsUtil;
import com.kalyter.ccwcc.util.MyDatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateStatisticsActivity extends AppCompatActivity {

    private LinearLayout layoutStartDate,layoutEndDate,layoutChart;
    private Button buttonSaveAsExcel,buttonGetChart;
    private Toolbar toolbar;
    private TextView textStart,textEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_statistics);
        findViews();
        initialActions();
        initialViews();
    }

    private void findViews() {
        layoutChart = (LinearLayout) findViewById(R.id.layout_date_statistics_chart);
        layoutStartDate = (LinearLayout) findViewById(R.id.layout_date_statistics_start_time);
        layoutEndDate = (LinearLayout) findViewById(R.id.layout_date_statistics_end_time);
        buttonSaveAsExcel = (Button) findViewById(R.id.button_date_statistics_save_as_excel);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        textStart = (TextView) findViewById(R.id.text_date_statistics_start_time);
        textEnd = (TextView) findViewById(R.id.text_date_statistics_end_time);
        buttonGetChart = (Button) findViewById(R.id.button_date_statistics_get_chart);
    }

    private void initialViews(){
        initialStartDate();
        initialSaveAsExcel();
        initialEndDate();
    }

    private void initialActions(){
        initialToolbar();
        initialGetChart();
    }

    private void initialToolbar() {
        toolbar.setTitle(getResources().getString(R.string.caption_date_statistics));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initialStartDate(){
        layoutStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatePicker myDatePicker = new MyDatePicker(DateStatisticsActivity.this, textStart);
                myDatePicker.showYearMonthPickerDialog();
            }
        });
    }

    private void initialEndDate(){
        layoutEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatePicker myDatePicker = new MyDatePicker(DateStatisticsActivity.this, textEnd);
                myDatePicker.showYearMonthPickerDialog();
            }
        });
    }

    private void initialSaveAsExcel(){
        buttonSaveAsExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDateComplete()) {
                    StatisticsUtil statisticsUtil = new StatisticsUtil(DateStatisticsActivity.this);
                    JSONArray array = statisticsUtil.getDateQuantityArray(textStart.getText().toString(), textEnd.getText().toString());
                    if (array != null) {
                        Intent intent = new Intent("SAVE_FILE_UTIL_ACTIVITY");
                        intent.putExtra(SaveFileUtilActivity.STRING_EXTRA_KEY, array.toJSONString());
                        startActivity(intent);
                    }else {
                        Toast.makeText(DateStatisticsActivity.this, R.string.prompt_sysytem_error, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void initialGetChart(){
        buttonGetChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDateComplete()) {
                    StatisticsUtil statisticsUtil = new StatisticsUtil(DateStatisticsActivity.this);
                    BarChartUtilActivity barChartUtilActivity = new BarChartUtilActivity(DateStatisticsActivity.this);
                    JSONArray data = statisticsUtil.getDateQuantityArray(textStart.getText().toString(), textEnd.getText().toString());

                    View dateBarChartView = barChartUtilActivity.getBarChartView(data);
                    if (dateBarChartView != null) {
                        layoutChart.removeAllViews();
                        layoutChart.addView(dateBarChartView);
                    } else {
                        Toast.makeText(DateStatisticsActivity.this, R.string.prompt_sysytem_error, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean isDateComplete() {
        String startDate= textStart.getText().toString();
        String endDate = textEnd.getText().toString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        try {
            if (!simpleDateFormat.parse(startDate).before(simpleDateFormat.parse(endDate))) {
                Toast.makeText(DateStatisticsActivity.this, R.string.prompt_date_incorrect, Toast.LENGTH_SHORT).show();
            }else {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(DateStatisticsActivity.this, R.string.prompt_date_uncomplete, Toast.LENGTH_SHORT).show();
            return false;
        }
        return false;
    }
}
