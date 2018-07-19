package com.jbangit.uicomponents.common.viewgroup.layouthelper.gridlayouthelper;

import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;

import com.jbangit.uicomponents.common.viewgroup.layouthelper.LayoutHelper;

/**
 * GridLayoutHelp layout a grid that has fixed width and number of row. And it's height is grow by
 * the number count.
 *
 * <p>You should override method {@link GridLayoutHelper#onMeasure(int)} and invoke method {@link
 * GridLayoutHelper#setSetMeasureResult(int, int, int, int)}
 */
abstract class GridLayoutHelper implements LayoutHelper {

    private ViewGroup mViewGroup;

    private LayoutHelper.ViewGroupHelper mViewGroupHelper;

    private int mRowNumber = 4;

    private int mHorizonInsetPadding;

    private int mVerticalInsetPadding;

    private int mChildViewWidth;

    private int mChildViewHeight;

    GridLayoutHelper(ViewGroup viewGroup) {
        mViewGroup = viewGroup;
    }

    public void setRowNumber(int rowNumber) {
        mRowNumber = rowNumber;
    }

    @Override
    public void onViewGroupMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = mViewGroupHelper.getChildCount();
        if (childCount == 0) {
            mViewGroupHelper.onSetMeasuredDimension(0, 0);
            return;
        }

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        switch (widthMode) {
            case MeasureSpec.AT_MOST:
                break;
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }

        onMeasure(width - mViewGroup.getPaddingStart() - mViewGroup.getPaddingEnd());

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int height = 0;
        switch (heightMode) {
            case MeasureSpec.AT_MOST:
                height = Math.min(heightSize, getExpectHeight());
                break;
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                height = getExpectHeight();
                break;
        }

        measureItem(mChildViewWidth, mChildViewHeight);

        mViewGroupHelper.onSetMeasuredDimension(width, height);
    }

    private int getExpectHeight() {
        int lineCount = ((mViewGroupHelper.getChildCount() - 1) / mRowNumber) + 1;

        int allInsetPaddingHeight = mVerticalInsetPadding * (lineCount - 1);
        int allPaddingHeight = mViewGroup.getPaddingTop() + mViewGroup.getPaddingBottom() + 2 * getExtraTopPadding();

        return lineCount * mChildViewHeight + allInsetPaddingHeight + allPaddingHeight;
    }

    private void measureItem(int itemWidth, int itemHeight) {
        int childCount = mViewGroupHelper.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View view = mViewGroupHelper.getChildView(i);
            view.measure(
                    MeasureSpec.makeMeasureSpec(itemWidth, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(itemHeight, MeasureSpec.EXACTLY));
        }
    }

    @Override
    public void onViewGroupLayout(boolean changed, int l, int t, int r, int b) {
        if (!changed) {
            return;
        }

        int left = mViewGroup.getPaddingLeft() + getExtraLeftPadding();
        int top = mViewGroup.getPaddingTop() + getExtraTopPadding();

        int childCount = mViewGroupHelper.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View childView = mViewGroupHelper.getChildView(i);

            childView.layout(
                    left,
                    top,
                    left + childView.getMeasuredWidth(),
                    top + childView.getMeasuredHeight());

            left += childView.getMeasuredWidth() + mHorizonInsetPadding;

            if ((i + 1) % mRowNumber == 0) {
                // end of the line
                left = mViewGroup.getPaddingStart() + getExtraLeftPadding();
                top += childView.getMeasuredHeight() + mVerticalInsetPadding;
            }
        }
    }

    @Override
    public void setViewGroupHelper(ViewGroupHelper viewGroupHelper) {
        mViewGroupHelper = viewGroupHelper;
    }

    protected int getExtraLeftPadding() {
        return 0;
    }

    protected int getExtraTopPadding() {
        return 0;
    }

    protected void setSetMeasureResult(
            int childViewWidth,
            int childViewHeight,
            int horizonInsetPadding,
            int verticalInsetPadding) {

        mHorizonInsetPadding = horizonInsetPadding;

        mVerticalInsetPadding = verticalInsetPadding;

        mChildViewWidth = childViewWidth;

        mChildViewHeight = childViewHeight;
    }

    protected ViewGroup getViewGroup() {
        return mViewGroup;
    }

    protected LayoutHelper.ViewGroupHelper getViewGroupHelper() {
        return mViewGroupHelper;
    }

    protected int getRowNumber() {
        return mRowNumber;
    }

    /**
     * @param width width exclude ViewGroup padding
     */
    protected abstract void onMeasure(int width);
}
