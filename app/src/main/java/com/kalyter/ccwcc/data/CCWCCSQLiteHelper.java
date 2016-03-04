package com.kalyter.ccwcc.data;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/*
*
*此类封装了基本所有此程序设计的本地SQLite数据库操作,包括在安装时候导入的初始数据库
* */
public class CCWCCSQLiteHelper {


    private SQLiteDatabase ccwccDatabase;
    private Context mContext;
    private final String CCWCC_DATABASE_PATH = "/data/data/com.kalyter.ccwcc/databases/";
    private final String  CCWCC_DATABASE_NAME="ccwcc.db";
    private final String CCWCC_FILE_ABSOLUTE_PATH=CCWCC_DATABASE_PATH+CCWCC_DATABASE_NAME;
    private final int CURRENT_VERSION=1;

    public CCWCCSQLiteHelper(Context context) {
        this.mContext = context;
        File file = new File(CCWCC_FILE_ABSOLUTE_PATH);
        if (file.exists()) {
            if (ccwccDatabase == null) {
                ccwccDatabase = SQLiteDatabase.openOrCreateDatabase(CCWCC_FILE_ABSOLUTE_PATH, null);
            }
        } else {
            File path = new File(CCWCC_DATABASE_PATH);
            path.mkdir();
            try {
                AssetManager assetManager = mContext.getAssets();
                InputStream inputStream = assetManager.open(CCWCC_DATABASE_NAME);
                FileOutputStream outputStream = new FileOutputStream(CCWCC_FILE_ABSOLUTE_PATH);
                byte[] buffer = new byte[1024];
                int count;
                while ((count = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, count);
                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public HashMap<String ,Vector<String >> getBirdsNames() {
        HashMap<String ,Vector<String >> results = new HashMap<>();
        Vector<String> species = getBirdsSpecies();
        String sql = "SELECT code,namezh FROM birds WHERE category=?";
        for (String s : species) {
            Cursor cursor = ccwccDatabase.rawQuery(sql, new String[]{s});
            Vector<String> name = new Vector<>();
            while (cursor.moveToNext()) {
                name.add(cursor.getString(1));
            }
            results.put(s, name);
        }
        return results;
    }

    public Vector<String> getBirdsSpecies() {
        Vector<String > species =new Vector<>();
        String sql = "SELECT DISTINCT category FROM birds";
        Cursor cursor = ccwccDatabase.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            species.add(cursor.getString(0));
        }
        return species;
    }

    public Cursor getBirdAllNamesCursor(String keyWord) {
        Cursor cursor;
        String sql = "SELECT ROWID AS _id,namezh,nameen,namelt,zhpinyin FROM birds WHERE namezh LIKE ? OR nameen LIKE ? OR namelt LIKE ? OR zhpinyin LIKE ?";
        cursor = ccwccDatabase.rawQuery(sql, new String[]{"%"+keyWord+"%","%"+keyWord+"%","%"+keyWord+"%","%"+keyWord+"%"});

        return cursor;
    }

    public  String getCodeByBirdName(String name) {
        String code=null;
        String sql = "SELECT code FROM birds WHERE namezh=?";
        Cursor cursor = ccwccDatabase.rawQuery(sql, new String[]{name});
        if (cursor.moveToNext()) {
            code = cursor.getString(0);
        }
        cursor.close();

        return code;
    }

    public void close(){
        if (ccwccDatabase.isOpen()) {
            ccwccDatabase.close();
        }
    }
}