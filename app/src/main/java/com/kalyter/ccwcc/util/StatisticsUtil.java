package com.kalyter.ccwcc.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kalyter.ccwcc.data.LoginSP;

import java.util.Date;
import java.util.concurrent.ExecutionException;

public class StatisticsUtil {

	private Context mContext;

	public StatisticsUtil(Context mContext) {
		this.mContext = mContext;
	}

	public JSONArray getAllQuantityArray() {
		JSONObject parameters = new JSONObject();
		JSONObject result;
		JSONArray resultArray = null;
		parameters.put("location", HttpHelperAsyncTask.ALL_QUANTITY_API);
		parameters.put("method", "GET");
		HttpHelperAsyncTask httpHelperAsyncTask = new HttpHelperAsyncTask(mContext);
		try {
			result = httpHelperAsyncTask.execute(parameters).get();
			if (result != null && result.getString("status_http_helper").equals("ok")) {
				String message = result.getJSONObject("status").getString("message");
				if (message.equals("SERVER_CALL_SUCCESS")) {
					resultArray = result.getJSONArray("data");
				}
			}

		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		return resultArray;
	}

    public JSONArray getDateQuantityArray(String  startDate,String  endDate) {
        JSONObject parameters = new JSONObject();
        JSONArray resultArray=null;
        parameters.put("location", HttpHelperAsyncTask.DATE_CATEGORY_LIST_API);
        parameters.put("method", "POST");
        JSONObject tmp = new JSONObject();
        tmp.put("startDate", startDate);
        tmp.put("endDate", endDate);
        parameters.put("content", tmp);
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(LoginSP.PREFERENCES_NAME, Context.MODE_PRIVATE);
        parameters.put("token", sharedPreferences.getString("token", null));
        HttpHelperAsyncTask httpHelperAsyncTask = new HttpHelperAsyncTask(mContext);
        try {
            JSONObject result=httpHelperAsyncTask.execute(parameters).get();
            if (result.getString("status_http_helper").equals("ok")) {
                String message = result.getJSONObject("status").getString("message");
                if (message.equals("SERVER_CALL_SUCCESS")) {
                    resultArray = result.getJSONArray("data");
                }
            }
        } catch (InterruptedException | ExecutionException|NullPointerException e) {
            e.printStackTrace();
        }
        return resultArray;
    }
}