package com.jbangit.uicomponents.button;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.widget.TextViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jbangit.uicomponents.R;
import com.jbangit.uicomponents.common.DensityUtils;
import com.jbangit.uicomponents.common.Globals;
import com.jbangit.uicomponents.common.resource.ShapeDrawableUtils;

public class JButton extends ViewGroup {

    public static final int STYLE_FILL = 0;

    public static final int STYLE_STROKE = 1;

    public static final int STYLE_CHIP = 2;

    private static final int SIZE_NORMAL = 0;

    private static final int SIZE_LITTLE = 1;

    private static final int SIZE_CHIP = 2;

    private static final int ICON_GRAVITY_LEFT = 0;

    private static final int ICON_GRAVITY_TOP = 1;

    private static final int ICON_GRAVITY_RIGHT = 2;

    private static final int ICON_GRAVITY_BOTTOM = 3;

    private static final int SHAPE_RECT = 0;

    private static final int SHAPE_OVAL = 1;

    private static final int DEFAULT_STROKE_WIDTH = 1;

    private String mAttrTitle = null;

    private int mAttrRadius;

    private Drawable mAttrIcon;

    private TextView mTitle;

    private ImageView mIcon;

    private int mAttrHPadding;

    private int mAttrVPadding;

    @ColorInt
    private int mAttrTextColor;

    @ColorInt
    private int mSolidColor;

    @ColorInt
    private int mStrokeColor;

    @ColorInt
    private int mDisableColor = getResources().getColor(R.color.colorTextLightGray);

    private int mAttrTextSize = DensityUtils.getPxFromSp(getContext(), 16);

    private int mAttrInsetPadding = DensityUtils.getPxFromDp(getContext(), 4);

    private int mAttrIconGravity = ICON_GRAVITY_LEFT;

    private int mAttrShape = SHAPE_RECT;

    private boolean mAttrBold = false;

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

    private void defaultInit() {
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.JButton);
        int attrColor =
                typedArray.getColor(
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
                mAttrHPadding = DensityUtils.getPxFromDp(context, 12);
                mAttrVPadding = DensityUtils.getPxFromDp(context, 12);
                mAttrTextSize = DensityUtils.getPxFromSp(context, 16);
                break;

            case SIZE_LITTLE:
                mAttrHPadding = DensityUtils.getPxFromDp(context, 12);
                mAttrVPadding = DensityUtils.getPxFromDp(context, 8);
                mAttrTextSize = DensityUtils.getPxFromSp(context, 14);
                break;

            case SIZE_CHIP:
                mAttrHPadding = DensityUtils.getPxFromDp(context, 12);
                mAttrVPadding = DensityUtils.getPxFromDp(context, 4);
                mAttrTextSize = DensityUtils.getPxFromSp(context, 14);
                break;
        }

        mAttrTitle = typedArray.getString(R.styleable.JButton_jButtonTitle);
        mAttrIcon = typedArray.getDrawable(R.styleable.JButton_jButtonIcon);
        mAttrHPadding =
                typedArray.getDimensionPixelOffset(
                        R.styleable.JButton_jButtonHPadding, mAttrHPadding);
        mAttrVPadding =
                typedArray.getDimensionPixelOffset(
                        R.styleable.JButton_jButtonVPadding, mAttrVPadding);
        mAttrInsetPadding =
                typedArray.getDimensionPixelOffset(
                        R.styleable.JButton_jButtonInsetPadding, mAttrInsetPadding);
        mAttrTextColor = typedArray.getColor(R.styleable.JButton_jButtonTextColor, mAttrTextColor);
        mAttrTextSize =
                typedArray.getDimensionPixelOffset(
                        R.styleable.JButton_jButtonTextSize, mAttrTextSize);
        mAttrIconGravity =
                typedArray.getInt(R.styleable.JButton_jButtonIconGravity, mAttrIconGravity);
        mAttrRadius =
                typedArray.getDimensionPixelOffset(R.styleable.JButton_jButtonRadius, mAttrRadius);
        mAttrShape = typedArray.getInt(R.styleable.JButton_jButtonShape, mAttrShape);
        mAttrBold = typedArray.getBoolean(R.styleable.JButton_jButtonBold, mAttrBold);
        mStrokeColor = typedArray.getColor(R.styleable.JButton_jButtonStrokeColor, mStrokeColor);

