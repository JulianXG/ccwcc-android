package com.kalyter.ccwcc.ui.main.tab.home;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.data.WeatherHelperSP;


public class HomeFragment extends Fragment {
    private View mView;//定义了这个Fragment里面的view
    private TextView textWeatherInformation;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        findViews(inflater, container);
        return mView;
    }

    private void findViews(LayoutInflater inflater, ViewGroup container){
        //判断是否为第一次加载view，否则用上一次保存的view
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_home, container, false);
        }
        // 缓存的view需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个view已经有parent的错误。
        ViewGroup parent=(ViewGroup) mView.getParent();
        if (parent != null) {
            parent.removeView(mView);
        }
        textWeatherInformation=(TextView) mView.findViewById(R.id.text_home_weather_information);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //刷新天气信息
        refreshWeatherInformation();
    }

    public void refreshWeatherInformation() {
        SharedPreferences sharedPreferences = mView.getContext().getSharedPreferences(WeatherHelperSP.PREFERENCES_NAME, Context.MODE_PRIVATE);
        String status = sharedPreferences.getString("status", "");
        if (status.equals("ok")) {
            String content = sharedPreferences.getString("city", "");
            content += "\n" + sharedPreferences.getString("weather", "")
                    + "," + sharedPreferences.getString("temperature", "");
            textWeatherInformation.setText(content);
        } else {
            textWeatherInformation.setText(status);
        }
    }

}
