package com.kalyter.ccwcc.data.repository;

import android.content.Context;
import android.content.SharedPreferences;



/*此类封装了用户登录类，http请求异步类
* 输入参数为用户名和密码，输出参数为boolean值，是否成功
* */
public class LoginSP {

    public static final String PREFERENCES_NAME = "user";
    public static final String USER_NAME_KEY = "userName";
    public static final String NICKNAME_KEY = "nickname";
    public static final String CHECK_POINT_KEY = "checkPoint";
    public static final String STATUS_KEY = "status";
    private SharedPreferences mSharedPreference;
    private Context mContext;

    public LoginSP(Context mContext) {
        this.mContext=mContext;
        mSharedPreference = mContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public boolean login(String userName, String password) {
//        HttpHelperAsyncTask httpHelperAsyncTask = new HttpHelperAsyncTask(mContext);
//        try {
//            JSONObject jsonPOST = new JSONObject();
//            JSONObject content = new JSONObject();
//            content.put(USER_NAME_KEY, userName);
//            content.put("password", password);
//            jsonPOST.put("location",HttpHelperAsyncTask.LOGIN_API_LOCATION);
//            jsonPOST.put("method", "POST");
//            jsonPOST.put("content", content);
//            JSONObject result = httpHelperAsyncTask.execute(jsonPOST).get();
//            Log.i("LoginHelper", result.toJSONString());
//            if (result.getJSONObject("status").getInteger("code") == 666) {
//                mSharedPreference = mContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor=mSharedPreference.edit();
//                editor.putString(STATUS_KEY, "success");
//                editor.putString(USER_NAME_KEY, userName);
//                editor.putString("password", password);
//                editor.putString(NICKNAME_KEY, result.getJSONObject("data").getString(NICKNAME_KEY));
//                editor.putString(CHECK_POINT_KEY, result.getJSONObject("data").getString(CHECK_POINT_KEY));
//                editor.putString("token", result.getJSONObject("data").getString("token"));
//                editor.apply();
//                return true;
//            }else {
//                return false;
//            }
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }

        return false;
    }

    public boolean updateToken(){
        String userName = mSharedPreference.getString(USER_NAME_KEY, "");
        String password=mSharedPreference.getString("password","");
        return login(userName, password);
    }
}
