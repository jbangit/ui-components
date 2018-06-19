package com.jbangit.uicomponents.nav;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.jbangit.uicomponents.R;
import com.jbangit.uicomponents.common.DensityUtils;

public class NavGrid extends ViewGroup {

    private int mGridSize;

    private int mDividerSize;

    private Paint mDividerPaint;

    private int mBackgroundColor;

    public NavGrid(Context context) {
        super(context);
        defaultInit();
    }

    private void defaultInit() {
        setWillNotDraw(false);
    }

    public NavGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
        defaultInit();
        initAttr(context, attrs);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        mBackgroundColor = getResources().getColor(R.color.colorForeground);
        mDividerSize = DensityUtils.getPxFromDp(context, 1);

        mDividerPaint = new Paint();
        mDividerPaint.setColor(getResources().getColor(R.color.colorBackground));
        mDividerPaint.setStrokeWidth(mDividerSize);
        mDividerPaint.setStyle(Paint.Style.STROKE);
    }

    public NavGrid(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        defaultInit();
        initAttr(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();

        int left, top;
        left = mDividerSize;
        top = mDividerSize;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            childView.layout(
                    left,
                    top,
                    left + childView.getMeasuredWidth(),
                    top + childView.getMeasuredHeight());

            left += mGridSize + mDividerSize;

            if ((i + 1) % 4 == 0) {
                // is the end of the row
                left = mDividerSize;
                top += mGridSize + mDividerSize;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawARGB(
                Color.alpha(mBackgroundColor),
                Color.red(mBackgroundColor),
                Color.green(mBackgroundColor),
                Color.blue(mBackgroundColor));

        int childCount = getChildCount();

        float left, top;
        left = mDividerSize;
        top = mDividerSize;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            canvas.drawRect(
                    left - mDividerSize / 2f,
                    top - mDividerSize / 2f,
                    left + childView.getMeasuredWidth() + mDividerSize / 2f,
                    top + childView.getMeasuredHeight() + mDividerSize / 2f,
                    mDividerPaint);

            left += mGridSize + mDividerSize;

            if ((i + 1) % 4 == 0) {
                // is the end of the row
                left = mDividerSize;
                top += mGridSize + mDividerSize;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        mGridSize = 0;

        int width = 0;
        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                width = widthSize;
                break;
            case MeasureSpec.AT_MOST:
                width = widthSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                width = widthSize;
                break;
        }

        int dividerNbr = 4 + 1;
        mGridSize = (width - (dividerNbr * mDividerSize)) / 4;

        measureChildView();

        int expectHeight = (getChildCount() / 4 + 1) * (mGridSize + mDividerSize) + mDividerSize;
        int height = 0;
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            case MeasureSpec.AT_MOST:
                height = Math.min(expectHeight, heightSize);
                break;
            case MeasureSpec.UNSPECIFIED:
                height = expectHeight;
                break;
        }

        setMeasuredDimension(width, height);
    }

    private void measureChildView() {
        int childSize = mGridSize;
        int w = MeasureSpec.makeMeasureSpec(childSize, MeasureSpec.EXACTLY);
        int h = MeasureSpec.makeMeasureSpec(childSize, MeasureSpec.EXACTLY);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.measure(w, h);
        }
    }
}
