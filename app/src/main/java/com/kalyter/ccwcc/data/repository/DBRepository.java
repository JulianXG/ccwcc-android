package com.kalyter.ccwcc.data.repository;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.kalyter.ccwcc.common.Config;
import com.kalyter.ccwcc.data.source.DBSource;
import com.kalyter.ccwcc.model.Bird;
import com.kalyter.ccwcc.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
*此类封装了基本所有此程序设计的本地SQLite数据库操作,包括在安装时候导入的初始数据库
*/
public class DBRepository implements DBSource {
    private static final String ALL_COLUMN = "SELECT id, code, family, genus, " +
            "is_water_bird, is_wetland_depend_bird, name_en, " +
            "name_lt, name_zh, nation, one_percent_standard, `order`, " +
            "protect_class, threatened, zh_pinyin, category FROM bird ";
    private SQLiteDatabase mDatabase;

    public DBRepository(SQLiteDatabase database) {
        mDatabase = database;
    }
    @Override
    public List<Bird> searchBirds(String keyword) {
        String sql = ALL_COLUMN +
                "WHERE name_en LIKE ? " +
                "OR name_lt LIKE ? OR name_zh LIKE ? " +
                "OR zh_pinyin LIKE ?";
        String condition = String.format("%%%s%%", keyword);
        Cursor cursor = mDatabase.rawQuery(sql, new String[]{condition, condition, condition, condition});
        return getBirdsFromCursor(cursor);
    }

    @NonNull
    private List<Bird> getBirdsFromCursor(Cursor cursor) {
        List<Bird> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            Bird bird = new Bird();
            bird.setId(cursor.getInt(0));
            bird.setCode(cursor.getString(1));
            bird.setFamily(cursor.getString(2));
            bird.setGenus(cursor.getString(3));
            bird.setIsWaterBird(Util.parseBoolean(cursor.getInt(4)));
            bird.setIsWetlandDependBird(Util.parseBoolean(cursor.getInt(5)));
            bird.setNameEn(cursor.getString(6));
            bird.setNameLt(cursor.getString(7));
            bird.setNameZh(cursor.getString(8));
            bird.setNation(cursor.getString(9));
            bird.setOnePercentStandard(cursor.getInt(10));
            bird.setOrder(cursor.getString(11));
            bird.setProtectClass(cursor.getInt(12));
            bird.setThreatened(cursor.getString(13));
            bird.setZhPinyin(cursor.getString(14));
            bird.setCategory(cursor.getString(15));
            result.add(bird);
        }
        cursor.close();
        return result;
    }

    @Override
    public Map<String, List<Bird>> getAllBirds() {
        Map<String, List<Bird>> result = new HashMap<>();
        List<String> categories = getCategories();
        String sql = ALL_COLUMN + "WHERE category=?";
        for (String category : categories) {
            Cursor cursor = mDatabase.rawQuery(sql, new String[]{category});
            List<Bird> birds = getBirdsFromCursor(cursor);
            result.put(category, birds);
        }
        return result;
    }

    @Override
    public List<String> getCategories() {
        List<String > species =new ArrayList<>();
        String sql = "SELECT DISTINCT category FROM birds";
        Cursor cursor = mDatabase.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String s = cursor.getString(0);
            if (!s.equals("")) {
                species.add(s);
            }else {
                species.add(Config.NO_SPECIES_NAME_TAG);
            }
        }
        cursor.close();
        return species;
    }
}
