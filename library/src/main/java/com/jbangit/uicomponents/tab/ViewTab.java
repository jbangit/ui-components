package com.jbangit.uicomponents.tab;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;

import com.jbangit.uicomponents.R;
import com.jbangit.uicomponents.common.DensityUtils;
import com.jbangit.uicomponents.common.Globals;
import com.jbangit.uicomponents.common.viewgroup.layouthelper.LayoutHelper;
import com.jbangit.uicomponents.common.viewgroup.layouthelper.emptyview.EmptyViewLayoutHelper;
import com.jbangit.uicomponents.common.viewgroup.layouthelper.singleRow.SingleRowLayoutHelper;
import com.jbangit.uicomponents.common.viewgroup.layouthelper.singleline.SingleLineLayoutHelper;
import com.jbangit.uicomponents.common.viewgroup.scrollhelper.ScrollHelper;
import com.jbangit.uicomponents.common.viewgroup.scrollhelper.scrollerhelper.HorizonScrollHelper;
import com.jbangit.uicomponents.common.viewgroup.scrollhelper.scrollerhelper.VerticalScrollerHelper;

import java.util.ArrayList;
import java.util.List;

public class ViewTab extends ViewGroup implements ValueAnimator.AnimatorUpdateListener {

    public static final int ORIENTATION_HORIZON = 0;

    public static final int ORIENTATION_VERTICAL = 1;

    public static final int INDICATOR_GRAVITY_TOP = 0;

    public static final int INDICATOR_GRAVITY_BOTTOM = 1;

    public static final int LAYOUT_STYLE_FILL = 0;

    public static final int LAYOUT_STYLE_WRAP = 1;

    private ViewTabAdapter mAdapter;

    private List<View> mViewItems = new ArrayList<>();

    private int mCurrentItem = -1;

    private int mAttrOrientation = ORIENTATION_HORIZON;

    private int mAttrIdcHPadding = DensityUtils.getPxFromDp(getContext(), 0);

    private int mAttrIdcVPadding = DensityUtils.getPxFromDp(getContext(), 0);

    private int mAttrIdcColor = Globals.getPrimaryColor(getContext());

    private int mAttrIdcSize = DensityUtils.getPxFromDp(getContext(), 3);

    private float mAttrIdcScale = 1f;

    private int mAttrIdcGravity = INDICATOR_GRAVITY_BOTTOM;

    private int mAttrLayoutStyle = LAYOUT_STYLE_FILL;

    private OnTabChangeListener mOnTabChangeListener = null;

    private LayoutHelper mLayoutHelper;

    private EmptyViewLayoutHelper mEmptyLayoutHelper;

    private ScrollHelper mScrollHelper;

    private View mHolderView;

    private View mEmptyView;

    public ViewTab(Context context) {
        super(context);
    }

