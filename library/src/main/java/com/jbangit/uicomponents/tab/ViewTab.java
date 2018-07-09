package com.jbangit.uicomponents.tab;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import com.jbangit.uicomponents.R;
import com.jbangit.uicomponents.common.DensityUtils;
import com.jbangit.uicomponents.common.Globals;

import java.util.ArrayList;
import java.util.List;

public class ViewTab extends ViewGroup implements ValueAnimator.AnimatorUpdateListener {

    public static final int ORIENTATION_HORIZON = 0;

    public static final int ORIENTATION_VERTICAL = 1;

    public static final int INDICATOR_GRAVITY_TOP = 0;

    public static final int INDICATOR_GRAVITY_BOTTOM = 1;

    private ViewTabAdapter mAdapter;

    private List<View> mItems = new ArrayList<>();

    private int mOldItemPosition = -1;

    private int mAttrOrientation = ORIENTATION_HORIZON;

    private int mAttrIdcHPadding = DensityUtils.getPxFromDp(getContext(), 0);

    private int mAttrIdcVPadding = DensityUtils.getPxFromDp(getContext(), 0);

    private int mAttrIdcColor = Globals.getPrimaryColor(getContext());

    private int mAttrIdcSize = DensityUtils.getPxFromDp(getContext(), 3);

    private float mAttrIdcScale = 1f;

    private int mAttrIdcGravity = INDICATOR_GRAVITY_BOTTOM;

    private OnTabChangeListener mOnTabChangeListener = null;

    public ViewTab(Context context) {
        super(context);
    }

