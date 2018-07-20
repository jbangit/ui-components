package com.jbangit.uicomponents.common.viewgroup.layouthelper.singleline;

import android.view.View;
import android.view.View.MeasureSpec;

import com.jbangit.uicomponents.common.viewgroup.layouthelper.LayoutHelper;

public class SingleLineLayoutHelper implements LayoutHelper {

    public static final int STYLE_AUTO = 0;

    public static final int STYLE_FILL = 1;

    public static final int STYLE_WRAP = 2;

    private int mStyle = STYLE_AUTO;

    private OnGetViewHeight mOnGetViewHeight;

    private ViewGroupHelper mViewGroupHelper;

    @Override
    public void onViewGroupMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int expectedHeight = mOnGetViewHeight.onGetHeight();
        int expectedWidth = 0;

        switch (mStyle) {
            case STYLE_AUTO:
                expectedWidth = styleAutoMeasure(widthSize, expectedHeight);
                break;
            case STYLE_FILL:
                expectedWidth = styleFillMeasure(widthSize, expectedHeight);
                break;
            case STYLE_WRAP:
                expectedWidth = styleWrapMeasure(expectedHeight);
                break;
        }

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
        int childViewWidth = width / mViewGroupHelper.getChildCount();
        measureChildView(childViewWidth, MeasureSpec.EXACTLY, height);
        return width;
    }

    private int styleWrapMeasure(int height) {
        measureChildView(0, MeasureSpec.UNSPECIFIED, height);
        return getTotalChildViewWidth();
    }

    private int styleAutoMeasure(int width, int height) {
        int totalChildViewWidth = styleWrapMeasure(height);

        if (totalChildViewWidth < width) {
            return styleFillMeasure(width, height);
        } else {
            return totalChildViewWidth;
        }
    }

    private int getTotalChildViewWidth() {
        int childCount = mViewGroupHelper.getChildCount();
        int totalWidth = 0;
        for (int i = 0; i < childCount; i++) {
            View childView = mViewGroupHelper.getChildView(i);
            totalWidth += childView.getMeasuredWidth();
        }

        return totalWidth;
    }

    private void measureChildView(int widthSize, int widthMode, int heightSize) {
        int childCount = mViewGroupHelper.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = mViewGroupHelper.getChildView(i);
            childView.measure(
                    MeasureSpec.makeMeasureSpec(widthSize, widthMode),
                    MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY));
        }
    }

    @Override
    public void onViewGroupLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = mViewGroupHelper.getChildCount();

        int left = 0;
        for (int i = 0; i < childCount; i++) {
            View childView = mViewGroupHelper.getChildView(i);
            childView.layout(
                    left, 0, left + childView.getMeasuredWidth(), childView.getMeasuredHeight());
            left += childView.getMeasuredWidth();
        }
    }

    @Override
    public void setViewGroupHelper(ViewGroupHelper viewGroupHelper) {
        mViewGroupHelper = viewGroupHelper;
    }

    public void setStyle(int style) {
        mStyle = style;
    }

    public void setOnGetHeightListener(OnGetViewHeight onGetViewHeight) {
        mOnGetViewHeight = onGetViewHeight;
    }

    public interface OnGetViewHeight {

        int onGetHeight();
    }
}
