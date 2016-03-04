package com.kalyter.ccwcc.ui.main.tab.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.data.RecordSP;
import com.kalyter.ccwcc.util.LocalRecordCheckedListAdapter;
import com.kalyter.ccwcc.util.RecordUtil;

import java.util.HashMap;

public class LocalRecordActivity extends AppCompatActivity {

    private ListView mList;
    private Button buttonSelectAll,buttonDelete,buttonUpload;
    private Toolbar toolbar;
    private JSONArray record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_record);
        findViews();
        initialRecord();
        initialList();
        initialListeners();
    }

    private void initialRecord() {
        SharedPreferences sharedPreferences = getSharedPreferences(RecordSP.PREFERENCES_NAME, MODE_PRIVATE);
        record = JSON.parseArray(sharedPreferences.getString("record", "[]"));
    }

    private void setListenerToolbar() {
        toolbar.setTitle(getResources().getString(R.string.title_toolbar_local_record));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void findViews() {
        mList= (ListView) findViewById(R.id.list_home_local);
        buttonDelete= (Button) findViewById(R.id.button_home_local_delete);
        buttonSelectAll= (Button) findViewById(R.id.button_home_local_select_all);
        buttonUpload= (Button) findViewById(R.id.button_home_local_upload);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
    }

    private void initialList() {
        LocalRecordCheckedListAdapter adapter = new LocalRecordCheckedListAdapter(this, record);
        mList.setAdapter(adapter);
        mList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    }

    private void initialListeners() {
        setListenerToolbar();
        setListenerSelectAll();
        setListenerDelete();
        setListenerUpload();
    }

    private void setListenerSelectAll() {
        buttonSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < record.size(); i++) {
                    ((LocalRecordCheckedListAdapter) mList.getAdapter()).getIsSelected().put(i, true);
                    ((LocalRecordCheckedListAdapter) mList.getAdapter()).notifyDataSetChanged();

                }
            }
        });
    }

    private void setListenerDelete() {
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<Integer,Boolean> isSelected= ((LocalRecordCheckedListAdapter) mList.getAdapter()).getIsSelected();
                //删除record中的记录
                JSONArray tmpArray= (JSONArray) record.clone();
                record.clear();
                for (int i = 0; i < isSelected.size(); i++) {
                    if (!isSelected.get(i)) {
                        record.add(tmpArray.get(i));
                    }
                }
                //删除isSelected中不是true的值，并将这个map缩减至record大小
                HashMap<Integer,Boolean> clone= (HashMap<Integer, Boolean>) isSelected.clone();
                isSelected.clear();
                int count=0;
                for (int i = 0; i < clone.size(); i++) {
                    if (!clone.get(i)) {
                        isSelected.put(count, false);
                        count++;
                    }
                }
                ((LocalRecordCheckedListAdapter) mList.getAdapter()).notifyDataSetChanged();
                RecordSP recordSP = new RecordSP(LocalRecordActivity.this);
                recordSP.overrideRecord(record);
                Toast.makeText(LocalRecordActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setListenerUpload() {
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordUtil recordUtil = new RecordUtil(LocalRecordActivity.this);
                HashMap<Integer,Boolean> isSelected= ((LocalRecordCheckedListAdapter) mList.getAdapter()).getIsSelected();
                JSONArray uploadRecord = new JSONArray();
                for (int i = 0; i < isSelected.size(); i++) {
                    if (isSelected.get(i)) {
                        uploadRecord.add(record.get(i));
                        record.remove(i);
                    }
                }

                //删除isSelected中不是true的值，并将这个map缩减至record大小
                HashMap<Integer,Boolean> clone= (HashMap<Integer, Boolean>) isSelected.clone();
                isSelected.clear();
                int count=0;
                for (int i = 0; i < clone.size(); i++) {
                    if (!clone.get(i)) {
                        isSelected.put(count, false);
                        count++;
                    }
                }
                recordUtil.submitRecord(uploadRecord);
                ((LocalRecordCheckedListAdapter) mList.getAdapter()).notifyDataSetChanged();
                RecordSP recordSP = new RecordSP(LocalRecordActivity.this);
                recordSP.overrideRecord(record);
            }
        });
    }

}
