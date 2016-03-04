package com.kalyter.ccwcc.ui.main.tab.add;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.data.CCWCCSQLiteHelper;
import com.kalyter.ccwcc.data.LoginSP;
import com.kalyter.ccwcc.data.RecordSP;
import com.kalyter.ccwcc.data.WeatherSP;
import com.kalyter.ccwcc.ui.main.MainActivity;
import com.kalyter.ccwcc.util.BirdsExpandableListAdapter;
import com.kalyter.ccwcc.util.DateTimePicker;
import com.kalyter.ccwcc.util.RecordListAdapter;
import com.kalyter.ccwcc.util.RecordUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddFragment extends Fragment {

    private View mView;
    private Button buttonLocate,buttonWeatherToday,buttonSaveLocal,buttonUpload, buttonAddRecord;
    private TextView  textDateTime,textBirds;
    private EditText editLocation,editWeather,editDeatail;
    private PopupWindow popupExpandBirds;
    private RelativeLayout layoutDate,layoutBirds;
    private ListView listConfirm;
    private JSONArray record=new JSONArray();
    private SearchView mSearchView;
    BirdsExpandableListAdapter birdsExpandableListAdapter;

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
        editLocation = (EditText) mView.findViewById(R.id.edit_add_locate);
        editWeather = (EditText) mView.findViewById(R.id.edit_add_weather);
        buttonWeatherToday = (Button) mView.findViewById(R.id.button_add_today_weather);
        buttonUpload = (Button) mView.findViewById(R.id.button_add_upload);
        buttonSaveLocal = (Button) mView.findViewById(R.id.button_add_local);
        layoutBirds = (RelativeLayout) mView.findViewById(R.id.layout_add_birds);
        layoutDate = (RelativeLayout) mView.findViewById(R.id.layout_add_date);
        textDateTime = (TextView) mView.findViewById(R.id.text_add_date);
        textBirds = (TextView) mView.findViewById(R.id.text_add_birds);
        listConfirm = (ListView) mView.findViewById(R.id.list_add_confirm);
        buttonAddRecord = (Button) mView.findViewById(R.id.button_add_addrecord);
        mSearchView = (SearchView) mView.findViewById(R.id.search_view_add);
        editDeatail = (EditText) mView.findViewById(R.id.edit_add_detail);
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        textDateTime.setText(dateFormat.format(new Date()));
        //初始化天气
        refreshWeatherInformation();
    }

    private void initialListeners() {
        setListenerLocate();
        setListenerBirdsLayout();
        setListenerDateLayout();
        setListenerTodayWeather();
        setListenerButtonSaveLocal();
        setListenerListConfirm();
        setListenerAddRecord();
        setListenerSearchView();
        setListenerUpload();
    }

    private void setListenerBirdsLayout() {
        layoutBirds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View expandView = LayoutInflater.from(mView.getContext()).inflate(R.layout.fragment_add_expand, null);
                final ExpandableListView expandBirds = (ExpandableListView) expandView.findViewById(R.id.expand_add_birds);
                popupExpandBirds = new PopupWindow(expandView
                        , ViewGroup.LayoutParams.MATCH_PARENT
                        , ViewGroup.LayoutParams.MATCH_PARENT);
                popupExpandBirds.setOutsideTouchable(false);
                popupExpandBirds.setFocusable(true);
                popupExpandBirds.setTouchable(true);
                popupExpandBirds.setBackgroundDrawable(mView.getResources().getDrawable(R.color.list_bg_color));
                popupExpandBirds.showAsDropDown(mView.getRootView().findViewById(R.id.toolbar));
                birdsExpandableListAdapter = new BirdsExpandableListAdapter(mView, popupExpandBirds);
                expandBirds.setAdapter(birdsExpandableListAdapter);
            }
        });
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
                new DateTimePicker(mView, textDateTime).DateTimePickerDialog();
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
        editWeather.setText(sharedPreferences.getString("weather", ""));
        editWeather.append("," + sharedPreferences.getString("temperature", ""));
    }

    private void setListenerButtonSaveLocal(){
        buttonSaveLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!record.isEmpty()) {
                    RecordSP recordSP = new RecordSP(mView.getContext());
                    recordSP.saveRecord(record);
                    initialAllViews();
                    Toast.makeText(mView.getContext(), R.string.prompt_add_save_local_success, Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(mView.getContext(), R.string.prompt_dialog_empty_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initialAllViews() {
        textBirds.setText("");
        editDeatail.setText("");
        record.clear();
        ((RecordListAdapter) listConfirm.getAdapter()).notifyDataSetChanged();
    }

    private void setListenerListConfirm() {
        RecordListAdapter recordAdapter = new RecordListAdapter(mView.getContext(),record);
        listConfirm.setAdapter(recordAdapter);
    }

    private void setListenerAddRecord(){
        buttonAddRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //每一组里面7个元素
                if (editLocation.length() == 0 || editWeather.length() == 0 || textBirds.length() == 0) {
                    new AlertDialog.Builder(mView.getContext())
                            .setTitle(mView.getResources().getString(R.string.dialog_title_warning))
                            .setMessage(mView.getResources().getString(R.string.dialog_message_incomplete))
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(mView.getResources().getString(R.string.dialog_positive_button_caption_iknow), null).create().show();
                } else {
                    addRecordTemporary();
                    textBirds.setText("");
                    editDeatail.setText("");
                    ((RecordListAdapter) listConfirm.getAdapter()).notifyDataSetChanged();
                }
            }
        });
    }


    private void addRecordTemporary() {
        CCWCCSQLiteHelper helper = new CCWCCSQLiteHelper(mView.getContext());

        char delimiter = '|';
        String wholeText= textBirds.getText().toString();
        int count=0;
        for (int i = 0; i < wholeText.length(); i++) {
            if (wholeText.charAt(i) == delimiter) {
                count++;
            }
        }

        int lastDelimiter=0;
        for (int i = 0; i < count; i++) {
            int nextDelimiter = wholeText.indexOf(delimiter, lastDelimiter);
            String singleText = wholeText.substring(lastDelimiter, nextDelimiter);
            lastDelimiter=nextDelimiter+1;
            int indexOfComma = singleText.indexOf(",");
            String name = singleText.substring(0, indexOfComma);
            int quantity = Integer.parseInt(singleText.substring(indexOfComma + 1));
            JSONObject recordElement = new JSONObject();
            String code = helper.getCodeByBirdName(name);
            recordElement.put("code", code);

            recordElement.put("birdname", name);
            recordElement.put("birdquantity", quantity);

            String location= String.valueOf(editLocation.getText());
            double lat = Double.valueOf(location.substring(0, location.indexOf(',')));
            double lon = Double.valueOf(location.substring(location.indexOf(',') + 1));
            recordElement.put("lat", lat);
            recordElement.put("lon", lon);

            recordElement.put("datetime", textDateTime.getText());
            String recordIndex = textDateTime.getText().toString().substring(0, 7);
            recordElement.put("recordidx", recordIndex);

            recordElement.put("weather", editWeather.getText());
            SharedPreferences sharedPreferences = mView.getContext().getSharedPreferences(LoginSP.PREFERENCES_NAME, Context.MODE_PRIVATE);
            String city = sharedPreferences.getString("checkpoint", "");
            recordElement.put("position", city);

            recordElement.put("detail", editDeatail.getText());
            record.add(recordElement);
        }

        helper.close();
    }

    private void setListenerSearchView() {
        final CCWCCSQLiteHelper dbHelper = new CCWCCSQLiteHelper(mView.getContext());
        Cursor birdAllNameCursor = dbHelper.getBirdAllNamesCursor(null);
        SimpleCursorAdapter simpleCursorAdapter=new SimpleCursorAdapter(mView.getContext(),android.R.layout.simple_list_item_1, birdAllNameCursor
                ,new String[]{"namezh"}, new int[]{android.R.id.text1});
        mSearchView.setSuggestionsAdapter(simpleCursorAdapter);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                onQueryTextChange(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Cursor cursor = dbHelper.getBirdAllNamesCursor(newText);
                SimpleCursorAdapter adapter = new SimpleCursorAdapter(mView.getContext(), android.R.layout.simple_list_item_1, cursor
                        , new String[]{"namezh"}, new int[]{android.R.id.text1});
                mSearchView.setSuggestionsAdapter(adapter);
                return false;
            }
        });

        mSearchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {

                final InputMethodManager imm = (InputMethodManager) mView.getContext().getSystemService(mView.getContext().INPUT_METHOD_SERVICE);
//                imm.toggleSoftInput(0,InputMethodManager.SHOW_FORCED);
                Cursor cursor = mSearchView.getSuggestionsAdapter().getCursor();
                cursor.moveToFirst();
                int i = 0;
                while (i < position) {
                    cursor.moveToNext();
                    i++;
                }
                String name = cursor.getString(1);
                textBirds.setText(name);
                final EditText editText = new EditText(mView.getContext());
                editText.setHint(R.string.hint_add_quantity);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                editText.setFocusableInTouchMode(true);
                editText.setFocusable(true);
                new AlertDialog.Builder(mView.getContext())
                        .setTitle(getResources().getString(R.string.dialog_title_information))
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(editText)
                        .setPositiveButton(getResources().getString(R.string.dialog_positive_button_caption_confirm), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!editText.getText().equals("")) {
                                    textBirds.append(","+editText.getText()+'|');
                                    buttonAddRecord.requestFocus();
                                    imm.hideSoftInputFromWindow(mView.getWindowToken(),0);
                                }
                            }
                        }).setNegativeButton(R.string.dialog_positive_button_caption_cancel, null)
                        .setCancelable(false)
                        .create().show();
                mSearchView.setQuery("",false);
                editDeatail.requestFocus();
                return false;
            }
        });
    }

    private void setListenerUpload() {
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordUtil recordUtil = new RecordUtil(mView.getContext());
                if (recordUtil.submitRecord(record)) {
                    initialAllViews();
                }
            }
        });
    }

}