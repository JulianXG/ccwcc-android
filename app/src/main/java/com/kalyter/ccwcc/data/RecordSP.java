package com.kalyter.ccwcc.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;


/*此类封装了所有关于本地记录的保存，读取，以及当前*/
public class RecordSP{
    public static final String PREFERENCES_NAME = "record";
    private SharedPreferences mSharedPreferences;
    private Context mContext;

    public RecordSP(Context mContext) {
        this.mContext=mContext;
        mSharedPreferences=mContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }



    public void saveRecord(JSONArray record) {
        JSONArray before = JSON.parseArray(mSharedPreferences.getString("record", "[]"));
        for (Object o:record) {
            JSONObject element=(JSONObject)o;
            before.add(element);
        }
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("record", before.toJSONString());
        editor.apply();
    }

    public void overrideRecord(JSONArray record) {
        SharedPreferences.Editor editor=mSharedPreferences.edit();
        editor.putString("record", record.toJSONString());
        editor.apply();
    }

}
