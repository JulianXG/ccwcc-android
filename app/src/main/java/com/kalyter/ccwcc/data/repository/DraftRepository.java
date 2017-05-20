package com.kalyter.ccwcc.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.kalyter.ccwcc.common.Config;
import com.kalyter.ccwcc.data.source.DraftSource;
import com.kalyter.ccwcc.model.Record;
import com.kalyter.ccwcc.util.Util;

import java.util.ArrayList;
import java.util.List;


/**
 * 此类封装了所有关于本地记录的保存，读取，以及当前
 * */
public class DraftRepository implements DraftSource {
    private SharedPreferences mSharedPreferences;
    private Context mContext;
    private static final String KEY_RECORD = "RECORD";

    public DraftRepository(Context context) {
        mContext=context;
        mSharedPreferences=mContext.getSharedPreferences(Config.SP, Context.MODE_PRIVATE);
    }

    @Override
    public void save(List<Record> recordList) {
        String string = mSharedPreferences.getString(KEY_RECORD, "[]");
        List<Record> records= Util.deserializeArray(string, Record[].class);
        records.addAll(recordList);
        updateAll(records);
    }

    @Override
    public void save(Record record) {
        List<Record> oneRecord = new ArrayList<>();
        oneRecord.add(record);
        save(oneRecord);
    }

    @Override
    public List<Record> getAllRecords() {
        String result = mSharedPreferences.getString(KEY_RECORD, "[]");
        return Util.deserializeArray(result, Record[].class);
    }

    @Override
    public void updateAll(List<Record> records) {
        SharedPreferences.Editor editor=mSharedPreferences.edit();
        editor.putString(KEY_RECORD, Util.serialize(records));
        editor.apply();
    }

    @Override
    public void delete(int index) {
        List<Record> records = getAllRecords();
        records.remove(index);
        updateAll(records);
    }

    @Override
    public void delete(List<Record> records) {
        List<Record> pre = getAllRecords();
        for (Record record: records) {
            pre.remove(record);
        }
        updateAll(pre);
    }
}
