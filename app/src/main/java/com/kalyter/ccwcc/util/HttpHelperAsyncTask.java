package com.kalyter.ccwcc.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.kalyter.ccwcc.R;

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
    private final String baiduAPIKEY = "d32c029b09c36071eab5684ca53331c2";
    private AlertDialog mAlertDialog;
    private Context mContext;

    public HttpHelperAsyncTask(Context mContext) {
        this.mContext = mContext;
        LinearLayout layout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_loading, null);
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
            connection.setRequestProperty("apikey",baiduAPIKEY);
            connection.setRequestProperty("devicemodel", Build.DEVICE);
            connection.setRequestProperty("os","Android");
            connection.setRequestProperty("osver", Build.VERSION.RELEASE);
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
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        mAlertDialog.dismiss();
    }

}
