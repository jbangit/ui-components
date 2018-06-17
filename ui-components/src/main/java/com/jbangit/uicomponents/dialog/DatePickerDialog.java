package com.jbangit.uicomponents.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.bigkoo.pickerview.listener.ISelectTimeCallback;
import com.bigkoo.pickerview.view.WheelTime;
import com.contrarywind.view.WheelView;
import com.jbangit.uicomponents.R;
import com.jbangit.uicomponents.common.Globals;
import com.jbangit.uicomponents.common.dialog.BottomDialogFragment;
import com.jbangit.uicomponents.common.dialog.FragmentUtils;
import com.jbangit.uicomponents.common.dialog.OnFragmentResultListener;
import com.jbangit.uicomponents.common.dialog.State;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 * <p>{@link DatePickerDialog#show(Fragment, int, Date, Date, Date)}
 *
 * <p>{@link DatePickerDialog#show(FragmentActivity, int, Date, Date, Date)}
 *
 * <p>{@link OnFragmentResultListener}
 *
 * <p>{@link DatePickerDialog#getDate()}
 */
public class DatePickerDialog extends BottomDialogFragment {

    public static final int OFFSET_WHEEL_3_D = 8;

    private WheelTime mWheelTime;

    @State private Date mFromDate;

    @State private Date mToDate;

    @State private Date mDate;

    public static void show(
            @NonNull FragmentActivity activity,
            int requestCode,
            Date fromDate,
            Date toDate,
            Date date) {
        DatePickerDialog dialog = new DatePickerDialog();
        dialog.mFromDate = fromDate;
        dialog.mToDate = toDate;
        dialog.mDate = date;
        FragmentUtils.show(dialog, activity, requestCode);
    }

    public static void show(
            @NonNull Fragment fragment, int requestCode, Date fromDate, Date toDate, Date date) {
        DatePickerDialog dialog = new DatePickerDialog();
        dialog.mFromDate = fromDate;
        dialog.mToDate = toDate;
        dialog.mDate = date;
        FragmentUtils.show(dialog, fragment, requestCode);
    }

    public Date getDate() {
        return mDate;
    }

    @Override
    public View onCreateContentView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentUtils.restoreState(this);

        View layoutView = inflater.inflate(R.layout.dialog_date_picker, container, false);
        initHeaderLayout(layoutView);
        initTimePickerLayout(layoutView);

        return layoutView;
    }

    private void initHeaderLayout(View layoutView) {
        @ColorInt int primaryColor = Globals.getPrimaryColor(layoutView.getContext());
        TextView ok = layoutView.findViewById(R.id.ok);
        TextView cancel = layoutView.findViewById(R.id.cancel);

        ok.setTextColor(primaryColor);
        cancel.setTextColor(primaryColor);

        ok.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentUtils.setResult(DatePickerDialog.this, true);
                        dismiss();
                    }
                });

        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentUtils.setResult(DatePickerDialog.this, false);
                        dismiss();
                    }
                });
    }

    private void initTimePickerLayout(View layoutView) {
        initPlaceHolder(layoutView);
        mWheelTime =
                new WheelTime(
                        layoutView.<LinearLayout>findViewById(R.id.timepicker),
                        new boolean[] {true, true, true, false, false, false},
                        Gravity.CENTER,
                        17);

        setDateRange();
        setDate();

        mWheelTime.setCyclic(false);
        mWheelTime.setTextXOffset(-OFFSET_WHEEL_3_D, 0, OFFSET_WHEEL_3_D, 0, 0, 0);
        mWheelTime.setLabels("年", "月", "日", null, null, null);

        mWheelTime.setSelectChangeCallback(
                new ISelectTimeCallback() {
                    @Override
                    public void onTimeSelectChanged() {
                        try {
                            mDate = WheelTime.dateFormat.parse(mWheelTime.getTime());
                        } catch (ParseException ignored) {
                        }
                    }
                });
    }

    /** hold a place that at the start and end of time picker layout, and it has dividers inside */
    private void initPlaceHolder(View layoutView) {
        WheelView endHolder = layoutView.findViewById(R.id.start);
        WheelView startHolder = layoutView.findViewById(R.id.end);
        endHolder.setAdapter(new ArrayWheelAdapter<>(Collections.singletonList("")));
        startHolder.setAdapter(new ArrayWheelAdapter<>(Collections.singletonList("")));
    }

    private void setDate() {
        int year, month, day;
        if (mDate == null) {
            mDate = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        mWheelTime.setPicker(year, month, day);
    }

    private void setDateRange() {
        Calendar from;
        if (mFromDate == null) {
            from = null;
        } else {
            from = Calendar.getInstance();
            from.setTime(mFromDate);
        }

        Calendar to;
        if (mToDate == null) {
            to = null;
        } else {
            to = Calendar.getInstance();
            to.setTime(mToDate);
        }

        mWheelTime.setRangDate(from, to);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        FragmentUtils.setResult(this, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        FragmentUtils.saveState(this);
    }
}