    public ViewTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        defaultInit();
        initAttrs(context, attrs);
    }

    public ViewTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        defaultInit();
        initAttrs(context, attrs);
    }

    private void defaultInit() {
        mScroller = new Scroller(getContext());
        setWillNotDraw(false);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
        invalidate();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ViewTab);
        mAttrOrientation =
                typedArray.getInt(R.styleable.ViewTab_viewTabOrientation, mAttrOrientation);
        mAttrIdcColor = typedArray.getColor(R.styleable.ViewTab_viewTabIdcColor, mAttrIdcColor);
        mAttrIdcSize =
                typedArray.getDimensionPixelSize(R.styleable.ViewTab_viewTabIdcSize, mAttrIdcSize);
        mAttrIdcGravity = typedArray.getInt(R.styleable.ViewTab_viewTabIdcGravity, mAttrIdcGravity);
        mAttrIdcVPadding =
                typedArray.getDimensionPixelSize(
                        R.styleable.ViewTab_viewTabIdcVPadding, mAttrIdcVPadding);
        mAttrIdcHPadding =
                typedArray.getDimensionPixelSize(
                        R.styleable.ViewTab_viewTabIdcHPadding, mAttrIdcHPadding);
        mAttrIdcScale =
                typedArray.getFraction(R.styleable.ViewTab_viewTabIdcScale, 1, 1, mAttrIdcScale);
        mPaint.setColor(mAttrIdcColor);
        mPaint.setStrokeWidth(mAttrIdcSize);

        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        switch (mAttrOrientation) {
            case ORIENTATION_HORIZON:
                onHorizonMeasure(widthMeasureSpec, heightMeasureSpec);
                break;
            case ORIENTATION_VERTICAL:
                onVerticalMeasure(widthMeasureSpec, heightMeasureSpec);
                break;
        }
    }

    private void onHorizonMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int childCount = getChildCount();

        int tabWidth = 0;

        int width = widthSize;
        tabWidth = widthSize / childCount;
        switch (widthMode) {
            case MeasureSpec.AT_MOST:
                break;
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int tabHeight = getTabHeight();

        int height = heightSize;
        switch (heightMode) {
            case MeasureSpec.AT_MOST:
                height = Math.min(tabHeight, height);
                break;
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }

        measureTabs(tabWidth, tabHeight);

        setMeasuredDimension(width, height);
    }

    private int getTabHeight() {
        if (getChildCount() == 0) {
            return 10;
        }
        View view = getChildAt(0);
        view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

        return view.getMeasuredHeight();
    }

    private void measureTabs(int tabWidth, int tabHeight) {
        int tabCount = getChildCount();

        for (int i = 0; i < tabCount; i++) {
            getChildAt(i)
                    .measure(
                            MeasureSpec.makeMeasureSpec(tabWidth, MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(tabHeight, MeasureSpec.EXACTLY));
        }
    }

    private void onVerticalMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int width = widthSize;

        switch (widthMode) {
            case MeasureSpec.AT_MOST:
                width = 0;
                break;
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.UNSPECIFIED:
                width = widthSize;
                break;
        }

        measureTabs(width, MeasureSpec.EXACTLY, 0, MeasureSpec.UNSPECIFIED);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int height = heightSize;

        switch (height) {
            case MeasureSpec.AT_MOST:
                break;
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }

        setMeasuredDimension(width, height);
    }

    private void measureTabs(int width, int widthMode, int height, int heightMode) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            getChildAt(i)
                    .measure(
                            MeasureSpec.makeMeasureSpec(width, widthMode),
                            MeasureSpec.makeMeasureSpec(height, heightMode));
        }
    }

    public void setCurrentItem(int position) {
        setCurrentItem(position, true);
    }

    public void setCurrentItem(int position, boolean animated) {
        if (mOldItemPosition != -1 && mOldItemPosition < mItems.size()) {
            mAdapter.onSelected(mItems.get(mOldItemPosition), mOldItemPosition, false);
        }
        mAdapter.onSelected(mItems.get(position), position, true);

        mOldItemPosition = position;
        moveIndicatorTo(position, animated);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        switch (mAttrOrientation) {
            case ORIENTATION_HORIZON:
                layoutHorizon();
                break;
            case ORIENTATION_VERTICAL:
                layoutVertical();
                break;
        }
    }

    private void layoutHorizon() {
        int childCount = getChildCount();

        int layoutX = 0;
        int layoutY = 0;
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            view.layout(
                    layoutX,
                    layoutY,
                    layoutX + view.getMeasuredWidth(),
                    layoutY + view.getMeasuredHeight());
            layoutX += view.getMeasuredWidth();
        }
    }

    private void layoutVertical() {
        int childCount = getChildCount();

        int left = 0;
        int top = 0;
        for (int i = 0; i < childCount; i++) {
            View tab = getChildAt(i);
            tab.layout(left, top, left + tab.getMeasuredWidth(), top + tab.getMeasuredHeight());
            top += tab.getMeasuredHeight();
        }
    }

    public void setAdapter(@NonNull ViewTabAdapter adapter) {
        mAdapter = adapter;

        removeAllViewsInLayout();
        mItems.clear();

        int viewCount = mAdapter.getCount();
        for (int i = 0; i < viewCount; i++) {
            View view = mAdapter.getItemView(this, i);
            addViewInLayout(view, -1, view.getLayoutParams());

            initTabItem(view, i);
        }
        requestLayout();
    }

    public ViewTabAdapter getAdapter() {
        return mAdapter;
    }

    private void initTabItem(View tabItem, final int position) {
        mItems.add(tabItem);
        tabItem.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onClickTabItem(position);
                    }
                });
    }

    private void onClickTabItem(int position) {
        View newItem = mItems.get(position);
        View oldItem = mOldItemPosition == -1 ? null : mItems.get(mOldItemPosition);

        boolean isTabChanged;
        if (mOnTabChangeListener == null) {
            isTabChanged = true;
        } else {
            isTabChanged =
                    mOnTabChangeListener.onTabChange(newItem, position, oldItem, mOldItemPosition);
        }

        if (isTabChanged) {
            if (oldItem != null) {
                mAdapter.onSelected(oldItem, mOldItemPosition, false);
            }
            mAdapter.onSelected(newItem, position, true);
            mOldItemPosition = position;
            moveIndicatorTo(position, true);
        }
    }

    private final ValueAnimator mIndicatorAnimator = ValueAnimator.ofFloat(0, 1f);

    {
        mIndicatorAnimator.addUpdateListener(this);
        mIndicatorAnimator.setInterpolator(new DecelerateInterpolator());
        mIndicatorAnimator.setDuration(300);
    }

    private float mIndicatorFromX1;

    private float mIndicatorFromX2;

    private float mIndicatorToX1;

    private float mIndicatorToX2;

    private float mIndicatorY;

    private float mIndicatorAnimateVal;

    private Paint mPaint = new Paint();

    {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    private void moveIndicatorTo(final int position, final boolean animated) {
        final View item = mItems.get(position);

        if (isLaidOut()) {
            if (animated) {
                moveIndicatorWithAnimate(item);
            } else {
                moveIndicator(item);
            }
        } else {
            getViewTreeObserver()
                    .addOnGlobalLayoutListener(
                            new ViewTreeObserver.OnGlobalLayoutListener() {
                                @Override
                                public void onGlobalLayout() {
                                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                    if (animated) {
                                        moveIndicatorWithAnimate(item);
                                    } else {
                                        moveIndicator(item);
                                    }
                                }
                            });
        }
    }

    private void moveIndicator(View item) {
        setupIndicatorTargetPosition(item);
        mIndicatorFromX1 = mIndicatorToX1;
        mIndicatorFromX2 = mIndicatorToX2;
        mIndicatorAnimateVal = 1f;
        getViewTreeObserver()
                .addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                        });
    }

    private void moveIndicatorWithAnimate(View item) {
        mIndicatorFromX1 = getCurAnimateX1();
        mIndicatorFromX2 = getCurAnimateX2();
        setupIndicatorTargetPosition(item);

        mIndicatorAnimator.cancel();
        mIndicatorAnimator.setupStartValues();
        mIndicatorAnimator.start();
    }

    private void setupIndicatorTargetPosition(View item) {
        switch (mAttrIdcGravity) {
            case INDICATOR_GRAVITY_TOP:
                mIndicatorY = item.getTop() + mAttrIdcVPadding + mAttrIdcSize / 2;
                break;
            case INDICATOR_GRAVITY_BOTTOM:
                mIndicatorY = item.getBottom() + mAttrIdcVPadding - mAttrIdcSize / 2;
                break;
        }
        mIndicatorToX1 = item.getLeft();
        mIndicatorToX2 = item.getRight();

        float length = mIndicatorToX2 - mIndicatorToX1;
        float scaledLength = length * (1 - mAttrIdcScale) / 2;
        mIndicatorToX1 += scaledLength;
        mIndicatorToX2 -= scaledLength;

        mIndicatorToX1 += 2 * mAttrIdcHPadding;
        mIndicatorToX2 -= 2 * mAttrIdcHPadding;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void onDrawForeground(Canvas canvas) {
        super.onDrawForeground(canvas);
        drawIndicator(canvas);
    }

    private void drawIndicator(Canvas canvas) {
        canvas.drawLine(getCurAnimateX1(), mIndicatorY, getCurAnimateX2(), mIndicatorY, mPaint);
    }

    private float getCurAnimateX2() {
        return mIndicatorFromX2 + (mIndicatorToX2 - mIndicatorFromX2) * mIndicatorAnimateVal;
    }

    private float getCurAnimateX1() {
        return mIndicatorFromX1 + (mIndicatorToX1 - mIndicatorFromX1) * mIndicatorAnimateVal;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        mIndicatorAnimateVal = (float) animation.getAnimatedValue();
        invalidate();
    }

    private float mLastY;

    private Scroller mScroller;

    private VelocityTracker mVelocityTracker = null;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!canScroll()) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mScroller.forceFinished(true);
                mLastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(event.getY() - mLastY) > 20) {
                    mLastY = event.getY();
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return false;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!canScroll()) {
            return false;
        }

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        int bottomEdge = getChildAt(getChildCount() - 1).getBottom() - getHeight();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:

                int dy = (int) (event.getY() - mLastY);

                if (getScrollY() - dy > bottomEdge) {
                    dy = getScrollY() - bottomEdge;
                } else if (getScrollY() - dy < 0) {
                    dy = getScrollY();
                }

                scrollBy(0, -dy);

                mLastY = event.getY();
                return true;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000);
                float yVelocity = -mVelocityTracker.getYVelocity();

                mScroller.fling(
                        getScrollX(), getScrollY(), 0, (int) yVelocity, 0, 0, 0, bottomEdge);
                postInvalidate();

                mVelocityTracker.recycle();
                mVelocityTracker = null;
                return false;
            case MotionEvent.ACTION_CANCEL:
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                return false;
        }

        return false;
    }

    private boolean canScroll() {
        return mAttrOrientation == ORIENTATION_VERTICAL && (getChildAt(getChildCount() - 1).getBottom() > getHeight());
    }

    public OnTabChangeListener getOnTabChangeListener() {
        return mOnTabChangeListener;
    }

    public void setOnTabChangeListener(OnTabChangeListener onTabChangeListener) {
        mOnTabChangeListener = onTabChangeListener;
    }

    public interface OnTabChangeListener {

        boolean onTabChange(View oldItem, int oldPosition, View newItem, int newPosition);
    }

    public interface ViewTabAdapter {

        View getItemView(@NonNull ViewGroup container, int position);

        void onSelected(@NonNull View item, int position, boolean selected);

        int getCount();
    }
}