        typedArray.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        inflate();
    }

    private void inflate() {
        mTitle = new TextView(getContext());
        mIcon = new ImageView(getContext());

        initViews();

        addViewInLayout(mTitle, -1, generateDefaultLayoutParams(), true);
        addViewInLayout(mIcon, -1, generateDefaultLayoutParams(), true);
        requestLayout();
    }

    private void initViews() {
        mTitle.setText(mAttrTitle);
        if (mAttrIcon != null) {
            mIcon.setVisibility(View.VISIBLE);
            mIcon.setImageDrawable(mAttrIcon);
        } else {
            mIcon.setVisibility(View.GONE);
        }

        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mAttrTextSize);
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(
                mTitle, 1, mAttrTextSize, 1, TypedValue.COMPLEX_UNIT_PX);

        mTitle.setTextColor(mAttrTextColor);
        if (mAttrBold) {
            mTitle.setTypeface(null, Typeface.BOLD);
        }

        setPadding(mAttrHPadding, mAttrVPadding, mAttrHPadding, mAttrVPadding);
        setBackground(
                Globals.addRipple(
                        getContext(), getBackgroundDrawable(), getBackgroundMaskDrawable()));
    }

    private Drawable getBackgroundDrawable() {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_enabled},
                ShapeDrawableUtils.builder(getContext())
                        .solid(mSolidColor)
                        .stroke(DEFAULT_STROKE_WIDTH, mStrokeColor)
                        .cornerPx(mAttrRadius)
                        .shape(getShape(mAttrShape))
                        .build());
        stateListDrawable.addState(new int[]{},
                ShapeDrawableUtils.builder(getContext())
                        .solid(mDisableColor)
                        .stroke(DEFAULT_STROKE_WIDTH, mDisableColor)
                        .cornerPx(mAttrRadius)
                        .shape(getShape(mAttrShape))
                        .build());
        return stateListDrawable;
    }

    private Drawable getBackgroundMaskDrawable() {
        return ShapeDrawableUtils.builder(getContext())
                .solid(Color.BLACK)
                .stroke(DEFAULT_STROKE_WIDTH, Color.BLACK)
                .cornerPx(Math.round(mAttrRadius))
                .shape(getShape(mAttrShape))
                .build();
    }

    private int getShape(int attrShape) {
        switch (attrShape) {
            case SHAPE_RECT:
                return GradientDrawable.RECTANGLE;
            case SHAPE_OVAL:
                return GradientDrawable.OVAL;
            default:
                return GradientDrawable.RECTANGLE;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int insetPadding = (mIcon.getDrawable() == null || mTitle.getText().length() == 0) ? 0 : mAttrInsetPadding;

        mTitle.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        mIcon.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

        int expectedWidth = 0;
        int expectedHeight = 0;

        switch (mAttrIconGravity) {
            case ICON_GRAVITY_LEFT:
            case ICON_GRAVITY_RIGHT:
                expectedWidth =
                        mTitle.getMeasuredWidth()
                                + insetPadding
                                + mIcon.getMeasuredWidth()
                                + getPaddingLeft()
                                + getPaddingRight();
                expectedHeight =
                        Math.max(mTitle.getMeasuredHeight(), mIcon.getMeasuredHeight())
                                + getPaddingTop()
                                + getPaddingBottom();
                break;
            case ICON_GRAVITY_TOP:
            case ICON_GRAVITY_BOTTOM:
                expectedWidth =
                        Math.max(mTitle.getMeasuredWidth(), mIcon.getMeasuredWidth())
                                + getPaddingLeft()
                                + getPaddingRight();
                expectedHeight =
                        mTitle.getMeasuredHeight()
                                + insetPadding
                                + mIcon.getMeasuredHeight()
                                + getPaddingTop()
                                + getPaddingBottom();
                break;
        }

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int width = 0;
        switch (widthMode) {
            case MeasureSpec.AT_MOST:
                width = Math.min(widthSize, expectedWidth);
                break;
            case MeasureSpec.EXACTLY:
                width = widthSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                width = expectedWidth;
                break;
        }

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int height = 0;
        switch (heightMode) {
            case MeasureSpec.AT_MOST:
                height = Math.min(heightSize, expectedHeight);
                break;
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                height = expectedHeight;
                break;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int insetPadding = (mIcon.getDrawable() == null || mTitle.getText().length() == 0) ? 0 : mAttrInsetPadding;
        switch (mAttrIconGravity) {
            case ICON_GRAVITY_LEFT:
            case ICON_GRAVITY_RIGHT:
                int left =
                        (getMeasuredWidth()
                                - mIcon.getMeasuredWidth()
                                - mTitle.getMeasuredWidth()
                                - insetPadding)
                                / 2;
                int iconTop = (getMeasuredHeight() - mIcon.getMeasuredHeight()) / 2;
                int titleTop = (getMeasuredHeight() - mTitle.getMeasuredHeight()) / 2;

                if (mAttrIconGravity == ICON_GRAVITY_LEFT) {
                    mIcon.layout(
                            left,
                            iconTop,
                            left + mIcon.getMeasuredWidth(),
                            iconTop + mIcon.getMeasuredHeight());
                    left += mIcon.getMeasuredWidth() + insetPadding;

                    mTitle.layout(
                            left,
                            titleTop,
                            left + mTitle.getMeasuredWidth(),
                            titleTop + mTitle.getMeasuredHeight());
                } else {
                    mTitle.layout(
                            left,
                            titleTop,
                            left + mTitle.getMeasuredWidth(),
                            titleTop + mTitle.getMeasuredHeight());

                    left += mTitle.getMeasuredWidth() + insetPadding;

                    mIcon.layout(
                            left,
                            iconTop,
                            left + mIcon.getMeasuredWidth(),
                            iconTop + mIcon.getMeasuredHeight());
                }
                break;
            case ICON_GRAVITY_TOP:
            case ICON_GRAVITY_BOTTOM:
                int top =
                        (getMeasuredHeight()
                                - mIcon.getMeasuredHeight()
                                - mTitle.getMeasuredHeight()
                                - insetPadding)
                                / 2;
                int iconLeft = (getMeasuredWidth() - mIcon.getMeasuredWidth()) / 2;
                int titleLeft = (getMeasuredWidth() - mTitle.getMeasuredWidth()) / 2;

                if (mAttrIconGravity == ICON_GRAVITY_TOP) {
                    mIcon.layout(
                            iconLeft,
                            top,
                            iconLeft + mIcon.getMeasuredWidth(),
                            top + mIcon.getMeasuredHeight());
                    top += mIcon.getMeasuredWidth() + insetPadding;

                    mTitle.layout(
                            titleLeft,
                            top,
                            titleLeft + mTitle.getMeasuredWidth(),
                            top + mTitle.getMeasuredHeight());
                } else {
                    mTitle.layout(
                            titleLeft,
                            top,
                            titleLeft + mTitle.getMeasuredWidth(),
                            top + mTitle.getMeasuredHeight());

                    top += mTitle.getMeasuredHeight() + insetPadding;

                    mIcon.layout(
                            iconLeft,
                            top,
                            iconLeft + mIcon.getMeasuredWidth(),
                            top + mIcon.getMeasuredHeight());
                }
                break;
        }
    }

    public CharSequence getTitle() {
        return mTitle.getText();
    }

    public void setTitle(CharSequence title) {
        mTitle.setText(title);
    }

    @BindingAdapter("jButtonTitle")
    public static void setTitle(JButton jButton, CharSequence title) {
        jButton.setTitle(title);
    }

    @BindingAdapter("jButtonEnable")
    public static void setEnable(JButton jButton, boolean enabled) {
        jButton.setEnabled(enabled);
    }
}
