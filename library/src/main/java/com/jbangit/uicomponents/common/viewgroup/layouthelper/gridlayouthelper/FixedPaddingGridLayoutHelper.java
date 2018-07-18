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

    private OnGetChildViewHeight mOnGetChildViewHeight;

    public FixedPaddingGridLayoutHelper(
            ViewGroup viewGroup, LayoutHelper.ViewGroupHelper viewGroupHelper) {
        super(viewGroup, viewGroupHelper);
    }

    @Override
    protected void onMeasure(int width) {
        int totalHorizonInsetPaddingWith = mHorizonInsetPadding * (getRowCount() - 1);
        int childViewWidth = (width - totalHorizonInsetPaddingWith) / getRowCount();

        setSetMeasureResult(
                childViewWidth,
                mOnGetChildViewHeight.onGetChildViewHeight(),
                mHorizonInsetPadding,
                mVerticalInsetPadding);
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

    public interface OnGetChildViewHeight {

        int onGetChildViewHeight();
    }
}
