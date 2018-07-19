package com.jbangit.uicomponents.common.viewgroup.scrollhelper.scrollerhelper;

import android.content.Context;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewGroup;
import android.widget.Scroller;

public class HorizonScrollHelper extends BaseScrollerHelper {

    private OnGetContentWidthListener mOnGetContentWidthListener;

    public HorizonScrollHelper(Context context) {
        super(context);
    }

    @Override
    protected float onGetTouchPosition(MotionEvent event) {
        return event.getX();
    }

    @Override
    protected int onGetScrollPosition(ViewGroup viewGroup) {
        return viewGroup.getScrollX();
    }

    @Override
    protected void onScroll(ViewGroup viewGroup, int distance) {
        viewGroup.scrollBy(distance, 0);
    }

    @Override
    protected void onFling(Scroller scroller, int scrollPosition, int scrollRange, int velocity) {
        scroller.fling(
                scrollPosition,
                getViewGroupHelper().getViewGroup().getScrollY(),
                velocity,
                0,
                0,
                scrollRange,
                0,
                0);
    }

    @Override
    protected float onGetVelocity(VelocityTracker velocityTracker) {
        return velocityTracker.getXVelocity();
    }

    @Override
    protected int onGetScrollRange() {
        return mOnGetContentWidthListener.onGetContentWidth() - getViewGroupHelper().getViewGroup().getWidth();
    }

    public void setOnGetContentWidthListener(OnGetContentWidthListener onGetContentWidthListener) {
        mOnGetContentWidthListener = onGetContentWidthListener;
    }

    public interface OnGetContentWidthListener {

        int onGetContentWidth();
    }
}
