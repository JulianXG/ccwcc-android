package com.kalyter.ccwcc.ui.main.tab.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.data.CCWCCSQLiteHelper;
import com.kalyter.ccwcc.data.LoginSP;
import com.kalyter.ccwcc.util.DateTimePicker;
import com.kalyter.ccwcc.util.FlagUtil;
import com.kalyter.ccwcc.util.SearchViewUtil;
import com.kalyter.ccwcc.util.SingleSelectDialog;

import java.util.Date;

public class FlagActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SearchView mSearchView;
    private TextView textBirdName,textFlagColor,textLeftHoop,textRightHoop, textDiscoverTime;
    private EditText editFlagCode,editRemarks;
    private Button buttonUpdate;
    private LinearLayout layoutFlagColor,layoutLeftHoop,layoutRightHoop,layoutDiscoverTime;
    private JSONObject flagData = new JSONObject();
    public static final String EMPTY_STRING = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flag);
        findViews();
        initialViews();
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mSearchView = (SearchView) findViewById(R.id.search_flag);
        textBirdName = (TextView) findViewById(R.id.text_flag_bird_name);
        textFlagColor = (TextView) findViewById(R.id.text_flag_flag_color);
        textLeftHoop = (TextView) findViewById(R.id.text_flag_left_hoop);
        textRightHoop = (TextView) findViewById(R.id.text_flag_right_hoop);
        textDiscoverTime = (TextView) findViewById(R.id.text_flag_discover_time);
        editFlagCode = (EditText) findViewById(R.id.edit_flag_flag_code);
        editRemarks = (EditText) findViewById(R.id.edit_flag_remarks);
        layoutFlagColor = (LinearLayout) findViewById(R.id.layout_flag_flag_color);
        layoutLeftHoop = (LinearLayout) findViewById(R.id.layout_flag_left_hoop);
        layoutRightHoop = (LinearLayout) findViewById(R.id.layout_flag_right_hoop);
        layoutDiscoverTime = (LinearLayout) findViewById(R.id.layout_flag_discover_time);
        buttonUpdate= (Button) findViewById(R.id.button_flag_upload);
    }

    private void initialViews(){
        initialToolbar();
        initialDiscoverTime();
        initialLeftHoop();
        initialRightHoop();
        initialFlagColor();
        initialSearchView();
        initialUpdate();
    }

    private void initialToolbar() {
        toolbar.setTitle(getResources().getString(R.string.caption_flag));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initialDiscoverTime(){
        textDiscoverTime.setText(DateTimePicker.DATE_FORMAT.format(new Date()));
        layoutDiscoverTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DateTimePicker(FlagActivity.this, textDiscoverTime).showDateTimePickerDialog();
            }
        });
    }

    private void initialLeftHoop(){
        layoutLeftHoop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleSelectDialog leftSelect = new SingleSelectDialog(FlagActivity.this, textLeftHoop);
                leftSelect.showHoopSingleSelectDialog();
            }
        });
    }

    private void initialRightHoop(){
        layoutRightHoop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleSelectDialog rightSelect = new SingleSelectDialog(FlagActivity.this, textRightHoop);
                rightSelect.showHoopSingleSelectDialog();
            }
        });
    }

    private void initialFlagColor(){
        layoutFlagColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleSelectDialog flagColorSelect = new SingleSelectDialog(FlagActivity.this, textFlagColor);
                flagColorSelect.showFlagSingleSelectDialog();
            }
        });
    }

    private void initialSearchView() {
        SearchViewUtil searchViewUtil = new SearchViewUtil(mSearchView, textBirdName);
        searchViewUtil.setDefaultAdapter();
    }

    private void initialUpdate(){
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInformationIntegrity()) {
                    addFlagData();
                    System.out.println("flagData = " + flagData);
                    FlagUtil flagUtil = new FlagUtil(FlagActivity.this);
                    if (flagUtil.submitFlag(flagData)){
                        clearAllViews();
                    }
                } else {
                    Toast.makeText(FlagActivity.this, R.string.prompt_information_incomplete, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkInformationIntegrity(){
        if (textBirdName.getText().equals(EMPTY_STRING)
                ||textFlagColor.getText().equals(EMPTY_STRING)
                ||textLeftHoop.getText().equals(EMPTY_STRING)
                ||textRightHoop.getText().equals(EMPTY_STRING)
                ||editFlagCode.getText().equals(EMPTY_STRING)){
            return false;
        }
        return true;
    }

    private void addFlagData(){
        flagData.clear();
        CCWCCSQLiteHelper helper = new CCWCCSQLiteHelper(FlagActivity.this);

        flagData.put("code", helper.getCodeByBirdName(textBirdName.getText().toString()));
        flagData.put("flagColor", textFlagColor.getText().toString());
        flagData.put("leftBandsColor", textLeftHoop.getText().toString());
        flagData.put("rightBandsColor", textRightHoop.getText().toString());
        flagData.put("flagCode", editFlagCode.getText().toString());
        flagData.put("discoveredTime", textDiscoverTime.getText().toString());
        flagData.put("note", editRemarks.getText().toString());
        SharedPreferences sharedPreferences = getSharedPreferences(LoginSP.PREFERENCES_NAME, MODE_PRIVATE);
        flagData.put("observer", sharedPreferences.getString("userName", EMPTY_STRING));

        helper.close();
    }

    private void clearAllViews(){
        textBirdName.setText(EMPTY_STRING);
        editFlagCode.setText(EMPTY_STRING);
        editRemarks.setText(EMPTY_STRING);
        flagData.clear();
    }
}
