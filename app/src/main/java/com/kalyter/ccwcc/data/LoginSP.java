package com.kalyter.ccwcc.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.kalyter.ccwcc.util.HttpHelperAsyncTask;

import java.util.concurrent.ExecutionException;


/*此类封装了用户登录类，http请求异步类
* 输入参数为用户名和密码，输出参数为boolean值，是否成功
* */
public class LoginSP {

    private final String LOGIN_API_LOCATION = "http://ebirdnote.cn/xgweb/api/user/login";
    public static final String PREFERENCES_NAME = "user";
    private SharedPreferences mSharedPreference;
    private Context mContext;

    public LoginSP(Context mContext) {
        this.mContext=mContext;
        mSharedPreference = mContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public boolean login(String userName, String password) {
        HttpHelperAsyncTask httpHelperAsyncTask = new HttpHelperAsyncTask(mContext);
        try {
            JSONObject jsonPOST = new JSONObject();
            JSONObject content = new JSONObject();
            content.put("usrname", userName);
            content.put("pwd", password);
            jsonPOST.put("location",LOGIN_API_LOCATION);
            jsonPOST.put("method", "POST");
            jsonPOST.put("content", content);
            JSONObject result = httpHelperAsyncTask.execute(jsonPOST).get();
            Log.i("LoginHelper", result.toJSONString());
            if (result.getJSONObject("status").getInteger("code") == 666) {
                mSharedPreference = mContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=mSharedPreference.edit();
                editor.putString("status", "success");
                editor.putString("username", userName);
                editor.putString("password", password);
                editor.putString("nickname", result.getJSONObject("data").getString("nickname"));
                editor.putString("checkpoint", result.getJSONObject("data").getString("checkpoint"));
                editor.putString("token", result.getJSONObject("data").getString("token"));
                editor.apply();
                return true;
            }else {
                return false;
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateToken(){
        String userName = mSharedPreference.getString("username", "");
        String password=mSharedPreference.getString("password","");
        return login(userName, password);
    }
}