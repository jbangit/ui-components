package com.jbangit.uicomponents.gridradiogroup;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.RippleDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jbangit.uicomponents.R;
import com.jbangit.uicomponents.common.DensityUtils;
import com.jbangit.uicomponents.common.Globals;

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

    private List<String> mItems = Collections.emptyList();

    private Set<Integer> mSelectedIndexes = new TreeSet<>();

    private int mGridHorizonInset;

    private int mGridVerticalInset;

    private int mAttrCheckedColor;

    private int mAttrUncheckedColor;

    private int mGridWidth = 0;

    private int mGridHeight = 0;

    private OnCheckedChangeListener mOnCheckedChangeListener;

    public GridRadioGroup(Context context) {
        super(context);
    }

    public GridRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GridRadioGroup);
        mAttrCheckedColor =
                typedArray.getColor(
                        R.styleable.GridRadioGroup_gridRadioGroupCheckedColor,
                        Globals.getPrimaryColor(context));
        typedArray.recycle();

        mAttrUncheckedColor = getResources().getColor(R.color.colorTextGray);
        mGridHorizonInset = DensityUtils.getPxFromDp(context, DEFAULT_HORIZON_INSET);
        mGridVerticalInset = DensityUtils.getPxFromDp(context, DEFAULT_VERTICAL_INSET);
        setBackgroundColor(getResources().getColor(R.color.colorForeground));
    }

    public GridRadioGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void showItems() {
        removeAllViewsInLayout();
        for (int i = 0; i < mItems.size(); i++) {
            String item = mItems.get(i);
            addItemView(item, i);
        }

        showAllChecked();
        requestLayout();
    }

    private void addItemView(final String item, final int i) {
        View itemView =
                LayoutInflater.from(getContext())
                        .inflate(R.layout.view_grid_radio_group, this, false);
        TextView titleView = itemView.findViewById(R.id.title);
        titleView.setText(item);
        addViewInLayout(itemView, -1, null, true);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            RippleDrawable r =
                    new RippleDrawable(
                            ColorStateList.valueOf(
                                    getResources().getColor(R.color.colorTextLightGray)),
                            titleView.getBackground(),
                            null);
            titleView.setBackground(r);
        }

        itemView.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnCheckedChangeListener != null) {
                            if (mOnCheckedChangeListener.onCheckedChange(i, item)) {
                                check(i);
                                showChecked(i);
                            }
                        } else {
                            check(i);
                            showChecked(i);
                        }
                    }
                });
    }

    private void showAllChecked() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            showChecked(i);
        }
    }

    public void check(int i) {
        if (mSelectedIndexes.contains(i)) {
            mSelectedIndexes.remove(i);
        } else {
            mSelectedIndexes.add(i);
        }
    }

    private void showChecked(int i) {
        if (mSelectedIndexes.contains(i)) {
            ((TextView) getItemView(i).findViewById(R.id.title)).setTextColor(mAttrCheckedColor);
        } else {
            ((TextView) getItemView(i).findViewById(R.id.title)).setTextColor(mAttrUncheckedColor);
        }
    }

    private View getItemView(int i) {
        return getChildAt(i);
    }

    /** the checked will not be clear */
    public void setItem(Collection<String> item) {
        mItems = new ArrayList<>(item);
        showItems();
    }

    /** clear all checked */
    public void initItem(Collection<String> item) {
        mItems = new ArrayList<>(item);
        mSelectedIndexes.clear();
        showItems();
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
        showItems();
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
}
