package com.kalyter.ccwcc.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.kalyter.ccwcc.R;

public class RecordListAdapter extends BaseAdapter{

    private JSONArray mJSONArray;
    private Context mContext;
    private TextView textName,textQuantity,textIndex;
    private Button buttonDelete;

    public RecordListAdapter(Context mContext, JSONArray mJSONArray) {
        this.mContext = mContext;
        this.mJSONArray = mJSONArray;
    }

    @Override
    public int getCount() {
        return mJSONArray.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_add_confirm, null);
        }
        if (!mJSONArray.getJSONObject(position).isEmpty()) {
            buttonDelete=(Button) convertView.findViewById(R.id.button_add_confirm_list_delete);
            textName = (TextView) convertView.findViewById(R.id.text_add_confirm_list_name);
            textQuantity = (TextView) convertView.findViewById(R.id.text_add_confirm_list_quantity);
            textIndex = (TextView) convertView.findViewById(R.id.text_add_confirm_list_index);

            textName.setText(mJSONArray.getJSONObject(position).getString("birdname"));
            textQuantity.setText(mJSONArray.getJSONObject(position).getString("birdquantity"));
            textIndex.setText((position+1)+"");

            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mJSONArray.remove(position);
                    notifyDataSetChanged();
                }
            });
        }

        return convertView;
    }


}