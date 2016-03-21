package com.kalyter.ccwcc.util;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.data.CCWCCSQLiteHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class BirdsExpandableListAdapter extends BaseExpandableListAdapter{

    private Context mContext;
    private HashMap<String ,Vector<String >> birds;
    private Vector<String> species;
    private static ArrayList<ArrayList<Quantity>> allQuantity;


    class Quantity{
        public boolean isSelected;
        public String name;
        public int quantity;
    }


    public BirdsExpandableListAdapter(Context mContext) {
        this.mContext = mContext;
        initialData();
    }

    private void initialData() {
        CCWCCSQLiteHelper ccwccsqLiteHelper = new CCWCCSQLiteHelper(mContext);
        birds= ccwccsqLiteHelper.getBirdsNames();
        species= ccwccsqLiteHelper.getBirdsSpecies();
        ccwccsqLiteHelper.close();
        allQuantity = new ArrayList<>();
        for (int i = 0; i < species.size(); i++) {
            ArrayList<Quantity> arrayList = new ArrayList<>();
            for (int j = 0; j < birds.get(species.get(i)).size(); j++) {
                Quantity t=new Quantity();
                t.isSelected=false;
                t.name = birds.get(species.get(i)).get(j);
                t.quantity=0;
                arrayList.add(t);
            }
            allQuantity.add(arrayList);
        }
    }

    public String getArrayJSONString() {
        JSONArray data = new JSONArray();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < allQuantity.size(); i++) {
            for (int j = 0; j < allQuantity.get(i).size(); j++) {
                Quantity t = allQuantity.get(i).get(j);
                if (t.isSelected && t.quantity > 0) {
                    stringBuffer.append(t.name + "," + t.quantity + "|");
                    JSONObject o = new JSONObject();
                    o.put(RecordUtil.QUANTITY_KEY, t.quantity);
                    o.put(RecordUtil.BIRD_NAME_KEY, t.name);
                    data.add(o);
                }
            }
        }
        return data.toJSONString();
    }


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
        LayoutInflater inflater= LayoutInflater.from(mContext);
        convertView = inflater.inflate(R.layout.expand_parent, null);

        TextView textSpecies = (TextView) convertView.findViewById(R.id.text_add_species);
        textSpecies.setText(species.get(groupPosition));
        return convertView;
    }


    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater= LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.expand_child, null);
        }
        final Quantity mQuantity = allQuantity.get(groupPosition).get(childPosition);

        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox_expand);
        final EditText editQuantity = (EditText) convertView.findViewById(R.id.edit_expand_quantity);
        checkBox.setText(birds.get(species.get(groupPosition)).get(childPosition));
        checkBox.setChecked(mQuantity.isSelected);

        if (mQuantity.quantity > 0) {
            editQuantity.setText(String.valueOf(mQuantity.quantity));
        }
        if (mQuantity.isSelected) {
            editQuantity.setVisibility(View.VISIBLE);
        }else {
            editQuantity.setVisibility(View.GONE);
        }

        editQuantity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                mQuantity.quantity = Integer.valueOf(String.valueOf(v.getText()));
                return false;
            }
        });

        editQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mQuantity.quantity = Integer.valueOf(s.toString());

            }
        });


        final View finalConvertView = convertView;
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuantity.isSelected = !mQuantity.isSelected;
                EditText editQuantity = (EditText) finalConvertView.findViewById(R.id.edit_expand_quantity);

                if (mQuantity.isSelected) {
                    editQuantity.setVisibility(View.VISIBLE);
                } else {
                    editQuantity.setVisibility(View.GONE);
                }
            }
        });


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
