package com.kalyter.ccwcc.util;

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
import android.widget.ExpandableListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.data.CCWCCSQLiteHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class BirdsExpandableListAdapter extends BaseExpandableListAdapter{

    private View mView;
    private PopupWindow mPopupWindow;
    private HashMap<String ,Vector<String >> birds;
    private Vector<String> species;
    private TextView textSpecies;
    private CheckBox checkBox;
    private EditText editQuantity;
    private Button buttonAdd;
    private static ArrayList<ArrayList<Quantity>> allQuantity;


    class Quantity{
        public boolean isSelected;
        public String name;
        public int quantity;

    }


    public BirdsExpandableListAdapter(final View mView, final PopupWindow mPopupWindow) {
        this.mPopupWindow = mPopupWindow;
        this.mView = mView;
        buttonAdd = (Button) mPopupWindow.getContentView().findViewById(R.id.button_expand_add);
        initialData(mView);
        setListenerAdd();
    }

    private void initialData(View mView) {
        CCWCCSQLiteHelper ccwccsqLiteHelper = new CCWCCSQLiteHelper(mView.getContext());
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

    private void setListenerAdd() {
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textBirds = (TextView) mView.getRootView().findViewById(R.id.text_add_birds);
                mPopupWindow.dismiss();
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < allQuantity.size(); i++) {
                    for (int j = 0; j < allQuantity.get(i).size(); j++) {
                        Quantity t = allQuantity.get(i).get(j);
                        if (t.isSelected) {
                            stringBuffer.append(t.name + "," + t.quantity + "|");
                        }
                    }
                }
                textBirds.setText(stringBuffer.toString());
                mPopupWindow.dismiss();
            }
        });
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
        LayoutInflater inflater= LayoutInflater.from(mView.getContext());
        convertView = inflater.inflate(R.layout.fragment_add_expand_parent, null);

        textSpecies= (TextView) convertView.findViewById(R.id.text_add_species);
        textSpecies.setText(species.get(groupPosition));
        return convertView;
    }


    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater inflater= LayoutInflater.from(mView.getContext());
        convertView = inflater.inflate(R.layout.fragment_add_expand_child, null);
        final Quantity mQuantity = allQuantity.get(groupPosition).get(childPosition);

        checkBox = (CheckBox) convertView.findViewById(R.id.checkbox_expand);
        editQuantity = (EditText) convertView.findViewById(R.id.edit_expand_quantity);
        checkBox.setText(birds.get(species.get(groupPosition)).get(childPosition));
        checkBox.setChecked(mQuantity.isSelected);

        if (mQuantity.quantity > 0) {
            editQuantity.setText(mQuantity.quantity+"");
        }
        if (mQuantity.isSelected) {
            editQuantity.setVisibility(View.VISIBLE);
        }else {
            editQuantity.setVisibility(View.INVISIBLE);
        }

        editQuantity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                mQuantity.quantity=Integer.valueOf(String.valueOf(v.getText()));
                return false;
            }
        });


        final View finalConvertView = convertView;
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuantity.isSelected=!checkBox.isChecked();
                EditText editQuantity = (EditText) finalConvertView.findViewById(R.id.edit_expand_quantity);

                editQuantity.setFocusableInTouchMode(true);
                if (mQuantity.isSelected) {
                    editQuantity.setVisibility(View.VISIBLE);
                } else {
                    editQuantity.setVisibility(View.INVISIBLE);
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
