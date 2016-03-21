package com.kalyter.ccwcc.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.kalyter.ccwcc.data.LoginSP;

import java.util.concurrent.ExecutionException;

public class FlagUtil {

    private Context mContext;

    public FlagUtil(Context mContext) {
        this.mContext = mContext;
    }

    public boolean submitFlag(JSONObject flagData) {
        HttpHelperAsyncTask httpHelperAsyncTask = new HttpHelperAsyncTask(mContext);
        JSONObject parameter = new JSONObject();
        parameter.put("location", HttpHelperAsyncTask.SUBMIT_FLAG_API);
        parameter.put("method", "POST");
        parameter.put("content", flagData);
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(LoginSP.PREFERENCES_NAME, Context.MODE_PRIVATE);
        parameter.put("token", sharedPreferences.getString("token", null));
        try {
            JSONObject result = httpHelperAsyncTask.execute(parameter).get();
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
