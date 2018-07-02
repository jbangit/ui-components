package com.jbangit.uicomponents.button;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jbangit.uicomponents.R;
import com.jbangit.uicomponents.common.DensityUtils;
import com.jbangit.uicomponents.common.Globals;
import com.jbangit.uicomponents.utils.resource.ShapeDrawableUtils;

public class JButton extends FrameLayout {

    public static final int STYLE_FILL = 0;

    public static final int STYLE_STROKE = 1;

    public static final int STYLE_CHIP = 2;

    private static final int SIZE_NORMAL = 0;

    private static final int SIZE_LITTLE = 1;

    private static final int SIZE_CHIP = 2;

    private static final int DEFAULT_STROKE_WIDTH = 1;

    private String mAttrTitle;

    private float mAttrRadius;

    private Drawable mAttrIcon;

    private View mLayout;

    private TextView mTitle;

    private ImageView mIcon;

    private int mHPadding;

    private int mVPadding;

    @ColorInt
    private int mAttrTextColor;

    @ColorInt
    private int mSolidColor;

    @ColorInt
    private int mStrokeColor;

    private int mAttrTextSize;

    public JButton(Context context) {
        super(context);
    }

    public JButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        defaultInit();
        initAttrs(context, attrs);
    }

    public JButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        defaultInit();
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.JButton);
        mAttrTitle = typedArray.getString(R.styleable.JButton_jButtonTitle);
        mAttrIcon = typedArray.getDrawable(R.styleable.JButton_jButtonIcon);
        int attrColor = typedArray.getColor(
                R.styleable.JButton_jButtonColor, Globals.getPrimaryColor(context));

        int attrSize = SIZE_NORMAL;

        int attrStyle = typedArray.getInt(R.styleable.JButton_jButtonStyle, STYLE_FILL);
        switch (attrStyle) {
            case STYLE_FILL:
                mAttrTextColor = getResources().getColor(R.color.colorForeground);
                mSolidColor = attrColor;
                mStrokeColor = attrColor;
                break;
            case STYLE_STROKE:
                mAttrTextColor = attrColor;
                mSolidColor = Color.TRANSPARENT;
                mStrokeColor = attrColor;
                break;
            case STYLE_CHIP:
                mAttrTextColor = getResources().getColor(R.color.colorTextDark);
                mSolidColor = Color.TRANSPARENT;
                mStrokeColor = getResources().getColor(R.color.colorLine);
                mAttrRadius = DensityUtils.getPxFromDp(context, 15);
                attrSize = STYLE_CHIP;
                break;
        }

        attrSize = typedArray.getInt(R.styleable.JButton_jButtonSize, attrSize);
        switch (attrSize) {
            case SIZE_NORMAL:
                mHPadding = DensityUtils.getPxFromDp(context, 16);
                mVPadding = DensityUtils.getPxFromDp(context, 16);
                mAttrTextSize = DensityUtils.getPxFromSp(context, 16);
                break;

            case SIZE_LITTLE:
                mHPadding = DensityUtils.getPxFromDp(context, 12);
                mVPadding = DensityUtils.getPxFromDp(context, 8);
                mAttrTextSize = DensityUtils.getPxFromSp(context, 14);
                break;

            case SIZE_CHIP:
                mHPadding = DensityUtils.getPxFromDp(context, 12);
                mVPadding = DensityUtils.getPxFromDp(context, 4);
                mAttrTextSize = DensityUtils.getPxFromSp(context, 14);
                break;
        }

        mAttrRadius = typedArray.getDimension(R.styleable.JButton_jButtonRadius, mAttrRadius);
        mAttrTextColor = typedArray.getColor(R.styleable.JButton_jButtonTextColor, mAttrTextColor);
        mAttrTextSize = (int) typedArray.getDimension(R.styleable.JButton_jButtonTextSize, mAttrTextSize);

        typedArray.recycle();
    }

    private void defaultInit() {
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        inflate();

        initViews();
    }

    private void inflate() {
        mLayout = inflate(getContext(), R.layout.view_j_button, this);
        mTitle = mLayout.findViewById(R.id.title);
        mIcon = mLayout.findViewById(R.id.icon);
    }

    private void initViews() {
        mTitle.setText(mAttrTitle);
        if (mAttrIcon != null) {
            mIcon.setVisibility(View.VISIBLE);
            mIcon.setImageDrawable(mAttrIcon);
        } else {
            mIcon.setVisibility(View.GONE);
        }

        mTitle.setTextSize(mAttrTextSize);
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mAttrTextSize);
        mTitle.setTextColor(mAttrTextColor);

        mLayout.setPadding(mHPadding, mVPadding, mHPadding, mVPadding);
        mLayout.setBackground(Globals.addRipple(getContext(), getBackgroundDrawable()));
    }

    private Drawable getBackgroundDrawable() {
        return ShapeDrawableUtils.builder(getContext())
                .solid(mSolidColor)
                .stroke(DEFAULT_STROKE_WIDTH, mStrokeColor)
                .cornerPx((int) mAttrRadius)
                .build();
    }
}
