package com.kalyter.ccwcc.util;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.kalyter.ccwcc.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimePicker implements DatePicker.OnDateChangedListener,TimePicker.OnTimeChangedListener {

    private String strDateTime;
    private View mView;
    private DatePicker mDatePicker;
    private TimePicker mTimePicker;
    private AlertDialog mAlertDialog;
    private Calendar mCalendar;
    private TextView mTextView;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");


    public DateTimePicker(View mView,TextView mTextView) {
        this.mView = mView;
        this.mTextView=mTextView;
        strDateTime= (String) mTextView.getText();
        mCalendar =Calendar.getInstance();
    }
    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mCalendar.set(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth()
                , mTimePicker.getCurrentHour(), mTimePicker.getCurrentMinute());
        strDateTime = simpleDateFormat.format(mCalendar.getTime());
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        mCalendar.set(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth()
                , mTimePicker.getCurrentHour(), mTimePicker.getCurrentMinute());
        strDateTime = simpleDateFormat.format(mCalendar.getTime());
    }

    public AlertDialog DateTimePickerDialog() {
        LinearLayout layoutDateTime = (LinearLayout) LayoutInflater.from(mView.getContext()).inflate(R.layout.layout_datetime,null);
        try {
            mDatePicker = (DatePicker) layoutDateTime.findViewById(R.id.date_picker);
            mTimePicker = (TimePicker) layoutDateTime.findViewById(R.id.time_picker);
            mTimePicker.setIs24HourView(true);
            mTimePicker.setOnTimeChangedListener(this);
            mCalendar.setTime(simpleDateFormat.parse(strDateTime));
            mDatePicker.setMaxDate(new Date().getTime());
            mDatePicker.init(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), this);
            mTimePicker.setCurrentHour(mCalendar.get(Calendar.HOUR_OF_DAY));
            mTimePicker.setCurrentMinute(mCalendar.get(Calendar.MINUTE));
            mDatePicker.setMaxDate(new Date().getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mAlertDialog=new AlertDialog.Builder(mView.getContext())
                .setView(layoutDateTime)
                .setPositiveButton("完成", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTextView.setText(strDateTime);
                    }
                }).show();
        return mAlertDialog;
    }


}
