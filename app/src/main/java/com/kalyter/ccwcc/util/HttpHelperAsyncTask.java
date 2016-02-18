package com.kalyter.ccwcc.util;

import android.os.AsyncTask;
import android.os.Build;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

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
public class HttpHelperAsyncTask extends AsyncTask<JSONObject,Object,JSONObject> {

    //在这里集成了baiduapikey，因为没有太好的办法，留作下次的改进
    private final String baiduAPIKEY = "d32c029b09c36071eab5684ca53331c2";

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
                JSONObject context=jsonParam.getJSONObject("content");
                byte[] byteContext = context.toJSONString().getBytes("UTF-8");
                dataOutputStream.write(byteContext);
                dataOutputStream.flush();
                dataOutputStream.close();
                connection.connect();
            }
            if(connection.getResponseCode()!= HttpURLConnection.HTTP_OK){
                System.out.println("连接失败，请重试！");
                jsonResponse.put("status", "error");
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
                return jsonResponse;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }


}
