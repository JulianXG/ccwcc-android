package com.kalyter.ccwcc.add;

import android.content.Context;

import com.kalyter.ccwcc.data.source.DBSource;
import com.kalyter.ccwcc.model.Bird;
import com.kalyter.ccwcc.util.BirdsExpandableListAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by Kalyter on 2017-5-14 0014.
 */

class BatchAddPresenter implements BatchAddContract.Presenter {
    private BatchAddContract.View mView;
    private BirdsExpandableListAdapter mAdapter;
    private DBSource mDBSource;
    private Context mContext;

    public BatchAddPresenter(BatchAddContract.View view, Context context,
                             DBSource dbSource) {
        mView = view;
        mContext = context;
        mDBSource = dbSource;
        mAdapter = new BirdsExpandableListAdapter(mContext);
    }

    @Override
    public void start() {
        Map<String, List<Bird>> allData = mDBSource.getAllBirds();
        List<String> categories = mDBSource.getCategories();
        mAdapter.setCategories(categories);
        mAdapter.setData(allData);
        mView.setAdapter(mAdapter);
    }

    @Override
    public List<Bird> getSelectData() {
        return mAdapter.getSelectData();
    }
}
