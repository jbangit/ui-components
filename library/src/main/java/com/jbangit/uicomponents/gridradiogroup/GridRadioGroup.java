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
import android.support.annotation.NonNull;
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
import com.jbangit.uicomponents.common.viewgroup.layouthelper.LayoutHelper;
import com.jbangit.uicomponents.common.viewgroup.layouthelper.gridlayouthelper.FixedPaddingGridLayoutHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * {@link GridRadioGroup#setItem(Collection)}
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

    private static final int DEFAULT_VERTICAL_INSET_PADDING = 8;

    private static final int DEFAULT_HORIZON_INSET_PADDING = 4;

    public static final int NONE_CHECKED_INDEX = -1;

    private final Set<Integer> mCheckedIndexes = new TreeSet<>();

    private int mLastCheckedIndex = NONE_CHECKED_INDEX;

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

    private int mAttrRowNumber = 4;

    private int mAttrVerticalInsetPadding = DensityUtils.getPxFromDp(getContext(), 12);

    private int mAttrHorizonInsetPadding = DensityUtils.getPxFromDp(getContext(), 12);

    private float mAttrVerticalInsetFraction = -1f;

    private float mAttrHorizonInsetFraction = -1f;

    private boolean mAttrIsOuterPadding = true;

    private int mButtonVerticalPadding;

    private int mButtonHorizonPadding;

    private OnCheckedChangeListener mOnCheckedChangeListener;

    private final List<String> mItems = new ArrayList<>();

    private final ViewRecycler mViewRecycler = new ViewRecycler();

    private final List<ViewHolder> mViewHolders = new ArrayList<>();

    private int mAttrTextSize;

    private boolean mAttrCheckable = true;

    private boolean mAttrMultipleChoice = false;

    private boolean mAttrAllowEmptyChoice = false;

    private Drawable mCheckedBackground;

    private Drawable mUncheckedBackground;

    private Drawable mForegroundDrawable;

    private Drawable mForegroundRippleMasker;

    private ViewHolder mSampleViewHolder;

    private LayoutHelper mLayoutHelper = null;

    public GridRadioGroup(Context context) {
        super(context);
    }

    public GridRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        initLayoutHelper();
    }

    public GridRadioGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initLayoutHelper();
    }

    private void initAttrs(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GridRadioGroup);

        int primaryColor;
        primaryColor = Globals.getPrimaryColor(context);

        mAttrCheckedColor = primaryColor;
        mAttrCheckedTextColor = getResources().getColor(R.color.colorForeground);
        mAttrUncheckedColor = getResources().getColor(R.color.colorBackground);
        mAttrUncheckedTextColor = getResources().getColor(R.color.colorTextDark);
        mAttrTextSize = DensityUtils.getPxFromSp(context, 16);

        mAttrRowNumber =
                typedArray.getInt(
                        R.styleable.GridRadioGroup_gridRadioGroupRowNumber, mAttrRowNumber);

        mAttrHorizonInsetPadding =
                typedArray.getDimensionPixelOffset(
                        R.styleable.GridRadioGroup_gridRadioGroupHorizonInsetPadding,
                        mAttrHorizonInsetPadding);
        mAttrVerticalInsetPadding =
                typedArray.getDimensionPixelOffset(
                        R.styleable.GridRadioGroup_gridRadioGroupVerticalInsetPadding,
                        mAttrVerticalInsetPadding);
        mAttrHorizonInsetFraction =
                typedArray.getFraction(
                        R.styleable.GridRadioGroup_gridRadioGroupHorizonInsetFraction,
                        1,
                        1,
                        mAttrHorizonInsetFraction);
        mAttrVerticalInsetFraction =
                typedArray.getFraction(
                        R.styleable.GridRadioGroup_gridRadioGroupVerticalInsetFraction,
                        1,
                        1,
                        mAttrVerticalInsetFraction);
        mAttrIsOuterPadding =
                typedArray.getBoolean(R.styleable.GridRadioGroup_gridRadioGroupOuterPadding, false);

        mAttrTextSize =
                typedArray.getDimensionPixelOffset(
                        R.styleable.GridRadioGroup_gridRadioGroupTextSize, mAttrTextSize);
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
        mAttrCheckedStrokeColor =
                typedArray.getColor(
                        R.styleable.GridRadioGroup_gridRadioGroupCheckedStrokeColor,
                        mAttrCheckedStrokeColor);
        mAttrUncheckedStrokeColor =
                typedArray.getColor(
                        R.styleable.GridRadioGroup_gridRadioGroupUncheckedStrokeColor,
                        mAttrUncheckedStrokeColor);
        mAttrRadius =
                typedArray.getDimensionPixelOffset(
                        R.styleable.GridRadioGroup_gridRadioGroupRadius, mAttrRadius);
        mAttrStrokeWidth =
                typedArray.getDimensionPixelOffset(
                        R.styleable.GridRadioGroup_gridRadioGroupStrokeWidth, mAttrStrokeWidth);
        mAttrCheckable =
                typedArray.getBoolean(
                        R.styleable.GridRadioGroup_gridRadioGroupCheckable, mAttrCheckable);
        mAttrMultipleChoice =
                typedArray.getBoolean(
                        R.styleable.GridRadioGroup_gridRadioGroupMultipleChoice,
                        mAttrMultipleChoice);
        mAttrAllowEmptyChoice =
                typedArray.getBoolean(
                        R.styleable.GridRadioGroup_gridRadioGroupAllowEmptyChoice,
                        mAttrAllowEmptyChoice);

        typedArray.recycle();

        mButtonHorizonPadding = DensityUtils.getPxFromDp(context, DEFAULT_HORIZON_INSET_PADDING);
        mButtonVerticalPadding = DensityUtils.getPxFromDp(context, DEFAULT_VERTICAL_INSET_PADDING);

        generateDrawable(context);

        mSampleViewHolder = new ViewHolder(createView(), -1);
        mSampleViewHolder.setTitle("S");
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

    private void initLayoutHelper() {
        LayoutHelper.ViewGroupHelper viewGroupHelper =
                new LayoutHelper.ViewGroupHelper() {
                    @Override
                    public View getChildView(int index) {
                        return mViewHolders.get(index).getView();
                    }

                    @Override
                    public int getChildCount() {
                        return mViewHolders.size();
                    }

                    @Override
                    public void onSetMeasuredDimension(int measuredWidth, int measuredHeight) {
                        setMeasuredDimension(measuredWidth, measuredHeight);
                    }

                    @Override
                    @NonNull
                    public ViewGroup getViewGroup() {
                        return GridRadioGroup.this;
                    }
                };

        FixedPaddingGridLayoutHelper layoutHelper =
                new FixedPaddingGridLayoutHelper(this);
        layoutHelper.setViewGroupHelper(viewGroupHelper);
        layoutHelper.setRowNumber(mAttrRowNumber);
        layoutHelper.setOnGetChildViewHeight(
                new FixedPaddingGridLayoutHelper.OnGetChildViewHeight() {
                    @Override
                    public int onGetChildViewHeight() {
                        return getItemHeight();
                    }
                });
        layoutHelper.setHorizonInsetPadding(mAttrHorizonInsetPadding);
        layoutHelper.setHorizonInsetFraction(mAttrHorizonInsetFraction);
        layoutHelper.setVerticalInsetPadding(mAttrVerticalInsetPadding);
        layoutHelper.setVerticalInsetFraction(mAttrVerticalInsetFraction);
        layoutHelper.setOuterPadding(mAttrIsOuterPadding);

        mLayoutHelper = layoutHelper;
    }

    private int getItemHeight() {
        View sampleView = mSampleViewHolder.getView();
        sampleView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        return sampleView.getMeasuredHeight();
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

        initItemViews();
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

    private void initItemViews() {
        for (int i = 0; i < mViewHolders.size(); i++) {
            ViewHolder viewHolder = mViewHolders.get(i);
            viewHolder.setTitle(mItems.get(i));

            viewHolder.setChecked(mCheckedIndexes.contains(i));
        }
    }

    private View createView() {
        return new AppCompatTextView(getContext());
    }

    private void cachedAllView() {
        for (ViewHolder viewHolder : mViewHolders) {
            mViewRecycler.recycle(viewHolder.getView());
        }
    }

    public void check(int index) {
        if (mAttrMultipleChoice) {
            doMultipleCheck(index);
        } else {
            doSingleCheck(index);
        }
    }

    private void doSingleCheck(int index) {
        if (mLastCheckedIndex == index) {
            return;
        }

        mCheckedIndexes.clear();
        mCheckedIndexes.add(index);

        mViewHolders.get(mLastCheckedIndex).setChecked(false);
        mViewHolders.get(index).setChecked(true);

        mLastCheckedIndex = index;
    }

    private void doMultipleCheck(int index) {
        if (mCheckedIndexes.contains(index)) {
            mCheckedIndexes.remove(index);
            mViewHolders.get(index).setChecked(false);
        } else {
            mCheckedIndexes.add(index);
            mViewHolders.get(index).setChecked(true);
        }
    }

    /**
     * the checked will be clear
     */
    public void setItem(Collection<String> item) {
        mItems.clear();
        mItems.addAll(item);

        mCheckedIndexes.clear();
        processEmptyChoice();

        setupItemView();
    }

    /** Only in single choice mode, */
    private void processEmptyChoice() {
        if (mAttrAllowEmptyChoice) {
            mLastCheckedIndex = -1;
        } else if (!mAttrMultipleChoice && mItems.size() > 0) {
            mLastCheckedIndex = 0;
            mCheckedIndexes.add(0);
        }
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        SaveState saveState = new SaveState(super.onSaveInstanceState());
        saveState.mItems = mItems;
        saveState.mSelectIndexes = mCheckedIndexes;
        return saveState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SaveState saveState = (SaveState) state;
        mItems.clear();
        mItems.addAll(saveState.mItems);
        mCheckedIndexes.clear();
        mCheckedIndexes.addAll(saveState.mSelectIndexes);

        setupItemView();
        super.onRestoreInstanceState(saveState.getSuperState());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initEditMode();
    }

    private void initEditMode() {
        if (isInEditMode()) {
            setItem(
                    Arrays.asList(
                            "One",
                            "Two",
                            "Three",
                            "Four",
                            "Five",
                            "Long Long Long"));
            if (!mAttrAllowEmptyChoice) {
                check(2);
                check(1);
                check(0);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mLayoutHelper.onViewGroupMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mLayoutHelper.onViewGroupLayout(changed, l, t, r, b);
    }

    public OnCheckedChangeListener getOnCheckedChangeListener() {
        return mOnCheckedChangeListener;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        mOnCheckedChangeListener = onCheckedChangeListener;
    }

    public Collection<Integer> getCheckedIndexes() {
        return new ArrayList<>(mCheckedIndexes);
    }

    public Collection<String> getCheckedItems() {
        ArrayList<String> items = new ArrayList<>();
        for (Integer index : mCheckedIndexes) {
            items.add(mItems.get(index));
        }
        return items;
    }

    public interface OnCheckedChangeListener {

        /** @return false to intercept */
        boolean onCheckedChange(int oldIndex, String oldItem, int newIndex, String newItem);
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

            int checkedIndexesSize = source.readInt();

            for (int i = 0; i < checkedIndexesSize; i++) {
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
            if (mAttrMultipleChoice) {
                if (mOnCheckedChangeListener.onCheckedChange(
                        NONE_CHECKED_INDEX, null, index, mItems.get(index))) {
                    check(index);
                }
            } else {
                String lastItem =
                        mLastCheckedIndex == NONE_CHECKED_INDEX
                                ? null
                                : mItems.get(mLastCheckedIndex);
                if (mOnCheckedChangeListener.onCheckedChange(
                        mLastCheckedIndex, lastItem, index, mItems.get(index))) {
                    check(index);
                }
            }
        } else {
            check(index);
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

            if (mAttrCheckable) {
                mButton.setOnClickListener(
                        new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onClickItem(mIndex);
                            }
                        });
            }
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
