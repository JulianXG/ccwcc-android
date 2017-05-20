package com.kalyter.ccwcc.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.model.Bird;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecordListAdapter extends BaseAdapter {
    private List<Bird> mData;
    private Context mContext;

    public RecordListAdapter(Context mContext) {
        this.mContext = mContext;
        this.mData = new ArrayList<>();
    }

    public void addData(Bird bird) {
        mData.add(bird);
        notifyDataSetChanged();
    }

    public void addData(List<Bird> birds) {
        mData.addAll(birds);
        notifyDataSetChanged();
    }

    public void clearAllData() {
        mData.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
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
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.list_add_confirm, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Bird bird = mData.get(position);
        viewHolder.mBirdName.setText(bird.getNameZh());
        viewHolder.mQuantity.setText(bird.getBirdCount().toString());
        viewHolder.mIndex.setText(String.format("%d", position + 1 ));
        return convertView;
    }

    public void deleteData(int position) {
        mData.remove(position);
        notifyDataSetChanged();
    }

    public void changeCount(int position, int count) {
        Bird bird = mData.get(position);
        bird.setBirdCount(count);
        notifyDataSetChanged();
    }

    public List<Bird> getData() {
        return mData;
    }

    class ViewHolder {
        @BindView(R.id.index)
        TextView mIndex;
        @BindView(R.id.bird_name)
        TextView mBirdName;
        @BindView(R.id.quantity)
        TextView mQuantity;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
