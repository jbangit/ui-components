package com.jbangit.uicomponents.cell;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jbangit.uicomponents.R;
import com.jbangit.uicomponents.common.Globals;

public class CheckedCell extends FrameLayout implements Checkable {

    private Drawable mCheckedDrawable;

    private String mAttrTitle;

    private boolean mAttrChecked;

    private int mAttrCheckedColor;

    private TextView mTitle;

    private ImageView mIcChecked;

    private boolean mChecked;

    private OnCheckedChangeListener mOnCheckedChangeListener;

    public CheckedCell(@NonNull Context context) {
        super(context);
    }

    public CheckedCell(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        defaultInit(context);
        initAttr(context, attrs);
    }

    private void defaultInit(Context context) {
        mCheckedDrawable = context.getResources().getDrawable(R.drawable.ic_check_circle).mutate();
        setSaveEnabled(true);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CheckedCell);
        mAttrTitle = typedArray.getString(R.styleable.CheckedCell_checkedCellTitle);
        mAttrChecked = typedArray.getBoolean(R.styleable.CheckedCell_checkedCellChecked, false);
        mAttrCheckedColor = typedArray.getColor(
                R.styleable.CheckedCell_checkedCellCheckedColor, Globals.getPrimaryColor(context));
        typedArray.recycle();
    }

    public CheckedCell(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        defaultInit(context);
        initAttr(context, attrs);
    }

    @BindingAdapter("onCheckedChanged")
    public static void setOnCheckedChangeListener(
            CheckedCell cell, OnCheckedChangeListener listener) {
        cell.setOnCheckedChangeListener(listener);
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        SaveState saveState = new SaveState(super.onSaveInstanceState());
        saveState.checked = mChecked;
        return saveState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SaveState saveState = (SaveState) state;
        setChecked(saveState.checked);
        super.onRestoreInstanceState(saveState.getSuperState());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        inflate();
        initViews();
    }

    private void inflate() {
        inflate(getContext(), R.layout.view_checked_cell, this);
        mTitle = findViewById(R.id.title);
        mIcChecked = findViewById(R.id.ic_checked);
    }

    private void initViews() {
        setTitle(mAttrTitle);
        setChecked(mAttrChecked);
        setCheckedColor(mAttrCheckedColor);
        setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toggle();

                        if (mOnCheckedChangeListener != null) {
                            mOnCheckedChangeListener.onCheckedChange(CheckedCell.this, mChecked);
                        }
                    }
                });
    }

    /**
     * Not supports runtime set color, for now
     */
    private void setCheckedColor(@ColorInt int color) {
        DrawableCompat.setTint(mCheckedDrawable, color);
    }

    @Override
    public void toggle() {
        mChecked = !mChecked;
        showChecked();
    }

    private void showChecked() {
        if (mChecked) {
            mIcChecked.setImageDrawable(mCheckedDrawable);
        } else {
            mIcChecked.setImageDrawable(null);
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    public CharSequence getTitle() {
        return mTitle.getText();
    }

    public void setTitle(CharSequence charSequence) {
        mTitle.setText(charSequence);
    }

    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;
        showChecked();
    }

    public OnCheckedChangeListener getOnCheckedChangeListener() {
        return mOnCheckedChangeListener;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    public interface OnCheckedChangeListener {

        /**
         * @param checkedCell the checked cell view
         * @param checked     is checked now
         */
        void onCheckedChange(CheckedCell checkedCell, boolean checked);
    }

    private static class SaveState extends BaseSavedState {

        public static final Creator<SaveState> CREATOR =
                new Creator<SaveState>() {
                    public SaveState createFromParcel(Parcel in) {
                        return new SaveState(in);
                    }

                    public SaveState[] newArray(int size) {
                        return new SaveState[size];
                    }
                };

        public boolean checked;

        SaveState(Parcelable superState) {
            super(superState);
        }

        SaveState(Parcel source) {
            super(source);
            checked = (source.readInt() == 1);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(checked ? 1 : 0);
        }
    }


}
