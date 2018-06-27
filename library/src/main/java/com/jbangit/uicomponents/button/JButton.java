package com.jbangit.uicomponents.button;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewOutlineProvider;

import com.jbangit.uicomponents.R;
import com.jbangit.uicomponents.common.DensityUtils;
import com.jbangit.uicomponents.common.Globals;
import com.jbangit.uicomponents.utils.resource.ShapeDrawableUtils;

public class JButton extends android.support.v7.widget.AppCompatTextView {

    public static final int STYLE_FILL = 0;

    public static final int STYLE_STROKE = 1;

    private static final int DEFAULT_ELEVATION = 2;

    private static final int DEFAULT_STROKE_WIDTH = 1;

    private static final int DEFAULT_TEXT_SIZE = 16;

    private String mAttrTitle;

    private int mAttrColor;

    private int mAttrStyle;

    private float mAttrRadius;

    public JButton(Context context) {
        super(context);
    }

    public JButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        defaultInit();
        initAttrs(context, attrs);
        initView();
    }

    public JButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        defaultInit();
        initAttrs(context, attrs);
        initView();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.JButton);
        mAttrTitle = typedArray.getString(R.styleable.JButton_jButtonTitle);
        mAttrColor =
                typedArray.getColor(
                        R.styleable.JButton_jButtonColor, Globals.getPrimaryColor(context));
        mAttrStyle = typedArray.getInt(R.styleable.JButton_jButtonStyle, STYLE_FILL);
        mAttrRadius = typedArray.getDimension(R.styleable.JButton_jButtonRadius, 0);
        typedArray.recycle();
    }

    private void defaultInit() {
        initPadding();
        setGravity(Gravity.CENTER);
        setTextSize(DEFAULT_TEXT_SIZE);
    }

    private void initPadding() {
        int vPadding = DensityUtils.getPxFromDp(getContext(), 12);
        int hPadding = DensityUtils.getPxFromDp(getContext(), 16);
        setPadding(hPadding, vPadding, hPadding, vPadding);
    }

    private void initView() {
        int colorSolid = Color.TRANSPARENT;
        int colorStroke = Color.TRANSPARENT;
        int colorText = Color.TRANSPARENT;

        switch (mAttrStyle) {
            case STYLE_FILL:
                colorSolid = mAttrColor;
                colorStroke = mAttrColor;
                colorText = getResources().getColor(R.color.colorForeground);
                break;
            case STYLE_STROKE:
                colorSolid = Color.TRANSPARENT;
                colorStroke = mAttrColor;
                colorText = mAttrColor;
                break;
        }

        Drawable background = ShapeDrawableUtils.builder(getContext())
                        .solid(colorSolid)
                        .stroke(DEFAULT_STROKE_WIDTH, colorStroke)
                        .cornerPx((int) mAttrRadius)
                .build();
        setBackground(Globals.addRipple(getContext(), background));

        setText(mAttrTitle);
        setTextColor(colorText);

        setElevation();
    }

    private void setElevation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setOutlineProvider(
                    new ViewOutlineProvider() {
                        @Override
                        public void getOutline(View view, Outline outline) {
                            getBackground().getOutline(outline);
                        }
                    });
            setElevation(DensityUtils.getPxFromDp(getContext(), DEFAULT_ELEVATION));
        }
    }
}
