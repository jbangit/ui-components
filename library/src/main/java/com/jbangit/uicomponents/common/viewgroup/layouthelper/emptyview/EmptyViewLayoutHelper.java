package com.jbangit.uicomponents.common.viewgroup.layouthelper.emptyview;

import android.view.View;
import android.view.ViewGroup;

import com.jbangit.uicomponents.common.viewgroup.layouthelper.LayoutHelper;

import static android.view.View.MeasureSpec;

public class EmptyViewLayoutHelper implements LayoutHelper {

    private ViewGroupHelper mViewGroupHelper;

    private OnGetHeightListener mOnGetHeightListener;

    private OnGetWidthListener mOnGetWidthListener;

    @Override
    public void onViewGroupMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int expectedWidth = 0;
        switch (widthMode) {
            case MeasureSpec.AT_MOST:
                expectedWidth = mOnGetWidthListener.onGetWidth();
                break;
            case MeasureSpec.EXACTLY:
                expectedWidth = widthSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                expectedWidth = mOnGetWidthListener.onGetWidth();
                break;
        }

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int expectedHeight = 0;
        switch (heightMode) {
            case MeasureSpec.AT_MOST:
                expectedHeight = mOnGetHeightListener.onGetHeight();
                break;
            case MeasureSpec.EXACTLY:
                expectedHeight = heightSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                expectedHeight = mOnGetHeightListener.onGetHeight();
                break;
        }

        View emptyView = mViewGroupHelper.getChildView(0);
        if (emptyView != null) {
            ViewGroup viewGroup = mViewGroupHelper.getViewGroup();
            emptyView.measure(
                    MeasureSpec.makeMeasureSpec(
                            expectedWidth
                                    - viewGroup.getPaddingLeft()
                                    - viewGroup.getPaddingRight(),
                            MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(
                            expectedHeight
                                    - viewGroup.getPaddingTop()
                                    - viewGroup.getPaddingBottom(),
                            MeasureSpec.EXACTLY));
        }

        mViewGroupHelper.onSetMeasuredDimension(expectedWidth, expectedHeight);
    }

    @Override
    public void onViewGroupLayout(boolean changed, int l, int t, int r, int b) {
        View emptyView = mViewGroupHelper.getChildView(0);
        ViewGroup viewGroup = mViewGroupHelper.getViewGroup();

        if (emptyView != null) {
            emptyView.layout(
                    viewGroup.getPaddingLeft(),
                    viewGroup.getPaddingTop(),
                    viewGroup.getPaddingStart() + emptyView.getMeasuredWidth(),
                    viewGroup.getPaddingTop() + emptyView.getMeasuredHeight());
        }
    }

    @Override
    public void setViewGroupHelper(ViewGroupHelper viewGroupHelper) {
        mViewGroupHelper = viewGroupHelper;
    }

    public OnGetHeightListener getOnGetHeightListener() {
        return mOnGetHeightListener;
    }

    public void setOnGetHeightListener(OnGetHeightListener onGetHeightListener) {
        mOnGetHeightListener = onGetHeightListener;
    }

    public OnGetWidthListener getOnGetWidthListener() {
        return mOnGetWidthListener;
    }

    public void setOnGetWidthListener(OnGetWidthListener onGetWidthListener) {
        mOnGetWidthListener = onGetWidthListener;
    }

    public interface OnGetWidthListener {

        int onGetWidth();
    }

    public interface OnGetHeightListener {

        int onGetHeight();
    }
}
