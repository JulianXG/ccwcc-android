package com.kalyter.ccwcc.util;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.kalyter.ccwcc.R;

import java.util.ArrayList;
import java.util.HashMap;

public class SingleSelectDialog {

    private ListView mListView;
    private Context mContext;
    private TextView mTextView;
    private AlertDialog mAlertDialog;
    private SimpleAdapter mAdapter;
    private ArrayList<HashMap<String, Object>> hoops = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> flags = new ArrayList<>();
    public static final String TITLE_TAG = "title";
    public static final String IMAGE_TAG = "image";
    private String[] hoopsTitle = new String[]{"石灰绿环", "绿环", "红环", "蓝环", "黄环", "白环"};
    private String[] hoopsCode = new String[]{"l", "g", "r", "b", "y", "w"};
    private int[] hoopsImage = new int[]{R.drawable.ic_hoop_l
            , R.drawable.ic_hoop_g
            , R.drawable.ic_hoop_r
            , R.drawable.ic_hoop_b
            , R.drawable.ic_hoop_y
            , R.drawable.ic_hoop_w};

    private String[] flagsTitle = new String[]{"黑旗标", "橙旗标", "白旗标", "黄旗标", "红旗标", "绿旗标"};
    private String[] flagsCode = new String[]{"B", "O", "W", "Y", "R", "G"};
    private int[] flagsImage = new int[]{R.drawable.ic_flag_b
            , R.drawable.ic_flag_o
            , R.drawable.ic_flag_w
            , R.drawable.ic_flag_y
            , R.drawable.ic_flag_r
            , R.drawable.ic_flag_g};

    public SingleSelectDialog(Context mContext,TextView mTextView) {
        this.mContext = mContext;
        this.mTextView=mTextView;
        mListView = new ListView(mContext);
        mAlertDialog=new AlertDialog.Builder(mContext).setView(mListView).create();
    }

    private void initialHoops(){
        for (int i = 0; i < hoopsTitle.length; i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put(TITLE_TAG, hoopsTitle[i]);
            map.put(IMAGE_TAG, hoopsImage[i]);
            hoops.add(map);
        }
        mAdapter = new SimpleAdapter(mContext, hoops
                ,R.layout.list_single_select
                , new String[]{TITLE_TAG, IMAGE_TAG}
                , new int[]{R.id.text_single_select, R.id.image_list_single_select});
        mListView.setAdapter(mAdapter);
    }

    private void initialFlags(){
        for (int i = 0; i < flagsTitle.length; i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put(TITLE_TAG, flagsTitle[i]);
            map.put(IMAGE_TAG, flagsImage[i]);
            flags.add(map);
        }
        mAdapter = new SimpleAdapter(mContext, flags
                , R.layout.list_single_select
                , new String[]{TITLE_TAG, IMAGE_TAG}
                , new int[]{R.id.text_single_select, R.id.image_list_single_select});
        mListView.setAdapter(mAdapter);
    }

    public void showHoopSingleSelectDialog() {
        initialHoops();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mTextView.setText(hoopsCode[position]);
                mAlertDialog.dismiss();
            }
        });
        mAlertDialog.show();
    }

    public void showFlagSingleSelectDialog(){
        initialFlags();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mTextView.setText(flagsCode[position]);
                mAlertDialog.dismiss();
            }
        });
        mAlertDialog.show();
    }


}
