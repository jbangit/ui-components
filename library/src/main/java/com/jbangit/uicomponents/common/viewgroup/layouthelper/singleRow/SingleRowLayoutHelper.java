package com.jbangit.uicomponents.common.viewgroup.layouthelper.singleRow;

import android.view.View;
import android.view.View.MeasureSpec;

import com.jbangit.uicomponents.common.viewgroup.layouthelper.LayoutHelper;

public class SingleRowLayoutHelper implements LayoutHelper {

    public static final int STYLE_AUTO = 0;

    public static final int STYLE_FILL = 1;

    public static final int STYLE_WRAP = 2;

    private OnGetWidthListener mOnGetWidthListener;

    private ViewGroupHelper mViewGroupHelper;

    private int mStyle = STYLE_AUTO;

    @Override
    public void onViewGroupMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int expectedWidth = mOnGetWidthListener.onGetWidth();

        int width = 0;
        switch (widthMode) {
            case MeasureSpec.AT_MOST:
                width = Math.min(expectedWidth, widthSize);
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

        int expectedHeight = 0;
        switch (mStyle) {
            case STYLE_AUTO:
                expectedHeight = styleAutoMeasure(width, heightSize);
                break;
            case STYLE_FILL:
                expectedHeight = styleFillMeasure(width, heightSize);
                break;
            case STYLE_WRAP:
                expectedHeight = styleWrapMeasure(width);
                break;
        }

        int height = 0;
        switch (heightMode) {
            case MeasureSpec.AT_MOST:
                height = Math.min(expectedHeight, heightSize);
                break;
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                height = expectedHeight;
                break;
        }

        mViewGroupHelper.onSetMeasuredDimension(width, height);
    }

    private int styleFillMeasure(int width, int height) {
        int childViewHeight = height / mViewGroupHelper.getChildCount();
        measureChildView(width, childViewHeight, MeasureSpec.EXACTLY);
        return height;
    }

    private int styleWrapMeasure(int width) {
        measureChildView(width, 0, MeasureSpec.UNSPECIFIED);
        return getTotalChildViewsHeight();
    }

    private int styleAutoMeasure(int width, int heightSize) {
        int totalChildViewsHeight = styleWrapMeasure(width);

        if (totalChildViewsHeight < heightSize) {
            styleFillMeasure(width, heightSize);
            return heightSize;
        } else {
            return totalChildViewsHeight;
        }
    }

    private int getTotalChildViewsHeight() {
        int childCount = mViewGroupHelper.getChildCount();

        int totalHeight = 0;
        for (int i = 0; i < childCount; i++) {
            totalHeight += mViewGroupHelper.getChildView(i).getMeasuredHeight();
        }

        return totalHeight;
    }

    private void measureChildView(int width, int height, int heightMode) {
        int childCount = mViewGroupHelper.getChildCount();

        for (int i = 0; i < childCount; i++) {
            mViewGroupHelper
                    .getChildView(i)
                    .measure(
                            MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(height, heightMode));
        }
    }

    @Override
    public void onViewGroupLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = mViewGroupHelper.getChildCount();

        int top = 0;
        for (int i = 0; i < childCount; i++) {
            View childView = mViewGroupHelper.getChildView(i);

            childView.layout(
                    0,
                    top,
                    childView.getMeasuredWidth(),
                    top + childView.getMeasuredHeight());
            top += childView.getMeasuredHeight();
        }
    }

    public void setStyle(int style) {
        mStyle = style;
    }

    @Override
    public void setViewGroupHelper(ViewGroupHelper viewGroupHelper) {
        mViewGroupHelper = viewGroupHelper;
    }

    public void setOnGetWidthListener(OnGetWidthListener onGetWidthListener) {
        mOnGetWidthListener = onGetWidthListener;
    }

    public interface OnGetWidthListener {

        int onGetWidth();
    }
}
