package com.kalyter.ccwcc.ui.main.tab.add;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.data.CCWCCSQLiteHelper;
import com.kalyter.ccwcc.data.LoginHelperSP;
import com.kalyter.ccwcc.data.WeatherHelperSP;
import com.kalyter.ccwcc.ui.main.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;


public class AddFragment extends Fragment {

    //定义Fragment根View
    private View mView;

    //定义页面元素
    private Button buttonLocate,buttonWeatherToday,buttonSaveLocal,buttonUpload;
    private TextView textUser,textDate,textBirds;
    private EditText editLocate,editWeather;
    private PopupWindow popupExpandBirds;
    private RelativeLayout layoutDate,layoutBirds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        findViews(inflater, container);
        return mView;
    }

    private void findViews(LayoutInflater inflater, ViewGroup container){
        //判断是否为第一次加载view，否则用上一次保存的view
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_add, container, false);
        }
        // 缓存的view需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个view已经有parent的错误。
        ViewGroup parent=(ViewGroup) mView.getParent();
        if (parent != null) {
            parent.removeView(mView);
        }
        buttonLocate = (Button) mView.findViewById(R.id.button_add_locate);
        editLocate = (EditText) mView.findViewById(R.id.edit_add_locate);
        editWeather = (EditText) mView.findViewById(R.id.edit_add_weather);
        buttonWeatherToday = (Button) mView.findViewById(R.id.button_add_today_weather);
        textUser = (TextView) mView.findViewById(R.id.text_add_user);
        buttonUpload = (Button) mView.findViewById(R.id.button_add_local);
        buttonSaveLocal = (Button) mView.findViewById(R.id.button_add_upload);
        layoutBirds = (RelativeLayout) mView.findViewById(R.id.layout_add_birds);
        layoutDate = (RelativeLayout) mView.findViewById(R.id.layout_add_date);
        textDate = (TextView) mView.findViewById(R.id.text_add_date);
        textBirds = (TextView) mView.findViewById(R.id.text_add_birds);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialListeners();
        initialActions();
    }

    private void initialActions() {
        //初始化定位
        buttonLocate.callOnClick();
        //初始化时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        textDate.setText(dateFormat.format(new Date()));
        //初始化天气
        refreshWeatherInformation();
        //初始化用户TextView
        SharedPreferences sharedPreferences = mView.getContext().getSharedPreferences(LoginHelperSP.PREFERENCE_NAME, Context.MODE_PRIVATE);
        String userName=sharedPreferences.getString("username","");
        textUser.setText(userName);
    }

    private void initialListeners() {
        addListenerToLocate();
        addListenerToBirdsLayout();
        addListenerToDateLayout();
        addListenerToTodayWeather();
        addListenerToButtonSaveLocal();
    }

    private void addListenerToBirdsLayout() {
        layoutBirds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //popupWindow部分代码,高度为500，宽度填充父容器
                View expandView = LayoutInflater.from(mView.getContext()).inflate(R.layout.fragment_add_expand, null);
                final ExpandableListView expandBirds = (ExpandableListView) expandView.findViewById(R.id.expand_add_birds);
                popupExpandBirds = new PopupWindow(expandView
                        , ViewGroup.LayoutParams.MATCH_PARENT
                        , ViewGroup.LayoutParams.MATCH_PARENT);
                popupExpandBirds.setOutsideTouchable(true);
                popupExpandBirds.setFocusable(true);
                popupExpandBirds.setTouchable(true);
                popupExpandBirds.setBackgroundDrawable(mView.getResources().getDrawable(R.color.app_background));
                popupExpandBirds.showAsDropDown(mView.getRootView().findViewById(R.id.toolbar_main));
                expandBirds.setAdapter(new BirdsAdapter());
                expandBirds.setOnChildClickListener(new BirdsAdapter());
            }
        });
    }

    private void addListenerToLocate() {
        buttonLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!buttonLocate.getText().equals("定位失败，请尝试手动填写")) {
                    editLocate.setText("正在定位中...");
                    editLocate.setEnabled(false);
                    MainActivity.myLocationListener = new BDLocationListener() {
                        @Override
                        public void onReceiveLocation(BDLocation bdLocation) {
                            editLocate.setEnabled(true);
                            //通过返回码判断定位是否成功
                            if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation
                                    || bdLocation.getLocType() == BDLocation.TypeOffLineLocation) {
                                editLocate.setText(String.valueOf(bdLocation.getLatitude()) + "," + bdLocation.getLongitude());
                            } else {
                                editLocate.setHint("定位失败，请尝试手动填写");
                            }
                            MainActivity.locationClient.stop();
                        }
                    };
                    MainActivity.locationClient.registerLocationListener(MainActivity.myLocationListener);
                    MainActivity.locationClient.start();
                }
            }
        });
    }

    private void addListenerToDateLayout(){
        layoutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = (String) textDate.getText();
                int year = Integer.valueOf(date.substring(0, date.indexOf("年")));
                int month = Integer.valueOf(date.substring(date.indexOf("年") + 1, date.indexOf("月"))) - 1;
                int day = Integer.valueOf(date.substring(date.indexOf("月") + 1, date.indexOf("日")));
                new DatePickerDialog(mView.getContext()
                        , new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        textDate.setText(String.format("%d年%d月%d日", year, monthOfYear + 1, dayOfMonth));
                    }
                }
                        , year, month, day).show();
            }
        });
    }

    private void addListenerToTodayWeather(){
        buttonWeatherToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshWeatherInformation();
            }
        });
    }

    private void refreshWeatherInformation() {
        SharedPreferences sharedPreferences = mView.getContext().getSharedPreferences(WeatherHelperSP.PREFERENCES_NAME, Context.MODE_PRIVATE);
        editWeather.setText(sharedPreferences.getString("weather", ""));
        editWeather.append("," + sharedPreferences.getString("temperature", ""));
    }

    private void addListenerToButtonSaveLocal(){
        buttonSaveLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    //因为重载的代码太长，所以尝试着把它写入一个内部类里面
    class BirdsAdapter extends BaseExpandableListAdapter implements ExpandableListView.OnChildClickListener{

        private CCWCCSQLiteHelper ccwccsqLiteHelper = new CCWCCSQLiteHelper(mView.getContext());
        private HashMap<String ,Vector<String >> birds=ccwccsqLiteHelper.getBirdsNames();
        private Vector<String> species=ccwccsqLiteHelper.getBirdsSpecies();



        @Override
        public int getGroupCount() {
            return species.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return birds.get(species.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return species.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return birds.get(species.get(groupPosition)).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            LayoutInflater inflater= LayoutInflater.from(mView.getContext());
            convertView = inflater.inflate(R.layout.fragment_add_expand_parent, null);

            TextView text= (TextView) convertView.findViewById(R.id.text_add_species);
            text.setText(species.get(groupPosition));
            return text;
        }


        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            LayoutInflater inflater= LayoutInflater.from(mView.getContext());
            convertView = inflater.inflate(R.layout.fragment_add_expand_child, null);

            TextView text= (TextView) convertView.findViewById(R.id.text_add_birds);
            text.setText(birds.get(species.get(groupPosition)).get(childPosition));
            return text;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            textBirds.setText(birds.get(species.get(groupPosition)).get(childPosition));
            popupExpandBirds.dismiss();
            return false;
        }
    }

}