package com.kalyter.ccwcc.util;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.data.ExcelHelper;

import java.io.File;
import java.util.ArrayList;

public class SaveFileUtilActivity extends AppCompatActivity {

	private Toolbar toolbar;
	private Button buttonCancel,buttonConfirm;
	private JSONArray mData;
	private TextView textDirectory;
	private ListView mList;
	private EditText editFileName;
	public static final String STRING_EXTRA_KEY = "array";
	private ArrayAdapter<String> adapter;
	private ArrayList<String> fileList;
	private  String lastPath ;
	public static final String PREFERENCES_NAME = "file";
	private static final String BACK_LEVEL_TAG = "上一级目录";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_save);
		findViews();
		initialActions();
		initialViews();
	}

	private void initialActions() {
		mData= JSON.parseArray(getIntent().getStringExtra(STRING_EXTRA_KEY));
		SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
		lastPath = sharedPreferences.getString("lastPath", "/");
		textDirectory.setText(lastPath);
		fileList = new ArrayList<>();
		if (!lastPath.equals("/")) {
			fileList.add(BACK_LEVEL_TAG);
		}
		for (String s:(new File(lastPath)).list()) {
			fileList.add(s);
		}
		editFileName.setText(sharedPreferences.getString("fileName",""));
	}

	private void initialViews() {
		initialToolbar();
		setListenerConfirm();
		setListenerCancel();
		initialList();
	}

	private void findViews() {
		toolbar= (Toolbar) findViewById(R.id.toolbar);
		buttonConfirm= (Button) findViewById(R.id.button_file_chooser_save_confirm);
		buttonCancel= (Button) findViewById(R.id.button_file_chooser_save_cancel);
		textDirectory= (TextView) findViewById(R.id.text_file_chooser_save_directory);
		mList= (ListView) findViewById(R.id.list_file_chooser_save);
		editFileName= (EditText) findViewById(R.id.edit_file_chooser_save_filename);
	}

	private void initialToolbar() {
		toolbar.setTitle(R.string.title_toolbar_file_save);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void setListenerConfirm() {
		buttonConfirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String path=textDirectory.getText().toString();
				String fileName = editFileName.getText().toString();
				String fullName=path+fileName;
				if (ExcelHelper.saveExcel(fullName, mData)) {
					Toast.makeText(SaveFileUtilActivity.this, R.string.prompt_file_save_success, Toast.LENGTH_SHORT).show();
					SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
					SharedPreferences.Editor editor=sharedPreferences.edit();
					editor.putString("lastPath", path);
					editor.putString("fileName", fileName);
					editor.apply();
				}else {
					Toast.makeText(SaveFileUtilActivity.this, R.string.prompt_file_save_failed, Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void setListenerCancel() {
		buttonCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initialList(){
		adapter = new ArrayAdapter<>(SaveFileUtilActivity.this, R.layout.list_folder,R.id.text_list_folder_directory, fileList);
		mList.setAdapter(adapter);
		mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String path=textDirectory.getText().toString();
				String item = adapter.getItem(position);
				if (!item.equals(BACK_LEVEL_TAG)) {
					path +=item+ "/";
				}else {
					path=path.substring(0,path.substring(0,path.length()-1).lastIndexOf('/')+1);
				}
				textDirectory.setText(path);
				fileList.clear();
				if (!path.equals("/")) {
					fileList.add(BACK_LEVEL_TAG);
				}
				String[] list = (new File(path)).list();
				if (list != null) {
					for (String s : list) {
						fileList.add(s);
					}
				}
				adapter.notifyDataSetChanged();
			}
		});
	}
}