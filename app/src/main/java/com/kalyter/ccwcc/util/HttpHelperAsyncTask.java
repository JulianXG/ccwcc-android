package com.kalyter.ccwcc.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.data.LoginSP;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/*此类封装了在此app中的所有的http请求，已经封装好了我们服务器的header，
* 输入参数JSONObject，地址(location包括参数)、方法类型(method)和如果有的话POST内容(content)JSONObject类型
* 返回从http端接收到的JSON信息
* */
public class HttpHelperAsyncTask extends AsyncTask<JSONObject,Void,JSONObject> {

    //在这里集成了baiduapikey，因为没有太好的办法，留作下次的改进
    private final String BAIDU_API = "d32c029b09c36071eab5684ca53331c2";
    private AlertDialog mAlertDialog;
    private Context mContext;
    public static final String EBIRDNOTE_ROOT="http://ebirdnote.cn/xgweb/api/";
    public static final String SUBMIT_RECORD_API = EBIRDNOTE_ROOT+"record/submit";
    public static final String LOGIN_API_LOCATION = EBIRDNOTE_ROOT +"user/login";
    public static final String WEATHER_INTERFACE_LOCATION ="http://apis.baidu.com/heweather/weather/free?city=";
    public static final String ALL_QUANTITY_API = EBIRDNOTE_ROOT+"record/quantity";
    public static final String DATE_CATEGORY_LIST_API = EBIRDNOTE_ROOT+"record/trend";
    public static final String SUBMIT_FLAG_API = EBIRDNOTE_ROOT + "flag/add";


    public HttpHelperAsyncTask(Context mContext) {
        this.mContext = mContext;
        LinearLayout layout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.dialog_loading, null);
        mAlertDialog = new AlertDialog.Builder(mContext)
                .setView(layout)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onCancelled();
                        mAlertDialog.dismiss();
                    }
                })
                .setCancelable(false)
                .create();
        mAlertDialog.show();
    }

    @Override
    protected JSONObject doInBackground(JSONObject... params) {
        JSONObject jsonParam = params[0];
        JSONObject jsonResponse = new JSONObject();
        try {
            URL url = new URL(jsonParam.getString("location"));
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("apikey", BAIDU_API);
            connection.setRequestProperty("deviceModel", Build.DEVICE);
            connection.setRequestProperty("os","Android");
            connection.setRequestProperty("osVersion", Build.VERSION.RELEASE);
            connection.setRequestProperty("token",jsonParam.getString("token"));
            String method=jsonParam.getString("method");
            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Accept-Charset", "utf-8");
            connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            if(method.equals("POST")){
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");
                connection.setUseCaches(false);
                connection.setInstanceFollowRedirects(false);
                connection.connect();
                DataOutputStream dataOutputStream=new DataOutputStream(connection.getOutputStream());
                byte[] byteContext;
                JSONObject context=jsonParam.getJSONObject("content");
                if (context == null) {
                    JSONArray array = jsonParam.getJSONArray("array");
                    byteContext = array.toJSONString().getBytes("UTF-8");
                } else {
                    byteContext = context.toJSONString().getBytes("UTF-8");
                }
                dataOutputStream.write(byteContext);
                dataOutputStream.flush();
                dataOutputStream.close();
                connection.connect();
            }
            if(connection.getResponseCode()!= HttpURLConnection.HTTP_OK){
                jsonResponse.put("status_http_helper", "error");
            }else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line, content;
                StringBuilder stringBuilder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                reader.close();
                content = new String(stringBuilder.toString().getBytes("UTF-8"));
                jsonResponse = JSON.parseObject(content);
                jsonResponse.put("status_http_helper", "ok");
            }
        } catch (JSONException|IOException e) {
            e.printStackTrace();
            return null;
        }
        return jsonResponse;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        mAlertDialog.dismiss();
        //一些常用的错误信息处理，大部分采用Toast来通知用户
        if (result != null && result.getString("status_http_helper").equals("ok")) {
            //检测是否为对ebirdnote.cn的http请求
            if (result.getJSONObject("status") != null) {
                String message = result.getJSONObject("status").getString("message");
                if (message.equals("SERVER_CALL_ERROR")) {
                    Toast.makeText(mContext, "对服务器请求失败，请重试", Toast.LENGTH_SHORT).show();
                } else if (message.equals("TOKEN_NOT_EXISTS")) {
                    Toast.makeText(mContext, "用户登录信息已过时，正在重新登录", Toast.LENGTH_SHORT).show();
                    LoginSP login = new LoginSP(mContext);
                    login.updateToken();
                } else if (message.equals("USER_NAME_OR_PASSWORD_ERROR")) {
                    Toast.makeText(mContext, "用户名密码错误，请重新登录", Toast.LENGTH_SHORT).show();
                    mContext.startActivity(new Intent("LOGIN_ACTIVITY"));
                }
            }
        }else if(result==null||result.getString("status_http_helper").equals("error")){
            Toast.makeText(mContext, "网络错误，请检查是否有网络", Toast.LENGTH_SHORT).show();
        }
    }

}