    public ViewTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        defaultInit();
        initAttrs(context, attrs);
        initLayoutHelper();
        initScrollHelper();
    }

    public ViewTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        defaultInit();
        initAttrs(context, attrs);
        initLayoutHelper();
        initScrollHelper();
    }

    private void defaultInit() {
        setWillNotDraw(false);
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
        mAttrLayoutStyle =
                typedArray.getInt(R.styleable.ViewTab_viewTabLayoutStyle, mAttrLayoutStyle);
        mPaint.setColor(mAttrIdcColor);
        mPaint.setStrokeWidth(mAttrIdcSize);

        typedArray.recycle();
    }

    private void initLayoutHelper() {
        switch (mAttrOrientation) {
            case ORIENTATION_HORIZON:
                SingleLineLayoutHelper singleLineLayoutHelper = new SingleLineLayoutHelper();
                singleLineLayoutHelper.setOnGetHeightListener(
                        new SingleLineLayoutHelper.OnGetViewHeight() {
                            @Override
                            public int onGetHeight() {
                                return getTabHeight();
                            }
                        });
                singleLineLayoutHelper.setStyle(mAttrLayoutStyle);
                mLayoutHelper = singleLineLayoutHelper;
                break;
            case ORIENTATION_VERTICAL:
                SingleRowLayoutHelper singleRowLayoutHelper = new SingleRowLayoutHelper();
                singleRowLayoutHelper.setOnGetWidthListener(
                        new SingleRowLayoutHelper.OnGetWidthListener() {
                            @Override
                            public int onGetWidth() {
                                return getTabWidth();
                            }
                        });
                mLayoutHelper = singleRowLayoutHelper;
                singleRowLayoutHelper.setStyle(mAttrLayoutStyle);
                break;
        }

        mEmptyLayoutHelper = new EmptyViewLayoutHelper();
        mEmptyLayoutHelper.setOnGetWidthListener(
                new EmptyViewLayoutHelper.OnGetWidthListener() {
                    @Override
                    public int onGetWidth() {
                        if (mHolderView == null) {
                            return 0;
                        }
                        return mHolderView.getMeasuredWidth();
                    }
                });
        mEmptyLayoutHelper.setOnGetHeightListener(
                new EmptyViewLayoutHelper.OnGetHeightListener() {
                    @Override
                    public int onGetHeight() {
                        if (mHolderView == null) {
                            return 0;
                        }
                        return mHolderView.getMeasuredHeight();
                    }
                });

        LayoutHelper.ViewGroupHelper viewGroupHelper =
                new LayoutHelper.ViewGroupHelper() {
                    @Override
                    public View getChildView(int index) {
                        return getChildAt(index);
                    }

                    @Override
                    public int getChildCount() {
                        return ViewTab.this.getChildCount();
                    }

                    @Override
                    public void onSetMeasuredDimension(int measuredWidth, int measuredHeight) {
                        setMeasuredDimension(measuredWidth, measuredHeight);
                    }

                    @NonNull
                    @Override
                    public ViewGroup getViewGroup() {
                        return ViewTab.this;
                    }
                };

        mEmptyLayoutHelper.setViewGroupHelper(viewGroupHelper);
        mLayoutHelper.setViewGroupHelper(viewGroupHelper);
    }

    private void initScrollHelper() {

        switch (mAttrOrientation) {
            case ORIENTATION_HORIZON:
                HorizonScrollHelper horizonScrollHelper = new HorizonScrollHelper(getContext());
                mScrollHelper = horizonScrollHelper;
                horizonScrollHelper.setOnGetContentWidthListener(
                        new HorizonScrollHelper.OnGetContentWidthListener() {
                            @Override
                            public int onGetContentWidth() {
                                if (mViewItems.size() == 0) {
                                    return 0;
                                } else {
                                    return getChildAt(getChildCount() - 1).getRight();
                                }
                            }
                        });
                break;
            case ORIENTATION_VERTICAL:
                VerticalScrollerHelper verticalScrollHelper =
                        new VerticalScrollerHelper(getContext());
                verticalScrollHelper.setOnGetContentHeightListener(
                        new VerticalScrollerHelper.OnGetContentHeightListener() {
                            @Override
                            public int onGetContentHeight() {
                                if (mViewItems.size() == 0) {
                                    return 0;
                                } else {
                                    return getChildAt(getChildCount() - 1).getBottom();
                                }
                            }
                        });
                mScrollHelper = verticalScrollHelper;
                break;
        }

        mScrollHelper.setViewGroupHelper(
                new ScrollHelper.ViewGroupHelper() {
                    @Override
                    public ViewGroup getViewGroup() {
                        return ViewTab.this;
                    }
                });
    }

    @Override
    public void computeScroll() {
        mScrollHelper.onViewGroupComputeScroll();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mViewItems.size() == 0) {
            mEmptyLayoutHelper.onViewGroupMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            mLayoutHelper.onViewGroupMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mScrollHelper.onDetachedFromWindow();
    }

    private int getTabHeight() {
        if (getChildCount() == 0) {
            return 0;
        }
        View view = getChildAt(0);
        view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

        return view.getMeasuredHeight();
    }

    public int getTabWidth() {
        if (getChildCount() == 0) {
            return 0;
        }
        View view = getChildAt(0);
        view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

        return view.getMeasuredWidth();
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;

        if (mViewItems.size() == 0) {
            removeAllViewsInLayout();
            addViewInLayout(mEmptyView, -1, mEmptyView.getLayoutParams(), true);
            requestLayout();
        }
    }

    public View getHolderView() {
        return mHolderView;
    }

    public void setHolderView(View holderView) {
        mHolderView = holderView;
        mHolderView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
    }

    public void setCurrentItem(int position) {
        setCurrentItem(position, true);
    }

    public void setCurrentItem(int position, boolean animated) {
        mCurrentItem = position;
        changeTab(mCurrentItem, animated);
    }

    private int mSelectedPosition = -1;

    public void changeTab(int position, boolean animated) {
        if (!canChangeTab(position)) {
            return;
        }

        // uncheck last tab
        if (mSelectedPosition != -1) {
            mAdapter.onSelected(mViewItems.get(mSelectedPosition), mSelectedPosition, false);
        }

        // check new tab
        if (position != -1) {
            mAdapter.onSelected(mViewItems.get(position), position, true);
        }

        mSelectedPosition = position;
        moveIndicatorTo(position, animated);
    }

    private boolean canChangeTab(int position) {
        if (position >= mViewItems.size() & position != -1) {
            return false;
        }

        if (position == mSelectedPosition) {
            return false;
        }

        View newItem = position == -1 ? null : mViewItems.get(position);
        View oldItem = mSelectedPosition == -1 ? null : mViewItems.get(mSelectedPosition);

        boolean canChangeTab;
        if (mOnTabChangeListener == null) {
            canChangeTab = true;
        } else {
            canChangeTab =
                    mOnTabChangeListener.onTabChange(oldItem, mCurrentItem, newItem, position);
        }

        return canChangeTab;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mViewItems.size() == 0) {
            mEmptyLayoutHelper.onViewGroupLayout(changed, l, t, r, b);
        } else {
            mLayoutHelper.onViewGroupLayout(changed, l, t, r, b);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mScrollHelper.onViewGroupInterceptTouchEvent(event);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mScrollHelper.onViewGroupTouchEvent(event);
    }

    public void setAdapter(@NonNull ViewTabAdapter adapter) {
        mAdapter = adapter;

        removeAllViewsInLayout();
        mViewItems.clear();

        int viewCount = mAdapter.getCount();
        for (int i = 0; i < viewCount; i++) {
            View view = mAdapter.getItemView(this, i);
            addViewInLayout(view, -1, view.getLayoutParams());

            mViewItems.add(view);
            initViewItem(view, i);
        }

        if (mViewItems.size() == 0 && mEmptyView != null) {
            addViewInLayout(mEmptyView, -1, mEmptyView.getLayoutParams(), true);
        }

        requestLayout();

        mSelectedPosition = -1;

        setCurrentItem(mCurrentItem, false);
    }

    public ViewTabAdapter getAdapter() {
        return mAdapter;
    }

    private void initViewItem(View tabItem, final int position) {
        tabItem.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onClickTabItem(position);
                    }
                });
    }

    private void onClickTabItem(int position) {
        setCurrentItem(position);
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

    /**
     * @param position when no item selected, pass position by -1
     * @param animated is move with animation
     */
    private void moveIndicatorTo(final int position, final boolean animated) {
        final View item;
        if (position == -1) {
            item = null;
        } else {
            item = mViewItems.get(position);
        }

        if (!isLayoutRequested() && isLaidOut()) {
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

    /**
     * @param item null if no item select
     */
    private void moveIndicator(@Nullable View item) {
        setupIndicatorTargetPosition(item);
        mIndicatorFromX1 = mIndicatorToX1;
        mIndicatorFromX2 = mIndicatorToX2;
        mIndicatorAnimateVal = 1f;
    }

    /**
     * @param item null if no item select
     */
    private void moveIndicatorWithAnimate(@Nullable View item) {
        mIndicatorFromX1 = getCurAnimateX1();
        mIndicatorFromX2 = getCurAnimateX2();
        setupIndicatorTargetPosition(item);

        mIndicatorAnimator.cancel();
        mIndicatorAnimator.setupStartValues();
        mIndicatorAnimator.start();
    }

    /** @param item null if no item select */
    private void setupIndicatorTargetPosition(@Nullable View item) {
        switch (mAttrIdcGravity) {
            case INDICATOR_GRAVITY_TOP:
                mIndicatorY = mAttrIdcVPadding + mAttrIdcSize / 2;
                break;
            case INDICATOR_GRAVITY_BOTTOM:
                mIndicatorY = getHeight() + mAttrIdcVPadding - mAttrIdcSize / 2;
                break;
        }

        if (item == null) {
            mIndicatorToX1 = 0;
            mIndicatorToX2 = 0;
        } else {
            mIndicatorToX1 = item.getLeft();
            mIndicatorToX2 = item.getRight();
        }

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

    public OnTabChangeListener getOnTabChangeListener() {
        return mOnTabChangeListener;
    }

    public void setOnTabChangeListener(OnTabChangeListener onTabChangeListener) {
        mOnTabChangeListener = onTabChangeListener;
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        SaveState saveState = new SaveState(super.onSaveInstanceState());
        saveState.mCurrentItem = mCurrentItem;
        return saveState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SaveState saveState = ((SaveState) state);
        setCurrentItem(saveState.mCurrentItem, false);
        super.onRestoreInstanceState(state);
    }

    private static class SaveState extends BaseSavedState {

        public static final Creator<SaveState> CREATOR =
                new Creator<SaveState>() {
                    public SaveState createFromParcel(Parcel in) {
                        return new SaveState(in);
                    }

                    public SaveState[] newArray(int size) {
                        return new SaveState[size];
                    }
                };

        private int mCurrentItem;

        SaveState(Parcelable superState) {
            super(superState);
        }

        SaveState(Parcel source) {
            super(source);
            mCurrentItem = source.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(mCurrentItem);
        }
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
