package com.jbangit.uicomponents.common.viewgroup.scrollhelper.scrollerhelper;

import android.content.Context;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.jbangit.uicomponents.common.viewgroup.scrollhelper.ScrollHelper;

abstract class BaseScrollerHelper implements ScrollHelper {

    final private Scroller mScroller;

    final private ViewConfiguration mViewConfiguration;

    private VelocityTracker mVelocityTracker;

    private ViewGroup mViewGroup;

    private ViewGroupHelper mViewGroupHelper;

    private float mLastPosition;

    BaseScrollerHelper(Context context) {
        mViewConfiguration = ViewConfiguration.get(context);
        mScroller = new Scroller(context);
    }

    @Override
    public boolean onViewGroupInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mScroller.forceFinished(true);
                mLastPosition = onGetTouchPosition(event);
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(onGetTouchPosition(event) - mLastPosition) > mViewConfiguration.getScaledTouchSlop()) {
                    mLastPosition = onGetTouchPosition(event);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return false;
    }

    @Override
    public boolean onViewGroupTouchEvent(MotionEvent event) {

        int scrollRange = onGetScrollRange();
        if (scrollRange <= 0) {
            return false;
        }

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        int scrollPosition = onGetScrollPosition(mViewGroup);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mScroller.forceFinished(true);
                mLastPosition = onGetTouchPosition(event);
                return true;
            case MotionEvent.ACTION_MOVE:
                float distance = onGetTouchPosition(event) - mLastPosition;

                if (scrollPosition - distance > scrollRange) {
                    distance = scrollPosition - scrollRange;
                } else if (scrollPosition - distance < 0) {
                    distance = scrollPosition;
                }

                onScroll(mViewGroup, -Math.round(distance));

                mLastPosition = onGetTouchPosition(event);
                return true;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(
                        1000, mViewConfiguration.getScaledMaximumFlingVelocity());
                float velocity = onGetVelocity(mVelocityTracker);

                if (Math.abs(velocity) > mViewConfiguration.getScaledMinimumFlingVelocity()) {
                    onFling(mScroller, scrollPosition, scrollRange, -Math.round(velocity));
                    mViewGroup.postInvalidate();
                }

                recycleVelocityTracker();
                return false;
            case MotionEvent.ACTION_CANCEL:
                recycleVelocityTracker();
                return false;
        }

        return false;
    }


    @Override
    public void onDetachedFromWindow() {
        recycleVelocityTracker();
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    @Override
    public void onViewGroupComputeScroll() {
        if (mScroller.computeScrollOffset()) {
            mViewGroup.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            mViewGroup.invalidate();
        }
    }

    @Override
    public void setViewGroupHelper(ViewGroupHelper viewGroupHelper) {
        mViewGroupHelper = viewGroupHelper;
        mViewGroup = mViewGroupHelper.getViewGroup();
    }

    ViewGroupHelper getViewGroupHelper() {
        return mViewGroupHelper;
    }

    protected abstract float onGetTouchPosition(MotionEvent event);

    protected abstract int onGetScrollPosition(ViewGroup viewGroup);

    protected abstract float onGetVelocity(VelocityTracker velocityTracker);

    protected abstract void onFling(Scroller scroller,
                                    int scrollPosition,
                                    int scrollRange,
                                    int velocity);

    protected abstract void onScroll(ViewGroup viewGroup, int distance);

    protected abstract int onGetScrollRange();
}
