package com.kalyter.ccwcc.ui.main.drawer;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.data.WeatherSP;

public class LeftDrawerFragment extends Fragment{

    private View mView;//定义了这个Fragment里面的view
    private String[] cities;
    private ListView cityList;
    private TextView textCurrentCity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        findViews(inflater, container);
        return mView;
    }

    private void findViews(LayoutInflater inflater, ViewGroup container){
        //判断是否为第一次加载view，否则用上一次保存的view
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_left_drawer, container, false);
        }
        // 缓存的view需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个view已经有parent的错误。
        ViewGroup parent=(ViewGroup) mView.getParent();
        if (parent != null) {
            parent.removeView(mView);
        }
        cityList = (ListView) mView.getRootView().findViewById(R.id.list_view_left_drawer_city);
        textCurrentCity = (TextView) mView.findViewById(R.id.text_left_drawer_current_city);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addListenerToCityList();
        initialActions();
    }

    private void initialActions() {
        //第一次先刷新并存入默认城市的信息，如果有上次城市信息则用上次城市
        SharedPreferences sharedPreferences = mView.getContext().getSharedPreferences(WeatherSP.PREFERENCES_NAME, Context.MODE_PRIVATE);
        String city = sharedPreferences.getString("city", "");
        if (city.equals("")) {
            //如果记录为空则使用默认城市
            city=mView.getResources().getString(R.string.default_city_name);
        }
        WeatherSP weatherSP = new WeatherSP(mView.getContext());
        weatherSP.saveWeather(city);
        textCurrentCity.setText(city);
    }

    private void addListenerToCityList() {
        //侧边城市列表
        cities = mView.getResources().getStringArray(R.array.city_name_arrays);
        String citiesDisplay[] = mView.getResources().getStringArray(R.array.city_name_display_arrays);
        cityList.setAdapter(new ArrayAdapter<>(mView.getContext(), android.R.layout.simple_list_item_1, citiesDisplay));
        cityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //更新天气信息
                WeatherSP weatherSP = new WeatherSP(cityList.getRootView().getContext());
                String city = cities[(int) id];
                if(weatherSP.saveWeather(city)){
                    textCurrentCity.setText(city);
                    new AlertDialog.Builder(cityList.getContext())
                            .setTitle("信息")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setMessage("获取" + city +"天气完成")
                            .setPositiveButton("我知道了", null).create().show();
                }else {
                    SharedPreferences sharedPreferences = mView.getContext().getSharedPreferences(weatherSP.PREFERENCES_NAME, Context.MODE_PRIVATE);
                    new AlertDialog.Builder(cityList.getContext())
                            .setTitle("警告")
                            .setIcon(android.R.drawable.stat_sys_warning)
                            .setMessage(sharedPreferences.getString("status","获取天气失败"))
                            .setPositiveButton("我知道了", null).create().show();
                }
            }
        });
    }

}
