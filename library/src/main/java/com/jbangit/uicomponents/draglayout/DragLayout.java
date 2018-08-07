package com.jbangit.uicomponents.draglayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.jbangit.uicomponents.R;

public class DragLayout extends ViewGroup {

    public static final double COLLAPSE_POSITION_P = 0.25;

    private Scroller mScroller;

    private View mContentView;

    private View mLeftView;

    private View mTopView;

    private View mRightView;

    private View mBottomView;

    private boolean mIsCollapsed;

    private ViewConfiguration mViewConfiguration;

    public DragLayout(Context context) {
        super(context);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        defaultInit();
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        defaultInit();
    }

    private void defaultInit() {
        mScroller = new Scroller(getContext());
        mViewConfiguration = ViewConfiguration.get(getContext());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            LayoutParams lp = (LayoutParams) childView.getLayoutParams();
            switch (lp.position) {
                case LayoutParams.POSITION_CONTENT:
                    mContentView = childView;
                    break;
                case LayoutParams.POSITION_LEFT:
                    mLeftView = childView;
                    break;
                case LayoutParams.POSITION_TOP:
                    mTopView = childView;
                    break;
                case LayoutParams.POSITION_RIGHT:
                    mRightView = childView;
                    break;
                case LayoutParams.POSITION_BOTTOM:
                    mBottomView = childView;
                    break;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        View content = getChildAt(0);
        if (content == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        View rightView = mRightView;
        if (rightView == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        content.measure(widthMeasureSpec, heightMeasureSpec);
        rightView.measure(
                MeasureSpec.makeMeasureSpec(
                        MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(content.getMeasuredHeight(), MeasureSpec.EXACTLY));
        setMeasuredDimension(content.getMeasuredWidth(), content.getMeasuredHeight());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mContentView.layout(
                0, 0, mContentView.getMeasuredWidth(), mContentView.getMeasuredHeight());
        mRightView.layout(
                getMeasuredWidth(),
                0,
                getMeasuredWidth() + mRightView.getMeasuredWidth(),
                mRightView.getMeasuredHeight());
    }

    private float mLastX = 0;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = event.getX() - mLastX;
                if (Math.abs(dx) > mViewConfiguration.getScaledTouchSlop()) {
                    mLastX = event.getX();
                    return true;
                }
                mLastX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return false;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int rightViewZone = mRightView.getRight() - getWidth();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = event.getX();
                break;

            case MotionEvent.ACTION_MOVE:
                int dx = (int) (event.getX() - mLastX);

                if (getScrollX() - dx <= 0) {
                    dx = getScrollX();
                } else if (getScrollX() - dx >= rightViewZone) {
                    dx = getScrollX() - rightViewZone;
                }

                scrollBy(-dx, 0);

                mLastX = event.getX();
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                int collapsePosition;

                if (mIsCollapsed) {
                    collapsePosition = (int) (rightViewZone * (1 - COLLAPSE_POSITION_P));
                } else {
                    collapsePosition = (int) (rightViewZone * COLLAPSE_POSITION_P);
                }

                if (getScrollX() > collapsePosition) {
                    mIsCollapsed = true;
                    mScroller.startScroll(getScrollX(), 0, rightViewZone - getScrollX(), 0, 300);
                } else {
                    mIsCollapsed = false;
                    mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0, 300);
                }

                invalidate();
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {

        static final int POSITION_CONTENT = 0;

        static final int POSITION_LEFT = 1;

        static final int POSITION_TOP = 2;

        static final int POSITION_RIGHT = 3;

        static final int POSITION_BOTTOM = 4;

        int position = POSITION_CONTENT;

        LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray typedArray = c.obtainStyledAttributes(attrs, R.styleable.DragLayout_Layout);
            position =
                    typedArray.getInt(
                            R.styleable.DragLayout_Layout_layout_dragLayoutPosition, position);
            typedArray.recycle();
        }

        LayoutParams(int width, int height) {
            super(width, height);
        }

        LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}
