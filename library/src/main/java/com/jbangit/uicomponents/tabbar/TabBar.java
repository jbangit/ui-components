package com.jbangit.uicomponents.tabbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jbangit.uicomponents.R;
import com.jbangit.uicomponents.common.DensityUtils;

import java.util.ArrayList;

/**
 * <p>Supports ViewPager
 * <p>
 * <P>{@link TabBar#select(int)}
 * <p>
 * <P>{@link TabBar#select(TabBarItem)}
 * <p>
 * <P>{@link TabBar.OnTabChangeListener}
 */
public class TabBar extends ViewGroup {

    private ArrayList<TabBarItem> mItems;

    private OnTabChangeListener mOnTabChangeListener = null;

    private int mTotalChildWidth;

    private ViewPager mViewPager;

    private int mSelectedTabPosition;

    private int mAttrItemTitleSize;

    private int mAttrItemIconSize;

    public TabBar(Context context) {
        this(context, null, 0);
    }

    public TabBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        defaultInit(context);
        initAttrs(context, attrs);
    }

    private void defaultInit(Context context) {
        mAttrItemTitleSize = DensityUtils.getPxFromSp(context, 9);
        mAttrItemIconSize = LayoutParams.WRAP_CONTENT;

        setWillNotDraw(false);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TabBar);

        mAttrItemTitleSize = typedArray.getDimensionPixelSize(R.styleable.TabBar_tabBarItemTitleTextSize, mAttrItemTitleSize);
        mAttrItemIconSize = typedArray.getDimensionPixelSize(R.styleable.TabBar_tabBarItemIconSize, mAttrItemIconSize);

        typedArray.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();

        final float itemInsetMultiple = 2.5f;

        int statEndInset = (int) ((getWidth() - mTotalChildWidth) / ((childCount - 1) * itemInsetMultiple + 2));
        int itemInset = (int) (itemInsetMultiple * statEndInset);

        int left, top, bottom;
        left = statEndInset;
        top = 0;
        bottom = getHeight();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            int childViewWidth = childView.getMeasuredWidth();
            childView.layout(left, top, left + childViewWidth, bottom);
            left += itemInset + childViewWidth;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int backgroundColor = getResources().getColor(R.color.colorForeground);
        canvas.drawARGB(
                Color.alpha(backgroundColor),
                Color.red(backgroundColor),
                Color.green(backgroundColor),
                Color.blue(backgroundColor));
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        SaveState saveState = new SaveState(super.onSaveInstanceState());
        saveState.mSelectedTabPosition = mSelectedTabPosition;
        return saveState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SaveState saveState = (SaveState) state;
        mSelectedTabPosition = saveState.mSelectedTabPosition;
        initTab();
        super.onRestoreInstanceState(saveState.getSuperState());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        getTabItem();
        initViews();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int maxChildHeight = 0;
        mTotalChildWidth = 0;

        int measureSpecW =
                MeasureSpec.makeMeasureSpec(
                        MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.AT_MOST);
        int measureSpecH =
                MeasureSpec.makeMeasureSpec(
                        MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.AT_MOST);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            childView.measure(measureSpecW, measureSpecH);

            maxChildHeight = Math.max(childView.getMeasuredHeight(), maxChildHeight);
            mTotalChildWidth += childView.getMeasuredWidth();
        }

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = widthSize;
        switch (widthMode) {
            case MeasureSpec.AT_MOST:
                width = mTotalChildWidth;
                break;
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }

        int height = heightSize;
        switch (heightMode) {
            case MeasureSpec.AT_MOST:
                height = Math.min(maxChildHeight, heightSize);
                break;
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }

        setMeasuredDimension(width, height);
    }

    public void deleteTab(View tabView) {
        removeView(tabView);
        getTabItem();
        initViews();
    }

    private void getTabItem() {
        mItems = new ArrayList<>();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView instanceof TabBarItem) {
                mItems.add((TabBarItem) childView);
            }
        }
    }

    private void initViews() {
        initTab();
    }

    private void initTab() {
        for (int i = 0; i < mItems.size(); i++) {
            TabBarItem tabBarItem = mItems.get(i);
            tabBarItem.setChecked(i == mSelectedTabPosition);
            initTabItem(tabBarItem);
        }

        if (mViewPager != null) {
            if (mSelectedTabPosition != mViewPager.getCurrentItem()) {
                mViewPager.setCurrentItem(mSelectedTabPosition, false);
            }
        }
    }

    private void initTabItem(TabBarItem tabBarItem) {
        ((TextView)tabBarItem.findViewById(R.id.title)).setTextSize(TypedValue.COMPLEX_UNIT_PX, mAttrItemTitleSize);
        LayoutParams layoutParams = tabBarItem.findViewById(R.id.icon).getLayoutParams();
        layoutParams.height = mAttrItemIconSize;
        layoutParams.width = mAttrItemIconSize;
        tabBarItem.setLayoutParams(layoutParams);
    }

    public void select(int position) {
        if (position < mItems.size()) {
            select(mItems.get(position), false);
        } else {
            throw new IllegalArgumentException("position out of range");
        }
    }

    void select(TabBarItem newItem, boolean withUser) {
        if (newItem.isChecked()) {
            return;
        }

        TabBarItem oldItem = null;
        for (TabBarItem item : mItems) {
            if (item.isChecked()) {
                oldItem = item;
            }
        }

        int oldPosition = getPosition(oldItem);
        int newPosition = getPosition(newItem);

        if (withUser && mOnTabChangeListener != null) {
            if (mOnTabChangeListener.onTabChange(oldItem, oldPosition, newItem, newPosition)) {
                mSelectedTabPosition = newPosition;
                changeTab(newItem, oldItem);
            }
        } else {
            mSelectedTabPosition = newPosition;
            changeTab(newItem, oldItem);
        }
    }

    private int getPosition(TabBarItem tabBarItem) {
        for (int i = 0; i < mItems.size(); i++) {
            TabBarItem thatItem = mItems.get(i);
            if (thatItem == tabBarItem) {
                return i;
            }
        }

        return -1;
    }

    private void changeTab(TabBarItem newItem, TabBarItem oldItem) {
        if (oldItem != null) {
            oldItem.setChecked(false);
        }
        newItem.setChecked(true);

        if (mViewPager != null) {
            if (mSelectedTabPosition != mViewPager.getCurrentItem()) {
                mViewPager.setCurrentItem(mSelectedTabPosition, false);
            }
        }
    }

    public void select(TabBarItem newItem) {
        select(newItem, false);
    }

    public OnTabChangeListener getOnTabChangeListener() {
        return mOnTabChangeListener;
    }

    public void setOnTabChangeListener(OnTabChangeListener onTabChangeListener) {
        mOnTabChangeListener = onTabChangeListener;
    }

    public void setupWithViewPager(@NonNull ViewPager viewPager) {
        mViewPager = viewPager;
    }

    public interface OnTabChangeListener {

        /**
         * Callback only when change tab by user
         *
         * @return false to intercept tab change
         */
        boolean onTabChange(
                TabBarItem oldItem, int oldPosition, TabBarItem newItem, int newPosition);
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

        int mSelectedTabPosition;

        SaveState(Parcelable superState) {
            super(superState);
        }

        SaveState(Parcel source) {
            super(source);
            mSelectedTabPosition = source.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(mSelectedTabPosition);
        }
    }
}
