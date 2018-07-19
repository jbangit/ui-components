package com.jbangit.uicomponents.common.viewgroup.scrollhelper.scrollerhelper;

import android.content.Context;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewGroup;
import android.widget.Scroller;

public class VerticalScrollerHelper extends BaseScrollerHelper {

    private OnGetContentHeightListener mOnGetContentHeightListener;

    public VerticalScrollerHelper(Context context) {
        super(context);
    }

    @Override
    protected float onGetTouchPosition(MotionEvent event) {
        return event.getY();
    }

    @Override
    protected int onGetScrollPosition(ViewGroup viewGroup) {
        return viewGroup.getScrollY();
    }

    @Override
    protected float onGetVelocity(VelocityTracker velocityTracker) {
        return velocityTracker.getYVelocity();
    }

    @Override
    protected void onScroll(ViewGroup viewGroup, int distance) {
        viewGroup.scrollBy(0, distance);
    }

    @Override
    protected void onFling(Scroller scroller, int scrollPosition, int scrollRange, int velocity) {
        scroller.fling(
                getViewGroupHelper().getViewGroup().getScrollX(),
                scrollPosition,
                0,
                velocity,
                0,
                0,
                0,
                scrollRange);
    }

    @Override
    protected int onGetScrollRange() {
        return mOnGetContentHeightListener.onGetContentHeight() - getViewGroupHelper().getViewGroup().getHeight();
    }

    public void setOnGetContentHeightListener(
            OnGetContentHeightListener onGetContentHeightListener) {
        mOnGetContentHeightListener = onGetContentHeightListener;
    }

    public interface OnGetContentHeightListener {

        int onGetContentHeight();
    }
}
