package com.kalyter.ccwcc.add;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.bigkoo.pickerview.TimePickerView;
import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.common.App;
import com.kalyter.ccwcc.common.BaseFragment;
import com.kalyter.ccwcc.common.Config;
import com.kalyter.ccwcc.model.Bird;
import com.kalyter.ccwcc.model.Record;
import com.kalyter.ccwcc.search.SearchActivity;
import com.kalyter.ccwcc.util.Util;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class AddFragment extends BaseFragment implements AddContract.View {
    private static final String TAG = "AddFragment";

    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.text_add_date)
    TextView mTextAddDate;
    @BindView(R.id.edit_add_detail)
    EditText mEditAddDetail;
    @BindView(R.id.add_birds)
    SwipeMenuListView mAddBirds;
    @BindView(R.id.locate_result)
    TextView mLocateResult;
    @BindView(R.id.weather)
    EditText mWeather;
    @BindView(R.id.switch_checkpoint)
    Spinner mSwitchCheckpoint;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    public static final int REQUEST_CODE = 200;
    private AddContract.Presenter mPresenter;
    private ProgressDialog mProgressDialog;
    private Record mRecord;

    @OnClick(R.id.locate)
    void locate() {
        mPresenter.locate();
    }

    @OnClick(R.id.layout_add_date)
    void showDate() {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(2000, 1, 1);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(new Date());
        Calendar recordCalendar = Calendar.getInstance();
        recordCalendar.setTime(mRecord.getRecordTime());
        new TimePickerView.Builder(getContext(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                mRecord.setRecordTime(date);
                mTextAddDate.setText(Config.yyyyMMddHHmmss.format(date));
            }
        })
                .setRangDate(startCalendar, endCalendar)
                .setDate(recordCalendar)
                .build().show();
    }

    @OnClick(R.id.button_add_batch_add)
    void batchAdd() {
        Intent intent = new Intent(getContext(), BatchAddActivity.class);
        startActivityForResult(intent, Config.REQUEST_BATCH_CODE);
    }

    @OnClick(R.id.button_add_search_add)
    void searchAdd() {
        Intent intent = new Intent(getContext(), SearchActivity.class);
        intent.putExtra(Config.KEY_REQUEST_SOURCE, REQUEST_CODE);
        startActivityForResult(intent, Config.REQUEST_SEARCH_CODE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecord = new Record();
        // 设置检查地默认值
        mRecord.setCheckpointId(1);
        Date now = new Date();
        mRecord.setRecordTime(now);
        mTextAddDate.setText(Config.yyyyMMddHHmmss.format(now));

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage(getString(R.string.loading));
        mProgressDialog.setCanceledOnTouchOutside(false);

        mTitle.setText(R.string.add_record);

        mSwitchCheckpoint.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mRecord.setCheckpointId(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mToolbar.inflateMenu(R.menu.submit);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (checkInformationIntegrity()) {
                    mPresenter.upload(mRecord);
                }
                return false;
            }
        });
        mPresenter.start();
    }

    @Override
    protected void setupPresenter() {
        mPresenter = new AddPresenter(App.getInjectClass().getRecordService(), this,
                App.getInjectClass().getDraftSource(),
                getContext(),
                App.getLocationClient(),
                App.getInjectClass().getSplashSource());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add;
    }

    @Override
    public void refreshUI() {
        mEditAddDetail.setText("");
        mWeather.setText("");
        mPresenter.clearData();
    }

    @Override
    public void setConfirmAdapter(BaseAdapter adapter) {
        mAddBirds.setAdapter(adapter);
        mAddBirds.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
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
    public void showActionSuccess() {
        Toast.makeText(getContext(), "操作成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showActionFail() {
        Toast.makeText(getContext(), "操作失败！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLocateResult(String result) {
        mLocateResult.setText(result);
    }

    @Override
    public void showLocateDenied() {
        Toast.makeText(getContext(), "没有正确获取定位权限，请允许获取位置信息", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void requestPermissions(String[] permissions) {
        Util.requestPermissions(getActivity(), permissions);
    }

    @Override
    public void showLocating() {
        mLocateResult.setText(R.string.locating);
    }

    @Override
    public void setMenuCreator(SwipeMenuCreator creator) {
        mAddBirds.setMenuCreator(creator);
    }

    @Override
    public void setOnMenuItemClickListener(SwipeMenuListView.OnMenuItemClickListener listener) {
        mAddBirds.setOnMenuItemClickListener(listener);
    }

    @Override
    public void showConfirmBirdCount(final int position) {
        new MaterialDialog.Builder(getContext())
                .title("请输入")
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .input("请输入鸟类数量", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        String content = input.toString();
                        if (!content.equals("")) {
                            mPresenter.editBird(position, Integer.parseInt(content));
                        }
                    }
                }).build().show();
    }

    @Override
    public void showDeleteBird(final int position) {
        new MaterialDialog.Builder(getContext())
                .title("确认删除此鸟种？")
                .positiveText(R.string.confirm)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mPresenter.deleteBird(position);
                    }
                })
                .negativeText(R.string.caption_cancel)
                .build().show();
    }

    @Override
    public void showSaveToDraft() {
        Toast.makeText(getContext(), "网络上传失败，自动保存到草稿箱", Toast.LENGTH_SHORT).show();
    }

    private boolean checkInformationIntegrity() {
        if (TextUtils.isEmpty(mWeather.getText())) {
            Toast.makeText(getContext(), "请填写天气", Toast.LENGTH_SHORT).show();
        } else if (mRecord.getRecordTime() == null) {
            Toast.makeText(getContext(), "请选择观察日期", Toast.LENGTH_SHORT).show();
        } else if (mPresenter.getSelectBirds().size() == 0) {
            Toast.makeText(getContext(), "请填写鸟类和数量信息", Toast.LENGTH_SHORT).show();
        } else {
            mRecord.setWeather(mWeather.getText().toString());
            mRecord.setDetail(mEditAddDetail.getText().toString());
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Config.REQUEST_BATCH_CODE:
                    String s = data.getExtras().getString(Config.KEY_BUNDLE_SELECT_BIRDS);
                    List<Bird> birds = Util.deserializeArray(s, Bird[].class);
                    mPresenter.addBirds(birds);
                    break;
                case Config.REQUEST_SEARCH_CODE:
                    String searchContent = data.getExtras().getString(Config.KEY_BUNDLE_BIRD);
                    Bird bird = Util.deserialize(searchContent, Bird.class);
                    mPresenter.addBird(bird);
                    break;
            }
        }
    }
}
