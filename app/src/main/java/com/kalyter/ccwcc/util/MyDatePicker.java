package com.kalyter.ccwcc.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kalyter.ccwcc.R;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyDatePicker implements DatePicker.OnDateChangedListener {

    private TextView mTextView;
    private Context mContext;
    private DatePicker mDatePicker;
    private String strDate;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Calendar mCalendar;

    public MyDatePicker(Context mContext, TextView mTextView) {
        this.mContext = mContext;
        this.mTextView = mTextView;
        mCalendar =Calendar.getInstance();
        strDate = simpleDateFormat.format(mCalendar.getTime());
    }

    public void showYearMonthPickerDialog() {
        try {
            LinearLayout layout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.dialog_year_month_picker, null);
            mDatePicker= (DatePicker) layout.findViewById(R.id.date_picker_year_month);
            mDatePicker.setMaxDate(new Date().getTime());
            if (!strDate.equals("")) {
                mCalendar.setTime(simpleDateFormat.parse(strDate));
            }
            mDatePicker.init(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), this);
            mDatePicker.setCalendarViewShown(false);
            new AlertDialog.Builder(mContext)
                    .setTitle(R.string.title_dialog_choos_date)
                    .setView(layout)
                    .setPositiveButton(R.string.caption_confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mTextView.setText(strDate);
                        }
                    }).show();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mCalendar.set(mDatePicker.getYear(),mDatePicker.getMonth(),mDatePicker.getDayOfMonth());
        strDate = simpleDateFormat.format(mCalendar.getTime());
    }


}
