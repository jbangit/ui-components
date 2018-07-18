package com.jbangit.uicomponents.common.viewgroup.layouthelper.gridlayouthelper;

import android.view.ViewGroup;

import com.jbangit.uicomponents.common.viewgroup.layouthelper.LayoutHelper;

/**
 * This layout helper make a grid that has fixed number of row. The item width is depending on the
 * padding including ViewGroup padding and item inset padding.
 */
public class FixedPaddingGridLayoutHelper extends GridLayoutHelper {

    private int mHorizonInsetPadding;

    private int mVerticalInsetPadding;

    private float mHorizonInsetFraction = -1f;

    private float mVerticalInsetFraction = -1f;

    private boolean mIsOuterPadding = false;

    private OnGetChildViewHeight mOnGetChildViewHeight;

    private int mItemHorizonPadding;

    private int mItemHeight;

    private int mItemWidth;

    private int mItemVerticalPadding;

    public FixedPaddingGridLayoutHelper(
            ViewGroup viewGroup, LayoutHelper.ViewGroupHelper viewGroupHelper) {
        super(viewGroup, viewGroupHelper);
    }

    @Override
    protected void onMeasure(int width) {

        if (mHorizonInsetFraction > 0) {
            // width = getRowNumber() * itemWidth + getPaddingCount() * itemPadding;
            // width = getRowNumber() * itemWidth + getPaddingCount() * mHorizonInsetFraction * itemWidth;
            // itemPadding = mHorizonInsetFraction * itemWidth;
            mItemWidth = Math.round(width / (getRowNumber() + getPaddingCount() * mHorizonInsetFraction));
            mItemHorizonPadding = Math.round(mHorizonInsetFraction * mItemWidth);
        } else {
            int totalHorizonInsetPaddingWith = mHorizonInsetPadding * getPaddingCount();
            mItemWidth = (width - totalHorizonInsetPaddingWith) / getRowNumber();
            mItemHorizonPadding = mHorizonInsetPadding;
        }

        mItemHeight = mOnGetChildViewHeight.onGetChildViewHeight();
        if (mVerticalInsetFraction > 0) {
            mItemVerticalPadding = Math.round(mVerticalInsetFraction * mItemHeight);
        } else {
            mItemVerticalPadding = mVerticalInsetPadding;
        }

        setSetMeasureResult(
                mItemWidth,
                mItemHeight,
                mItemHorizonPadding,
                mItemVerticalPadding);
    }

    @Override
    protected int getExtraLeftPadding() {
        return mIsOuterPadding ? mItemHorizonPadding : 0;
    }

    @Override
    protected int getExtraTopPadding() {
        return mIsOuterPadding ? mItemVerticalPadding : 0;
    }

    private int getPaddingCount() {
        if (mIsOuterPadding) {
            return getRowNumber() + 1;
        } else {
            return getRowNumber() - 1;
        }
    }

    public void setHorizonInsetPadding(int horizonInsetPadding) {
        mHorizonInsetPadding = horizonInsetPadding;
    }

    public void setVerticalInsetPadding(int verticalInsetPadding) {
        mVerticalInsetPadding = verticalInsetPadding;
    }

    public void setOnGetChildViewHeight(OnGetChildViewHeight onGetChildViewHeight) {
        mOnGetChildViewHeight = onGetChildViewHeight;
    }

    public void setHorizonInsetFraction(float horizonInsetFraction) {
        mHorizonInsetFraction = horizonInsetFraction;
    }

    public void setVerticalInsetFraction(float verticalInsetFraction) {
        mVerticalInsetFraction = verticalInsetFraction;
    }

    public void setOuterPadding(boolean isOuterPadding) {
        mIsOuterPadding = isOuterPadding;
    }

    public interface OnGetChildViewHeight {

        int onGetChildViewHeight();
    }
}
