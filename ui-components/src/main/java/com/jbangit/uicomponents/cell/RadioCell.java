package com.jbangit.uicomponents.cell;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
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

public class RadioCell extends FrameLayout implements Checkable {

    private Drawable mRadioOnDrawable;

    private Drawable mRadioOffDrawable;

    private TextView mTitle;

    private ImageView mIcRadio;

    private boolean mRadio;

    private String mAttrTitle;

    private boolean mAttrChecked;

    private OnRadioChangeListener mOnRadioChangeListener;

    public RadioCell(@NonNull Context context) {
        super(context);
        defaultInit(context);
    }

    private void defaultInit(Context context) {
        mRadioOnDrawable = context.getResources().getDrawable(R.drawable.ic_radio_on).mutate();
        mRadioOffDrawable = context.getResources().getDrawable(R.drawable.ic_radio_off).mutate();
    }

    public RadioCell(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        defaultInit(context);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RadioCell);
        mAttrTitle = typedArray.getString(R.styleable.RadioCell_radioCellTitle);
        int attrOnColor =
                typedArray.getColor(
                        R.styleable.RadioCell_radioCellOnColor, Globals.getPrimaryColor(context));
        DrawableCompat.setTint(mRadioOnDrawable, attrOnColor);
        int attrOffColor = context.getResources().getColor(R.color.colorTextGray);
        DrawableCompat.setTint(mRadioOffDrawable, attrOffColor);
        mAttrChecked = typedArray.getBoolean(R.styleable.RadioCell_radioCellOn, false);
        typedArray.recycle();
    }

    public RadioCell(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        defaultInit(context);
        initAttrs(context, attrs);
    }

    @BindingAdapter("onRadioChanged")
    public static void setOnRadioChangeListener(RadioCell radioCell,
                                                OnRadioChangeListener listener) {
        radioCell.setOnRadioChangeListener(listener);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        inflate();
        initViews();
    }

    private void inflate() {
        View view = inflate(getContext(), R.layout.view_raido_cell, this);
        mTitle = view.findViewById(R.id.title);
        mIcRadio = view.findViewById(R.id.ic_checked);
    }

    private void initViews() {
        mIcRadio.setImageDrawable(mRadioOffDrawable);

        setTitle(mAttrTitle);
        setChecked(mAttrChecked);
        setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toggle();
                        if (mOnRadioChangeListener != null) {
                            mOnRadioChangeListener.onRadioChange(RadioCell.this, mRadio);
                        }
                    }
                });
    }

    @Override
    public void toggle() {
        mRadio = !mRadio;
        showRadio();
    }

    private void showRadio() {
        if (mRadio) {
            mIcRadio.setImageDrawable(mRadioOnDrawable);
        } else {
            mIcRadio.setImageDrawable(mRadioOffDrawable);
        }
    }

    public CharSequence getTitle() {
        return mTitle.getText();
    }

    @Override
    public void setChecked(boolean checked) {
        mRadio = checked;
        showRadio();
    }

    public void setTitle(CharSequence title) {
        mTitle.setText(title);
    }

    public OnRadioChangeListener getOnRadioChangeListener() {
        return mOnRadioChangeListener;
    }

    @Override
    public boolean isChecked() {
        return mRadio;
    }

    public void setOnRadioChangeListener(OnRadioChangeListener onRadioChangeListener) {
        mOnRadioChangeListener = onRadioChangeListener;
    }

    public interface OnRadioChangeListener {

        /**
         * Callback only when radio changed with user
         */
        void onRadioChange(RadioCell radioCell, boolean radio);
    }


}
