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

    public RecordUtil(Context mContext) {
        this.mContext = mContext;
    }

    public boolean submitRecord(JSONArray record) {
        HttpHelperAsyncTask http=new HttpHelperAsyncTask(mContext);
        JSONObject httpParameter = new JSONObject();
        final String API_LOCATION = "http://ebirdnote.cn/xgweb/api/record/submitrecord";
        httpParameter.put("location", API_LOCATION);
        httpParameter.put("method", "POST");
        httpParameter.put("array", record);
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(LoginSP.PREFERENCES_NAME, Context.MODE_PRIVATE);
        httpParameter.put("token", sharedPreferences.getString("token",null));
        System.out.println(httpParameter.toJSONString());
        try {
            JSONObject result = http.execute(httpParameter).get();
            System.out.println(result.toJSONString());
            if (result != null && result.getString("status_http_helper").equals("ok")) {
                String message = result.getJSONObject("status").getString("message");
                if (message.equals("SERVER_CALL_SUCCESS")) {
                    Toast.makeText(mContext, "上传成功", Toast.LENGTH_SHORT).show();
                    return true;
                }else if (message.equals("SERVER_CALL_ERROR")){
                    Toast.makeText(mContext, "上传失败", Toast.LENGTH_SHORT).show();
                    return false;
                }else if (message.equals("TOKEN_NOT_EXISTS")) {
                    Toast.makeText(mContext, "用户登录信息已过时，正在重新登录", Toast.LENGTH_SHORT).show();
                    LoginSP login = new LoginSP(mContext);
                    login.updateToken();
                    submitRecord(record);
                }else if (message.equals("USER_NAME_OR_PASSWORD_ERROR")){
                    Toast.makeText(mContext, "用户名密码错误，请重新登录", Toast.LENGTH_SHORT).show();
                    mContext.startActivity(new Intent("LOGIN_ACTIVITY"));
                    return false;
                }
            }else if(result.getString("status_http_helper").equals("error")){
                Toast.makeText(mContext, "网络错误，请检查是否有网络", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return true;
    }
}
