package com.kalyter.ccwcc.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.common.Config;
import com.kalyter.ccwcc.model.Record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DraftAdapter extends BaseAdapter {
    private List<Record> mData;
    private static HashMap<Integer, Boolean> mSelectedData;
    private Context mContext;
    private String[] mCheckpoints;

    public DraftAdapter(Context context) {
        mContext = context;
        mData = new ArrayList<>();
        mCheckpoints = mContext.getResources().getStringArray(R.array.checkpoints);
    }

    public Record getRecordByIndex(int index) {
        return mData.get(index);
    }

    public void setData(List<Record> data) {
        mData.clear();
        notifyDataSetChanged();
        mData.addAll(data);
        mSelectedData = new HashMap<>();
        for (int i = 0; i < mData.size(); i++) {
            mSelectedData.put(i, false);
        }
        notifyDataSetChanged();
    }

    public List<Record> getData() {
        return mData;
    }

    public void toggleAllStatus(boolean status) {
        for (int i = 0; i < mSelectedData.size(); i++) {
            mSelectedData.put(i, status);
        }
        notifyDataSetChanged();
    }

    public List<Record> getSelectedData() {
        List<Record> selectedRecord = new ArrayList<>();
        for (int i = 0; i < mSelectedData.size(); i++) {
            if (mSelectedData.get(i)) {
                selectedRecord.add(mData.get(i));
            }
        }
        return selectedRecord;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Record record = mData.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_draft, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mCheckpoint.setText(mCheckpoints[record.getCheckpointId() - 1]);
        viewHolder.mDate.setText(Config.yyyyMMddHHmmss.format(record.getRecordTime()));
        viewHolder.mQuantity.setText(String.format("共%d种鸟类", record.getBirds().size()));
        viewHolder.mCheck.setChecked(mSelectedData.get(position));

        viewHolder.mCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedData.put(position, !mSelectedData.get(position));
            }
        });

        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.check)
        CheckBox mCheck;
        @BindView(R.id.checkpoint)
        TextView mCheckpoint;
        @BindView(R.id.date)
        TextView mDate;
        @BindView(R.id.quantity)
        TextView mQuantity;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
