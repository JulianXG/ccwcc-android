package com.kalyter.ccwcc.flag;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.common.App;
import com.kalyter.ccwcc.common.BaseActivity;
import com.kalyter.ccwcc.common.Config;
import com.kalyter.ccwcc.model.Bird;
import com.kalyter.ccwcc.model.Flag;
import com.kalyter.ccwcc.search.SearchActivity;
import com.kalyter.ccwcc.util.SingleSelectDialog;
import com.kalyter.ccwcc.util.Util;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class FlagActivity extends BaseActivity implements FlagContract.View {

    public static final int REQUEST_CODE = 10;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.bird_name)
    TextView mBirdName;
    @BindView(R.id.bird_name_container)
    LinearLayout mBirdNameContainer;
    @BindView(R.id.flag)
    TextView mFlagText;
    @BindView(R.id.flag_container)
    LinearLayout mFlagContainer;
    @BindView(R.id.lu)
    TextView mLu;
    @BindView(R.id.lu_container)
    LinearLayout mLuContainer;
    @BindView(R.id.ru)
    TextView mRu;
    @BindView(R.id.ru_container)
    LinearLayout mRuContainer;
    @BindView(R.id.ld)
    TextView mLd;
    @BindView(R.id.ld_container)
    LinearLayout mLdContainer;
    @BindView(R.id.rd)
    TextView mRd;
    @BindView(R.id.rd_container)
    LinearLayout mRdContainer;
    @BindView(R.id.text_flag_flag_code)
    TextView mTextFlagFlagCode;
    @BindView(R.id.flag_code)
    EditText mFlagCode;
    @BindView(R.id.discover_time)
    TextView mDiscoverTime;
    @BindView(R.id.discover_time_container)
    LinearLayout mDiscoverTimeContainer;
    @BindView(R.id.text_flag_remarks)
    TextView mTextFlagRemarks;
    @BindView(R.id.remark)
    EditText mRemark;

    @OnClick(R.id.discover_time_container)
    void changeTime() {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(2000, 1, 1);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(new Date());
        Calendar discoveredCalendar = Calendar.getInstance();
        discoveredCalendar.setTime(mFlag.getDiscoveredTime());
        new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                mFlag.setDiscoveredTime(date);
                mDiscoverTime.setText(Config.yyyyMMddHHmmss.format(date));
            }
        })
                .setRangDate(startCalendar, endCalendar)
                .setDate(discoveredCalendar)
                .build().show();
    }

    @OnClick(R.id.bird_name_container)
    void chooseBird() {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra(Config.KEY_REQUEST_SOURCE, REQUEST_CODE);
        startActivityForResult(intent, Config.REQUEST_SEARCH_CODE);
    }

    @OnClick(R.id.ld_container)
    void chooseLd() {
        SingleSelectDialog dialog = new SingleSelectDialog(this, mLd);
        dialog.showHoopSingleSelectDialog();
    }

    @OnClick(R.id.rd_container)
    void chooseRd() {
        SingleSelectDialog dialog = new SingleSelectDialog(this, mRd);
        dialog.showHoopSingleSelectDialog();
    }


    @OnClick(R.id.lu_container)
    void chooseLu() {
        SingleSelectDialog dialog = new SingleSelectDialog(this, mLu);
        dialog.showHoopSingleSelectDialog();
    }

    @OnClick(R.id.ru_container)
    void chooseRu() {
        SingleSelectDialog dialog = new SingleSelectDialog(this, mRu);
        dialog.showHoopSingleSelectDialog();
    }

    @OnClick(R.id.flag_container)
    void chooseFlagColor() {
        SingleSelectDialog dialog = new SingleSelectDialog(this, mFlagText);
        dialog.showFlagSingleSelectDialog();
    }

    private ProgressDialog mProgressDialog;
    private FlagContract.Presenter mPresenter;
    private Flag mFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFlag = new Flag();
        Date now = new Date();
        mFlag.setDiscoveredTime(now);
        mDiscoverTime.setText(Config.yyyyMMddHHmmss.format(now));

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.loading));
        mProgressDialog.setCanceledOnTouchOutside(false);

        mTitle.setText(R.string.caption_flag);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mToolbar.inflateMenu(R.menu.submit);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (checkIntegrity()) {
                    mPresenter.upload(mFlag);
                }
                return false;
            }
        });

        mPresenter.start();
    }

    @Override
    protected void setupPresenter() {
        mPresenter = new FlagPresenter(this,
                App.getInjectClass().getSplashSource(),
                App.getInjectClass().getFlagService());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_flag;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String searchContent = data.getExtras().getString(Config.KEY_BUNDLE_BIRD);
            Bird bird = Util.deserialize(searchContent, Bird.class);
            mBirdName.setText(bird.getNameZh());
            mFlag.setBirdId(bird.getId());
        }
    }

    @Override
    public void showLoading() {
        mProgressDialog.show();
    }

    @Override
    public void closeLoading() {
        mProgressDialog.dismiss();
    }

    @Override
    public void showUploadSuccess() {
        Toast.makeText(this, R.string.action_success, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showUploadFail() {
        Toast.makeText(this, R.string.action_fail, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMain() {
        finish();
    }

    @Override
    public boolean checkIntegrity() {
        if (mFlag.getBirdId() == null) {
            Toast.makeText(this, "请选择鸟种名称", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(mFlagText.getText()) ||
                TextUtils.isEmpty(mLd.getText()) ||
                TextUtils.isEmpty(mRd.getText()) ||
                TextUtils.isEmpty(mLu.getText()) ||
                TextUtils.isEmpty(mRu.getText())) {
            Toast.makeText(this, "请选择旗标对应的所有颜色", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(mFlagCode.getText())) {
            Toast.makeText(this, "请输入旗标编码", Toast.LENGTH_SHORT).show();
        } else {
            mFlag.setFlagColorCombination(mFlagText.getText().toString());
            mFlag.setLd(mLd.getText().toString());
            mFlag.setRd(mRd.getText().toString());
            mFlag.setLu(mLu.getText().toString());
            mFlag.setRu(mRu.getText().toString());
            mFlag.setCode(mFlagCode.getText().toString());
            mFlag.setNote(mRemark.getText().toString());
            return true;
        }
        return false;
    }
}
