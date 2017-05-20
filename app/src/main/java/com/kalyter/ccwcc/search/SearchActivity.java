package com.kalyter.ccwcc.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.add.AddFragment;
import com.kalyter.ccwcc.common.App;
import com.kalyter.ccwcc.common.BaseActivity;
import com.kalyter.ccwcc.common.Config;
import com.kalyter.ccwcc.flag.FlagActivity;
import com.kalyter.ccwcc.model.Bird;
import com.kalyter.ccwcc.util.RecycleViewDivider;
import com.kalyter.ccwcc.util.Util;

import butterknife.BindView;

public class SearchActivity extends BaseActivity implements SearchContract.View {
    public static final int RESULT_CODE = 1;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.edit_search)
    EditText mEditSearch;
    @BindView(R.id.result_recycler)
    RecyclerView mResultRecycler;

    private int mRequestCode;
    private SearchContract.Presenter mPresenter;
    private int mSearchItemType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mRequestCode = getIntent().getIntExtra(Config.KEY_REQUEST_SOURCE, -1);
        if (mRequestCode == FlagActivity.REQUEST_CODE) {
            mSearchItemType = SearchRecyclerAdapter.TYPE_NORMAL;
        } else if (mRequestCode == AddFragment.REQUEST_CODE) {
            mSearchItemType = SearchRecyclerAdapter.TYPE_WITH_CONFIRM;
        }
        super.onCreate(savedInstanceState);
        mTitle.setText(R.string.caption_search_add);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mEditSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mPresenter.search(s.toString());
            }
        });

        mPresenter.start();
    }

    @Override
    protected void setupPresenter() {
        mPresenter = new SearchPresenter(
                App.getInjectClass().getDBSource(),
                this,
                this,
                mSearchItemType);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_add;
    }

    @Override
    public void showConfirmBirdQuantity(final Bird bird) {
        new MaterialDialog.Builder(this)
                .title("请输入")
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .input("请输入鸟类数量", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        bird.setBirdCount(Integer.valueOf(input.toString()));
                        showResult(bird);
                    }
                }).build().show();
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mResultRecycler.setLayoutManager(manager);
        mResultRecycler.addItemDecoration(new RecycleViewDivider(this,
                LinearLayoutManager.VERTICAL, 20, R.color.divider));
        mResultRecycler.setAdapter(adapter);
    }

    @Override
    public void showResult(Bird bird) {
        Intent intent = new Intent();
        intent.putExtra(Config.KEY_BUNDLE_BIRD, Util.serialize(bird));
        SearchActivity.this.setResult(RESULT_OK, intent);
        finish();
    }
}
