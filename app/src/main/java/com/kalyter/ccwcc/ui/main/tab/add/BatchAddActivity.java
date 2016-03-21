package com.kalyter.ccwcc.ui.main.tab.add;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.util.BirdsExpandableListAdapter;

public class BatchAddActivity extends AppCompatActivity {

    public static final String ARRAY_STRING_KEY = "array";
    private Toolbar toolbar;
    private Button buttonBatchAdd;
    private ExpandableListView list;
    private BirdsExpandableListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_add);
        findViews();
        initialViews();
    }

    private void findViews(){
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        list= (ExpandableListView) findViewById(R.id.expand_batch_add);
        buttonBatchAdd= (Button) findViewById(R.id.button_expand_add);
    }

    private void initialViews(){
        initialToolbar();
        initialBatchAdd();
        initialList();
    }

    private void initialToolbar() {
        toolbar.setTitle(getResources().getString(R.string.caption_batch_add));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initialList() {
        adapter = new BirdsExpandableListAdapter(BatchAddActivity.this);
        list.setAdapter(adapter);
    }

    private void initialBatchAdd() {
        buttonBatchAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(ARRAY_STRING_KEY, adapter.getArrayJSONString());
                BatchAddActivity.this.setResult(0,intent);
                finish();
            }
        });
    }

}
