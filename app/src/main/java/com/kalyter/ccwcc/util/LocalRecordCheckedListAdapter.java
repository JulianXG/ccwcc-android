package com.kalyter.ccwcc.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.kalyter.ccwcc.R;

import java.util.HashMap;

public class LocalRecordCheckedListAdapter extends BaseAdapter {

    private JSONArray record;
    private static HashMap<Integer,Boolean> isSelected;
    private Context mContext;
    private TextView textBirdName,textDate,textQuantity;
    private CheckBox checkBox;

    public LocalRecordCheckedListAdapter(Context mContext, JSONArray record) {
        this.mContext = mContext;
        this.record = record;
        isSelected = new HashMap<>();
        for (int i = 0; i < record.size(); i++) {
            isSelected.put(i, false);
        }
    }

    @Override
    public int getCount() {
        return record.size();
    }

    @Override
    public Object getItem(int position) {
        return record.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_home_local, null);
        }
        textBirdName = (TextView) convertView.findViewById(R.id.text_home_local_bird_name);
        textDate = (TextView) convertView.findViewById(R.id.text_home_local_date);
        textQuantity = (TextView) convertView.findViewById(R.id.text_home_local_quantity);
        checkBox = (CheckBox) convertView.findViewById(R.id.checkbox_local);

        textBirdName.setText(record.getJSONObject(position).getString("birdName"));
        textDate.setText(record.getJSONObject(position).getString("datetime"));
        textQuantity.setText(record.getJSONObject(position).getString("quantity"));
        try {
            checkBox.setChecked(getIsSelected().get(position));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIsSelected().put(position, !getIsSelected().get(position));
            }
        });

        return convertView;
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }


}
