package com.jbangit.uicomponents.gridradiogroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
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
import android.text.TextUtils;
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
    private int mAttrUncheckableColor;

    @ColorInt
    private int mAttrCheckedTextColor;

    @ColorInt
    private int mAttrUncheckedTextColor;

    @ColorInt
    private int mAttrUncheckableTextColor;

    @ColorInt
    private int mAttrCheckedStrokeColor;

    @ColorInt
    private int mAttrUncheckedStrokeColor;

    @ColorInt
    private int mAttrUncheckableStrokeColor;

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

    private final List<CharSequence> mItems = new ArrayList<>();

    private final ViewRecycler mViewRecycler = new ViewRecycler();

    private final List<ViewHolder> mViewHolders = new ArrayList<>();

    private int mAttrTextSize;

    private boolean mAttrCheckable = true;

    private boolean mAttrMultipleChoice = false;

    private boolean mAttrAllowEmptyChoice = false;

    private CharSequence[] mAttrItems;

    private Drawable mCheckedBackground;

    private Drawable mUncheckedBackground;

    private Drawable mUncheckableBackground;

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
        mAttrUncheckableColor = getResources().getColor(R.color.colorBackground);
        mAttrUncheckableTextColor = getResources().getColor(R.color.colorTextGray);
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

        mAttrItems = typedArray.getTextArray(R.styleable.GridRadioGroup_gridRadioGroupItems);

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
        mUncheckableBackground =
                ShapeDrawableUtils.builder(context)
                        .solid(mAttrUncheckableColor)
                        .corner(mAttrRadius)
                        .strokePx(mAttrStrokeWidth, mAttrUncheckableStrokeColor)
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

        FixedPaddingGridLayoutHelper layoutHelper = new FixedPaddingGridLayoutHelper(this);
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
        initChecked();

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
        for (int index = 0; index < mViewHolders.size(); index++) {
            ViewHolder viewHolder = mViewHolders.get(index);
            viewHolder.setTitle(mItems.get(index));
            viewHolder.setChecked(false);
        }
    }

    private void initChecked() {
        mLastCheckedIndex = NONE_CHECKED_INDEX;
        for (Integer checkedIndex : mCheckedIndexes) {
            check(checkedIndex);
        }

        if (!mAttrAllowEmptyChoice && mCheckedIndexes.size() == 0) {
            check(0);
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

        if (index >= mItems.size()) {
            return;
        }

        mCheckedIndexes.clear();
        mCheckedIndexes.add(index);

        boolean canChangeCheck;
        CharSequence lastItem =
                mLastCheckedIndex == NONE_CHECKED_INDEX ? null : mItems.get(mLastCheckedIndex);
        CharSequence newItem = mItems.get(index);

        if (mOnCheckedChangeListener == null) {
            canChangeCheck = true;
        } else {
            canChangeCheck =
                    mOnCheckedChangeListener.onCheckedChange(
                            mLastCheckedIndex, lastItem, index, newItem);
        }

        if (canChangeCheck) {
            singleChangeCheck(index);
        }
    }

    private void doMultipleCheck(int index) {
        if (index >= mItems.size()) {
            return;
        }

        if (mCheckedIndexes.contains(index)) {
            mCheckedIndexes.remove(index);
        } else {
            mCheckedIndexes.add(index);
        }

        boolean canChangeCheck;
        if (mOnCheckedChangeListener == null) {
            canChangeCheck = true;
        } else {
            canChangeCheck =
                    mOnCheckedChangeListener.onCheckedChange(
                            NONE_CHECKED_INDEX, null, index, mItems.get(index));
        }

        if (canChangeCheck) {
            multipleChangeCheck(index);
        }
    }

    private void singleChangeCheck(int index) {
        if (mLastCheckedIndex < mItems.size() && mLastCheckedIndex != NONE_CHECKED_INDEX) {
            mViewHolders.get(mLastCheckedIndex).setChecked(false);
        }
        if (index < mItems.size()) {
            mViewHolders.get(index).setChecked(true);
        }
        mLastCheckedIndex = index;
    }

    private void multipleChangeCheck(int index) {
        if (index < mItems.size() && index != NONE_CHECKED_INDEX) {
            mViewHolders.get(index).setChecked(mCheckedIndexes.contains(index));
        }
    }

    public void setItem(@Nullable Collection<? extends CharSequence> item) {
        mItems.clear();
        if (item != null) {
            mItems.addAll(item);
        }

        setupItemView();
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
        mCheckedIndexes.clear();
        mCheckedIndexes.addAll(saveState.mSelectIndexes);

        setItem(saveState.mItems);
        super.onRestoreInstanceState(saveState.getSuperState());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (mAttrItems == null) {
            initEditMode();
        } else {
            setItem(Arrays.asList(mAttrItems));
        }
    }

    private void initEditMode() {
        if (isInEditMode()) {
            setItem(Arrays.asList("One", "Two", "Three", "Four", "Five", "Long Long Long"));
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

    public void setOnCheckedChangeListener(final SimpleOnCheckedChangeListener listener) {
        if (listener == null) {
            mOnCheckedChangeListener = null;
            return;
        }
        mOnCheckedChangeListener = new OnCheckedChangeListener() {
            @Override
            public boolean onCheckedChange(int oldIndex,
                                           CharSequence oldItem,
                                           int newIndex,
                                           CharSequence newItem) {
                listener.onCheckedChange(newIndex);
                return true;
            }
        };
    }

    public Collection<Integer> getCheckedIndexes() {
        return new ArrayList<>(mCheckedIndexes);
    }

    public Collection<CharSequence> getCheckedItems() {
        ArrayList<CharSequence> items = new ArrayList<>();
        for (Integer index : mCheckedIndexes) {
            items.add(mItems.get(index));
        }
        return items;
    }

    public CharSequence getCheckedItem() {
        if (mCheckedIndexes.iterator().hasNext()) {
            return mItems.get(mCheckedIndexes.iterator().next());
        } else {
            return null;
        }
    }

    public int getCheckedIndex() {
        if (mCheckedIndexes.iterator().hasNext()) {
            return mCheckedIndexes.iterator().next();
        } else {
            return -1;
        }
    }

    public interface OnCheckedChangeListener {

        /** @return false to intercept */
        boolean onCheckedChange(
                int oldIndex, CharSequence oldItem, int newIndex, CharSequence newItem);
    }

    public interface SimpleOnCheckedChangeListener {

        void onCheckedChange(int newIndex);
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

        List<CharSequence> mItems;

        Set<Integer> mSelectIndexes;

        SaveState(Parcelable superState) {
            super(superState);
        }

        SaveState(Parcel source) {
            super(source);

            int itemSize = source.readInt();
            for (int i = 0; i < itemSize; i++) {
                mItems.add(TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(source));
            }

            mSelectIndexes = new TreeSet<>();
            int checkedIndexesSize = source.readInt();
            for (int i = 0; i < checkedIndexesSize; i++) {
                mSelectIndexes.add(source.readInt());
            }
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);

            out.writeInt(mItems.size());
            for (CharSequence item : mItems) {
                TextUtils.writeToParcel(item, out, 0);
            }

            out.writeInt(mSelectIndexes.size());
            for (Integer index : mSelectIndexes) {
                out.writeInt(index);
            }
        }
    }

    public void onClickItem(int index) {
        check(index);
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

        void setTitle(CharSequence title) {
            mButton.setText(title);
        }

        void setChecked(boolean checked) {
            if (checked) {
                mButton.setBackground(mCheckedBackground);
                mButton.setTextColor(mAttrCheckedTextColor);
            } else {
                if (mAttrCheckable) {
                    mButton.setBackground(mUncheckedBackground);
                    mButton.setTextColor(mAttrUncheckedTextColor);
                } else {
                    mButton.setBackground(mUncheckableBackground);
                    mButton.setTextColor(mAttrUncheckableTextColor);
                }
            }
        }

        View getView() {
            return mButton;
        }
    }

    @BindingAdapter("gridRadioGroupItems")
    public static void setItem(
            GridRadioGroup gridRadioGroup,
            @Nullable Collection<? extends CharSequence> collections) {
        gridRadioGroup.setItem(collections);
    }

    @BindingAdapter("gridRadioGroupCheckedIndex")
    public static void setCheckedIndex(GridRadioGroup gridRadioGroup, int index) {
        gridRadioGroup.check(index);
    }

    @BindingAdapter("gridRadioGroupOnCheckedChange")
    public static void setOnCheckedChange(
            GridRadioGroup gridRadioGroup, OnCheckedChangeListener listener) {
        gridRadioGroup.setOnCheckedChangeListener(listener);
    }

    @BindingAdapter("gridRadioGroupOnCheckedChange")
    public static void setOnCheckedChange(
            GridRadioGroup gridRadioGroup, SimpleOnCheckedChangeListener listener) {
        gridRadioGroup.setOnCheckedChangeListener(listener);
    }
}
