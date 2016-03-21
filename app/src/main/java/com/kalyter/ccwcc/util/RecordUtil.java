package com.kalyter.ccwcc.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kalyter.ccwcc.data.LoginSP;

import java.util.concurrent.ExecutionException;

public class RecordUtil {

    private Context mContext;
    public static final String CODE_KEY = "code";
    public static final String BIRD_NAME_KEY = "birdName";
    public static final String QUANTITY_KEY = "quantity";
    public static final String LAT_KEY = "lat";
    public static final String LON_KEY = "lon";
    public static final String POSITION_KEY = "position";
    public static final String DETAIL_KEY = "detail";
    public static final String WEATHER_KEY = "weather";
    public static final String DATETIME_KEY = "datetime";
    public static final String RECORD_INDEX_KEY = "recordIndex";


    public RecordUtil(Context mContext) {
        this.mContext = mContext;
    }

    public boolean submitRecord(JSONArray record) {
        HttpHelperAsyncTask http=new HttpHelperAsyncTask(mContext);
        JSONObject httpParameter = new JSONObject();
        httpParameter.put("location", HttpHelperAsyncTask.SUBMIT_RECORD_API);
        httpParameter.put("method", "POST");
        httpParameter.put("array", record);
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(LoginSP.PREFERENCES_NAME, Context.MODE_PRIVATE);
        httpParameter.put("token", sharedPreferences.getString("token",null));
        try {
            JSONObject result = http.execute(httpParameter).get();
            if (result != null && result.getString("status_http_helper").equals("ok")) {
                String message = result.getJSONObject("status").getString("message");
                if (message.equals("SERVER_CALL_SUCCESS")) {
                    Toast.makeText(mContext, "上传成功", Toast.LENGTH_SHORT).show();
                    return true;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return false;
    }
}
