package com.kalyter.ccwcc.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.model.Bird;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Kalyter on 2017-5-14 0014.
 */

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.ViewHolder> {
    // 搜索子项的类型
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_WITH_CONFIRM = 2;

    private List<Bird> mData;
    private Context mContext;
    private OnClickBirdListener mOnClickBirdListener;
    private int mType;

    public SearchRecyclerAdapter(Context context, int type) {
        mData = new ArrayList<>();
        mContext = context;
        mType = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.list_search_suggestion_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Bird bird = mData.get(position);
        holder.mEn.setText(bird.getNameEn());
        holder.mZh.setText(bird.getNameZh());
        holder.mLt.setText(bird.getNameLt());
        holder.mPinyin.setText(bird.getZhPinyin());

        holder.mSearchItemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickBirdListener.onClickBird(bird);
            }
        });
    }

    public void setData(List<Bird> data) {
        notifyItemRangeRemoved(0, mData.size());
        mData.clear();
        mData.addAll(data);
        notifyItemRangeInserted(0, mData.size());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setOnClickBirdListener(OnClickBirdListener onClickBirdListener) {
        mOnClickBirdListener = onClickBirdListener;
    }

    public interface OnClickBirdListener {
        void onClickBird(Bird bird);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.zh)
        TextView mZh;
        @BindView(R.id.en)
        TextView mEn;
        @BindView(R.id.pinyin)
        TextView mPinyin;
        @BindView(R.id.lt)
        TextView mLt;
        @BindView(R.id.search_item_container)
        LinearLayout mSearchItemContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
