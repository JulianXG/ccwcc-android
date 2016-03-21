package com.kalyter.ccwcc.ui.main.tab.add;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.data.LoginSP;
import com.kalyter.ccwcc.data.RecordSP;
import com.kalyter.ccwcc.data.WeatherSP;
import com.kalyter.ccwcc.ui.main.MainActivity;
import com.kalyter.ccwcc.util.BirdsExpandableListAdapter;
import com.kalyter.ccwcc.util.DateTimePicker;
import com.kalyter.ccwcc.util.RecordListAdapter;
import com.kalyter.ccwcc.util.RecordUtil;
import com.kalyter.ccwcc.util.SearchViewUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddFragment extends Fragment {

    private View mView;
    private Button buttonLocate,buttonWeatherToday,buttonSaveLocal,buttonUpload,buttonSearchAdd,buttonBatchAdd;
    private TextView  textDateTime;
    private EditText editLocation,editWeather, editDetail;
    private LinearLayout layoutDate;
    private JSONArray information = new JSONArray();    //这个只是显示在确认列表中的，鸟名称和数量
    private SearchView mSearchView;
    private ListView listConfirm;
    private RecordListAdapter recordAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        findViews(inflater, container);
        initialListeners();
        initialActions();
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
        editLocation = (EditText) mView.findViewById(R.id.edit_add_locate);
        editWeather = (EditText) mView.findViewById(R.id.edit_add_weather);
        buttonWeatherToday = (Button) mView.findViewById(R.id.button_add_today_weather);
        buttonUpload = (Button) mView.findViewById(R.id.button_add_upload);
        buttonSaveLocal = (Button) mView.findViewById(R.id.button_add_local);
        layoutDate = (LinearLayout) mView.findViewById(R.id.layout_add_date);
        textDateTime = (TextView) mView.findViewById(R.id.text_add_date);
        mSearchView = (SearchView) mView.findViewById(R.id.search_view_add);
        editDetail = (EditText) mView.findViewById(R.id.edit_add_detail);
        listConfirm=(ListView) mView.findViewById(R.id.list_add_confirm);
        buttonSearchAdd=(Button) mView.findViewById(R.id.button_add_search_add);
        buttonBatchAdd=(Button) mView.findViewById(R.id.button_add_batch_add);
    }

    private void initialActions() {
        //初始化定位
        buttonLocate.callOnClick();
        //初始化时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        textDateTime.setText(dateFormat.format(new Date()));
        //初始化天气
        refreshWeatherInformation();

    }

    private void initialListeners() {
        setListenerLocate();
        setListenerDateLayout();
        setListenerTodayWeather();
        setListenerButtonSaveLocal();
        setListenerListConfirm();
        setListenerSearchView();
        setListenerUpload();
        setSearchAdd();
        setBatchAdd();
    }

    private void setListenerLocate() {
        buttonLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!buttonLocate.getText().equals(mView.getResources().getString(R.string.hint_add_locate_error))) {
                    editLocation.setText("");
                    editLocation.setHint(mView.getResources().getString(R.string.hint_add_locating));
                    editLocation.setEnabled(false);
                    MainActivity.myLocationListener = new BDLocationListener() {
                        @Override
                        public void onReceiveLocation(BDLocation bdLocation) {
                            editLocation.setEnabled(true);
                            //通过返回码判断定位是否成功
                            if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation
                                    || bdLocation.getLocType() == BDLocation.TypeOffLineLocation) {
                                editLocation.setText(String.valueOf(bdLocation.getLatitude()) + "," + bdLocation.getLongitude());
                                editLocation.setHint(mView.getResources().getString(R.string.hint_add_locate));
                            } else {
                                editLocation.setHint(mView.getResources().getString(R.string.hint_add_locate_error));
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

    private void setListenerDateLayout(){
        layoutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DateTimePicker(mView.getContext(), textDateTime).showDateTimePickerDialog();
            }
        });
    }

    private void setListenerTodayWeather(){
        buttonWeatherToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshWeatherInformation();
            }
        });
    }

    private void refreshWeatherInformation() {
        SharedPreferences sharedPreferences = mView.getContext().getSharedPreferences(WeatherSP.PREFERENCES_NAME, Context.MODE_PRIVATE);
        String weather = sharedPreferences.getString("weather", "");
        String temperature = sharedPreferences.getString("temperature", "");
        if (!weather.equals("") && !weather.equals("")) {
            editWeather.setText(weather);
            editWeather.append("," + temperature);
        }
    }

    private void setListenerButtonSaveLocal(){
        buttonSaveLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInformationIntegrity()) {
                    RecordSP recordSP = new RecordSP(mView.getContext());
                    recordSP.saveRecord(integrateRecord());
                    initialAllViews();
                    Toast.makeText(mView.getContext(), R.string.prompt_add_save_local_success, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mView.getContext(), R.string.prompt_information_incomplete, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initialAllViews() {
        editDetail.setText("");
        information.clear();
        ((RecordListAdapter) listConfirm.getAdapter()).notifyDataSetChanged();
    }

    private void setListenerListConfirm() {
        recordAdapter = new RecordListAdapter(mView.getContext(),information);
        listConfirm.setAdapter(recordAdapter);
    }

    private boolean checkInformationIntegrity(){
        if (editLocation.getText().equals("") ||editWeather.getText().equals("")||information.isEmpty()){
                return false;
        }
        return true;
    }

    private JSONArray integrateRecord() {
        JSONArray array = new JSONArray();
        for (Object o : information) {
            JSONObject t = ((JSONObject) o);
            JSONObject recordElement = new JSONObject();
            recordElement.put(RecordUtil.CODE_KEY, t.getString(RecordUtil.CODE_KEY));

            recordElement.put(RecordUtil.BIRD_NAME_KEY, t.getString(RecordUtil.BIRD_NAME_KEY));
            recordElement.put(RecordUtil.QUANTITY_KEY, t.getString(RecordUtil.QUANTITY_KEY));

            String location = String.valueOf(editLocation.getText());
            double lat = Double.valueOf(location.substring(0, location.indexOf(',')));
            double lon = Double.valueOf(location.substring(location.indexOf(',') + 1));
            recordElement.put(RecordUtil.LAT_KEY, lat);
            recordElement.put(RecordUtil.LON_KEY, lon);

            recordElement.put(RecordUtil.DATETIME_KEY, textDateTime.getText());
            String recordIndex = textDateTime.getText().toString().substring(0, 7);
            recordElement.put(RecordUtil.RECORD_INDEX_KEY, recordIndex);

            recordElement.put(RecordUtil.WEATHER_KEY, editWeather.getText());
            SharedPreferences sharedPreferences = mView.getContext().getSharedPreferences(LoginSP.PREFERENCES_NAME, Context.MODE_PRIVATE);
            String city = sharedPreferences.getString(LoginSP.CHECK_POINT_KEY, "");
            recordElement.put(RecordUtil.POSITION_KEY, city);

            recordElement.put(RecordUtil.DETAIL_KEY, editDetail.getText());
            array.add(recordElement);
        }
        return array;
    }

    private void setListenerSearchView() {
        SearchViewUtil searchViewUtil = new SearchViewUtil(mSearchView, recordAdapter);
        searchViewUtil.setInputQuantityAdapter();
    }

    private void setListenerUpload() {
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInformationIntegrity()) {
                    RecordUtil recordUtil = new RecordUtil(mView.getContext());
                    if (recordUtil.submitRecord(integrateRecord())) {
                        initialAllViews();
                    }
                } else {
                    Toast.makeText(getContext(), R.string.prompt_information_incomplete, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setSearchAdd(){
        buttonSearchAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchView.requestFocus();
                InputMethodManager inputMethodManager = (InputMethodManager) mView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
    }

    private void setBatchAdd(){
        buttonBatchAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent("BATCH_ADD_ACTIVITY"),0);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data!=null) {
            String s = data.getExtras().getString(BatchAddActivity.ARRAY_STRING_KEY);
            JSONArray array = JSON.parseArray(s);
            for (Object o : array) {
                JSONObject t=(JSONObject) o;
                information.add(t);
            }
            recordAdapter.notifyDataSetChanged();
        }
    }

}