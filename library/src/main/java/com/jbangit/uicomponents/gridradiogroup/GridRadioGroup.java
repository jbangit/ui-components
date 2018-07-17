package com.jbangit.uicomponents.gridradiogroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.Dimension;
import android.support.annotation.Nullable;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.jbangit.uicomponents.R;
import com.jbangit.uicomponents.common.DensityUtils;
import com.jbangit.uicomponents.common.Globals;
import com.jbangit.uicomponents.common.resource.ShapeDrawableUtils;
import com.jbangit.uicomponents.common.view.ViewRecycler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * {@link GridRadioGroup#setItem(Collection)}
 *
 * <p>{@link GridRadioGroup#initItem(Collection)}
 *
 * <p>{@link GridRadioGroup#getCheckedIndexes()}
 *
 * <p>{@link GridRadioGroup#getCheckedItems()}
 *
 * <p>{@link GridRadioGroup#check(int)}
 *
 * <p>{@link GridRadioGroup#setOnCheckedChangeListener(OnCheckedChangeListener)}
 */
public class GridRadioGroup extends ViewGroup {

    private static final int DEFAULT_HORIZON_INSET = 12;

    private static final int ROW_COUNT = 4;

    private static final int DEFAULT_VERTICAL_INSET = 16;

    private static final int DEFAULT_VERTICAL_INSET_PADDING = 8;

    private static final int DEFAULT_HORIZON_INSET_PADDING = 4;

    private Set<Integer> mSelectedIndexes = new TreeSet<>();

    private int mGridHorizonInset;

    private int mGridVerticalInset;

    @ColorInt
    private int mAttrCheckedColor;

    @ColorInt
    private int mAttrUncheckedColor;

    @ColorInt
    private int mAttrCheckedTextColor;

    @ColorInt
    private int mAttrUncheckedTextColor;

    @ColorInt
    private int mAttrCheckedStrokeColor;

    @ColorInt
    private int mAttrUncheckedStrokeColor;

    @Dimension()
    private int mAttrStrokeWidth;

    @Dimension()
    private int mAttrRadius;

    private int mButtonVerticalPadding;

    private int mButtonHorizonPadding;

    private int mGridWidth = 0;

    private int mGridHeight = 0;

    private OnCheckedChangeListener mOnCheckedChangeListener;

    private List<String> mItems = Collections.emptyList();

    private ViewRecycler mViewRecycler = new ViewRecycler();

    private List<ViewHolder> mViewHolders = new ArrayList<>();

    private int mAttrTextSize;

    private Drawable mCheckedBackground;

    private Drawable mUncheckedBackground;

    private Drawable mForegroundDrawable;

    private Drawable mForegroundRippleMasker;

    public GridRadioGroup(Context context) {
        super(context);
    }

    public GridRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public GridRadioGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GridRadioGroup);

        int primaryColor = Globals.getPrimaryColor(context);

        mAttrCheckedColor = primaryColor;
        mAttrCheckedTextColor = getResources().getColor(R.color.colorForeground);
        mAttrUncheckedColor = getResources().getColor(R.color.colorBackground);
        mAttrUncheckedTextColor = getResources().getColor(R.color.colorTextDark);
        mAttrTextSize = DensityUtils.getPxFromSp(context, 16);

        mAttrTextSize =
                typedArray.getDimensionPixelOffset(
                        R.styleable.GridRadioGroup_gridRadioGroupRadius, mAttrTextSize);

        mAttrCheckedColor =
                typedArray.getColor(
                        R.styleable.GridRadioGroup_gridRadioGroupCheckedColor, mAttrCheckedColor);
        mAttrUncheckedColor =
                typedArray.getColor(
                        R.styleable.GridRadioGroup_gridRadioGroupUncheckedColor,
                        mAttrUncheckedColor);
        mAttrCheckedTextColor =
                typedArray.getColor(
                        R.styleable.GridRadioGroup_gridRadioGroupCheckedTextColor,
                        mAttrCheckedTextColor);
        mAttrUncheckedTextColor =
                typedArray.getColor(
                        R.styleable.GridRadioGroup_gridRadioGroupUncheckedTextColor,
                        mAttrUncheckedTextColor);
        mAttrRadius =
                typedArray.getDimensionPixelOffset(
                        R.styleable.GridRadioGroup_gridRadioGroupRadius, mAttrRadius);

        mAttrCheckedStrokeColor =
                typedArray.getColor(
                        R.styleable.GridRadioGroup_gridRadioGroupCheckedStrokeColor,
                        mAttrCheckedStrokeColor);

        mAttrUncheckedStrokeColor =
                typedArray.getColor(
                        R.styleable.GridRadioGroup_gridRadioGroupUncheckedStrokeColor,
                        mAttrUncheckedTextColor);

        mAttrStrokeWidth =
                typedArray.getDimensionPixelOffset(
                        R.styleable.GridRadioGroup_gridRadioGroupStrokeWidth,
                        mAttrStrokeWidth
                );

        typedArray.recycle();

        mGridHorizonInset = DensityUtils.getPxFromDp(context, DEFAULT_HORIZON_INSET);
        mGridVerticalInset = DensityUtils.getPxFromDp(context, DEFAULT_VERTICAL_INSET);
        mButtonHorizonPadding = DensityUtils.getPxFromDp(context, DEFAULT_HORIZON_INSET_PADDING);
        mButtonVerticalPadding = DensityUtils.getPxFromDp(context, DEFAULT_VERTICAL_INSET_PADDING);

        generateDrawable(context);
    }

    private void generateDrawable(Context context) {
        mCheckedBackground =
                ShapeDrawableUtils.builder(context)
                        .solid(mAttrCheckedColor)
                        .corner(mAttrRadius)
                        .strokePx(mAttrStrokeWidth, mAttrCheckedStrokeColor)
                        .build();
        mUncheckedBackground =
                ShapeDrawableUtils.builder(context)
                        .solid(mAttrUncheckedColor)
                        .corner(mAttrRadius)
                        .strokePx(mAttrStrokeWidth, mAttrUncheckedStrokeColor)
                        .build();
        mForegroundDrawable = new ColorDrawable(Color.TRANSPARENT);
        mForegroundRippleMasker =
                ShapeDrawableUtils.builder(getContext())
                        .corner(mAttrRadius)
                        .solid(Color.BLACK)
                        .strokePx(mAttrStrokeWidth, mAttrUncheckedStrokeColor)
                        .build();
    }

    private void setupItemView() {
        cachedAllView();
        mViewHolders.clear();
        removeAllViewsInLayout();

        for (int index = 0; index < mItems.size(); index++) {
            View view = getView();
            mViewHolders.add(new ViewHolder(view, index));
            addViewInLayout(view, -1, view.getLayoutParams(), true);
        }

        requestLayout();
    }

    private View getView() {
        View view = mViewRecycler.get();
        if (view == null) {
            view = createView();
            view.setLayoutParams(generateDefaultLayoutParams());
        }
        return view;
    }

    private View createView() {
        return new AppCompatTextView(getContext());
    }

    private void cachedAllView() {
        for (ViewHolder viewHolder : mViewHolders) {
            mViewRecycler.recycle(viewHolder.getView());
        }
    }

    public void check(int i) {
        if (mSelectedIndexes.contains(i)) {
            mSelectedIndexes.remove(i);
        } else {
            mSelectedIndexes.add(i);
        }
    }

    private void showChecked(int index) {
        if (mSelectedIndexes.contains(index)) {
            mViewHolders.get(index).setChecked(true);
        } else {
            mViewHolders.get(index).setChecked(false);
        }
    }

    /** the checked will not be clear */
    public void setItem(Collection<String> item) {
        mItems = new ArrayList<>(item);
        setupItemView();
    }

    /** clear all checked */
    public void initItem(Collection<String> item) {
        mItems = new ArrayList<>(item);
        mSelectedIndexes.clear();
        setupItemView();
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        SaveState saveState = new SaveState(super.onSaveInstanceState());
        saveState.mItems = mItems;
        saveState.mSelectIndexes = mSelectedIndexes;
        return saveState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SaveState saveState = (SaveState) state;
        mItems = saveState.mItems;
        mSelectedIndexes = saveState.mSelectIndexes;
        setupItemView();
        super.onRestoreInstanceState(saveState.getSuperState());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (getChildCount() == 0) {
            setMeasuredDimension(0, 0);
            return;
        }

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        switch (widthMode) {
            case MeasureSpec.AT_MOST:
                break;
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }

        int allInsetWith = mGridHorizonInset * (ROW_COUNT - 1);
        int allPaddingWidth = getPaddingLeft() + getPaddingRight();
        mGridWidth = (width - allInsetWith - allPaddingWidth) / ROW_COUNT;

        measureChildren();
        int lineCount = ((getChildCount() - 1) / ROW_COUNT) + 1;
        mGridHeight = getChildAt(0).getMeasuredHeight();

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int allInsetHeight = mGridVerticalInset * (lineCount - 1);
        int allPaddingHeight = getPaddingTop() + getPaddingBottom();
        int expectHeight = lineCount * mGridHeight + allInsetHeight + allPaddingHeight;

        int height = heightSize;
        switch (heightMode) {
            case MeasureSpec.AT_MOST:
                height = Math.min(heightSize, expectHeight);
                break;
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }

        setMeasuredDimension(width, height);
    }

    private void measureChildren() {
        int childCount = getChildCount();

        int widthSpec = MeasureSpec.makeMeasureSpec(mGridWidth, MeasureSpec.EXACTLY);
        int heightSpec = MeasureSpec.UNSPECIFIED;
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).measure(widthSpec, heightSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = getPaddingLeft(), top = getPaddingTop();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);

            childView.layout(
                    left,
                    top,
                    left + childView.getMeasuredWidth(),
                    top + childView.getMeasuredHeight());

            left += mGridWidth + mGridHorizonInset;

            if ((i + 1) % ROW_COUNT == 0) {
                // end of the line
                left = getPaddingLeft();
                top += mGridHeight + mGridVerticalInset;
            }
        }

        initItemViews();
    }

    private void initItemViews() {
        for (int i = 0; i < mViewHolders.size(); i++) {
            ViewHolder viewHolder = mViewHolders.get(i);
            viewHolder.setTitle(mItems.get(i));

            if (mSelectedIndexes.contains(i)) {
                viewHolder.setChecked(true);
            } else {
                viewHolder.setChecked(false);
            }
        }
    }

    public OnCheckedChangeListener getOnCheckedChangeListener() {
        return mOnCheckedChangeListener;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        mOnCheckedChangeListener = onCheckedChangeListener;
    }

    public Collection<Integer> getCheckedIndexes() {
        return mSelectedIndexes;
    }

    public Collection<String> getCheckedItems() {
        ArrayList<String> items = new ArrayList<>();
        for (Integer index : mSelectedIndexes) {
            items.add(mItems.get(index));
        }
        return items;
    }

    public interface OnCheckedChangeListener {

        /** @return false to intercept */
        boolean onCheckedChange(int index, String item);
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

        List<String> mItems;

        Set<Integer> mSelectIndexes;

        SaveState(Parcelable superState) {
            super(superState);
        }

        SaveState(Parcel source) {
            super(source);
            source.readStringList(mItems);

            int selectIndexesSize = source.readInt();

            for (int i = 0; i < selectIndexesSize; i++) {
                mSelectIndexes.add(source.readInt());
            }
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeStringList(mItems);

            out.writeInt(mSelectIndexes.size());
            for (Integer index : mSelectIndexes) {
                out.writeInt(index);
            }
        }
    }

    public void onClickItem(int index) {
        if (mOnCheckedChangeListener != null) {
            if (mOnCheckedChangeListener.onCheckedChange(index, mItems.get(index))) {
                check(index);
                showChecked(index);
            }
        } else {
            check(index);
            showChecked(index);
        }
    }

    class ViewHolder {

        private final AppCompatTextView mButton;

        private final int mIndex;

        ViewHolder(View view, int index) {
            mIndex = index;
            mButton = (AppCompatTextView) view;

            mButton.setGravity(Gravity.CENTER);
            mButton.setPadding(
                    mButtonHorizonPadding,
                    mButtonVerticalPadding,
                    mButtonHorizonPadding,
                    mButtonVerticalPadding);

            TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(
                    mButton, 1, mAttrTextSize, 4, TypedValue.COMPLEX_UNIT_PX);

            mButton.setOnClickListener(
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onClickItem(mIndex);
                        }
                    });
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mButton.setForeground(
                        Globals.addRipple(
                                getContext(), mForegroundDrawable, mForegroundRippleMasker));
            }
        }

        void setTitle(String title) {
            mButton.setText(title);
        }

        void setChecked(boolean checked) {
            if (checked) {
                mButton.setBackground(mCheckedBackground);
                mButton.setTextColor(mAttrCheckedTextColor);
            } else {
                mButton.setBackground(mUncheckedBackground);
                mButton.setTextColor(mAttrUncheckedTextColor);
            }
        }

        View getView() {
            return mButton;
        }
    }
}
