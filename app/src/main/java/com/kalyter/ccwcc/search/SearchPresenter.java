package com.kalyter.ccwcc.search;

import android.content.Context;

import com.kalyter.ccwcc.data.source.DBSource;
import com.kalyter.ccwcc.model.Bird;

import java.util.List;

/**
 * Created by Kalyter on 2017-5-14 0014.
 */

class SearchPresenter implements SearchContract.Presenter,
        SearchRecyclerAdapter.OnClickBirdListener {
    private DBSource mDBSource;
    private SearchContract.View mView;
    private SearchRecyclerAdapter mSearchRecyclerAdapter;
    private Context mContext;
    private int mSearchItemType;

    public SearchPresenter(DBSource DBSource,
                           SearchContract.View view,
                           Context context,
                           int searchItemType) {
        mDBSource = DBSource;
        mView = view;
        mContext = context;
        mSearchItemType = searchItemType;
        mSearchRecyclerAdapter = new SearchRecyclerAdapter(mContext, mSearchItemType);
    }

    @Override
    public void start() {
        mSearchRecyclerAdapter.setOnClickBirdListener(this);
        mView.setAdapter(mSearchRecyclerAdapter);
        search("");
    }

    @Override
    public void search(String keyword) {
        List<Bird> result = mDBSource.searchBirds(keyword);
        mSearchRecyclerAdapter.setData(result);
    }

    @Override
    public void onClickBird(Bird bird) {
        if (mSearchItemType == SearchRecyclerAdapter.TYPE_NORMAL) {
            mView.showResult(bird);
        } else if (mSearchItemType == SearchRecyclerAdapter.TYPE_WITH_CONFIRM) {
            mView.showConfirmBirdQuantity(bird);
        }
    }
}
