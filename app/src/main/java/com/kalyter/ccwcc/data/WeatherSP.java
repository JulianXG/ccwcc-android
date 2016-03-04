package com.kalyter.ccwcc.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.util.HttpHelperAsyncTask;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;


/*
* 封装了通过HttpHelperAsyncTask得到天气数据并存入SharedPreference的操作
* */
public class WeatherSP {
    private final String WEATHER_INTERFACE_LOCATION ="http://apis.baidu.com/heweather/weather/free?city=";
    public static final String PREFERENCES_NAME = "weather";
    private SharedPreferences sharedPreferences;
    private Context mContext;
    private String mCity;
    private String weather;
    private String  temperature;

    public WeatherSP(Context mContext) {
        this.mContext=mContext;
        sharedPreferences = this.mContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public boolean saveWeather(String city) {
        boolean result=false;
        HttpHelperAsyncTask httpHelperAsyncTask=new HttpHelperAsyncTask(mContext);
        try {
            //转码成iso-8859-1，支持调用接口传入中文
            mCity=city;
            mCity=new String(mCity.getBytes("utf-8"),"iso-8859-1");
            String strUrl = WEATHER_INTERFACE_LOCATION + mCity;
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("method", "GET");
            jsonRequest.put("location", strUrl);
            //获得天气JSON
            JSONObject jsonGET=httpHelperAsyncTask.execute(jsonRequest).get();
            JSONObject jsonWeather = new JSONObject();
            //处理JSON,第一次判断status是判断是否http通讯成功
            if (jsonGET!=null&&jsonGET.getString("status_http_helper").equals("ok")) {
                JSONArray content = jsonGET.getJSONArray("HeWeather data service 3.0");
                String status = content.getJSONObject(0).getString("status");
                if (status.equals("ok")) {
                    JSONObject now = content.getJSONObject(0).getJSONObject("now");
                    weather = now.getJSONObject("cond").getString("txt");
                    JSONObject wholeDay = content.getJSONObject(0).getJSONArray("daily_forecast").getJSONObject(0);
                    JSONObject temp = wholeDay.getJSONObject("tmp");
                    temperature = temp.getInteger("min") + "~" + temp.getInteger("max") + "℃";
                    jsonWeather.put("weather", weather);
                    jsonWeather.put("temperature", temperature);
                    jsonWeather.put("status", status);
                    result=true;
                }else {
                    jsonWeather.put("status", mContext.getResources().getString(R.string.weather_obtain_error_message));
                }
            }else{
                jsonWeather.put("status", mContext.getResources().getString(R.string.network_error_message));
            }

            //存入SharedPreference
            SharedPreferences.Editor editor=sharedPreferences.edit();
            if (jsonWeather.getString("status").equals("ok")) {
                try {
                    editor.putString("status", "ok");
                    editor.putString("city", new String(mCity.getBytes("iso-8859-1"),"utf-8"));
                    editor.putString("weather", weather);
                    editor.putString("temperature", temperature);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }else {
                editor.putString("status", jsonWeather.getString("status"));
            }
            editor.commit();
        } catch (UnsupportedEncodingException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return result;
    }

}
