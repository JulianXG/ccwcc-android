package com.kalyter.ccwcc.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.model.Bird;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BirdsExpandableListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    // 总数据
    private Map<String, List<Bird>> mData;
    // 类别父类数据
    private List<String> mCategories;

    public BirdsExpandableListAdapter(Context context) {
        mContext = context;
        mData = new HashMap<>();
        mCategories = new ArrayList<>();
    }

    public void setData(Map<String, List<Bird>> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void setCategories(List<String> categories) {
        mCategories = categories;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return mCategories.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mData.get(mCategories.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mCategories.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mData.get(mCategories.get(groupPosition)).get(childPosition);
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
        ParentViewHolder parentViewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.item_parent_category, parent, false);
            parentViewHolder = new ParentViewHolder(convertView);
            convertView.setTag(parentViewHolder);
        } else {
            parentViewHolder = (ParentViewHolder) convertView.getTag();
        }
        parentViewHolder.mCategoryName.setText(mCategories.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder childViewHolder;
        final Bird bird = mData.get(mCategories.get(groupPosition)).get(childPosition);
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.item_child_bird, parent, false);
            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        if (bird.getBirdCount() != null) {
            childViewHolder.mQuantity.setText(bird.getBirdCount() + "");
        } else {
            childViewHolder.mQuantity.setText("");
        }
        childViewHolder.mBirdName.setText(bird.getNameZh());
        childViewHolder.mItemChildContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(mContext)
                        .title(R.string.please_input)
                        .content("请输入具体数量值")
                        .inputType(InputType.TYPE_CLASS_NUMBER)
                        .input("请输入具体数量", "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                String content = input.toString();
                                if (content.equals("")) {
                                    bird.setBirdCount(null);
                                    childViewHolder.mQuantity.setText("");
                                } else {
                                    Integer birdCount = Integer.valueOf(content);
                                    bird.setBirdCount(birdCount);
                                    childViewHolder.mQuantity.setText(birdCount + "");
                                }
                            }
                        }).build().show();
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public List<Bird> getSelectData() {
        List<Bird> result = new ArrayList<>();
        for (String category : mCategories) {
            for (Bird bird : mData.get(category)) {
                if (bird.getBirdCount() != null) {
                    result.add(bird);
                }
            }
        }
        return result;
    }

    class ParentViewHolder {
        @BindView(R.id.category_name)
        TextView mCategoryName;

        ParentViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class ChildViewHolder {

        @BindView(R.id.bird_name)
        TextView mBirdName;
        @BindView(R.id.quantity)
        TextView mQuantity;
        @BindView(R.id.item_child_container)
        LinearLayout mItemChildContainer;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
