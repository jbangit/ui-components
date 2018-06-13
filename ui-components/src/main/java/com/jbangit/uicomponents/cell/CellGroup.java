package com.jbangit.uicomponents.cell;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jbangit.uicomponents.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CellGroup extends LinearLayout {

    private String mAttrTitle;

    private TextView mTitle;

    private ArrayList<CheckedCell> mCheckedCells;

    private OnCheckedChangeListener mOnCheckedChangeListener;

    public CellGroup(Context context) {
        super(context);
        defaultInit();
    }

    private void defaultInit() {
        setOrientation(LinearLayout.VERTICAL);
    }

    public CellGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        defaultInit();
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CellGroup);
        mAttrTitle = typedArray.getString(R.styleable.CellGroup_cellGroupTitle);
        typedArray.recycle();
    }

    public CellGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        defaultInit();
        initAttrs(context, attrs);
    }

    @BindingAdapter("onCheckedChanged")
    public static void setCheckedChangeListener(
            CellGroup cellGroup, OnCheckedChangeListener listener) {
        cellGroup.setOnCheckedChangeListener(listener);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findAllCheckedCell();
        addHeaderAndDivider(getContext());
        initViews();
    }

    private void addHeaderAndDivider(Context context) {
        int childCount = getChildCount();

        List<View> views = new ArrayList<>(childCount);
        for (int i = 0; i < childCount; i++) {
            views.add(getChildAt(i));
        }

        removeAllViewsInLayout();

        addViewInLayout(createHeaderView(context));
        Iterator<View> iterator = views.iterator();
        addView(iterator.next());
        while (iterator.hasNext()) {
            addViewInLayout(createDividerView(context));
            addViewInLayout(iterator.next());
        }
    }

    private void initViews() {
        setTitle(mAttrTitle);
    }

    private void addViewInLayout(View view) {
        addViewInLayout(view, -1, view.getLayoutParams());
    }

    private View createHeaderView(Context context) {
        View headerView = LayoutInflater.from(context).inflate(R.layout.view_header_cell_group, this, false);
        mTitle = headerView.findViewById(R.id.title);
        return headerView;
    }

    private View createDividerView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.view_divider_cell_group, this, false);
    }

    @Nullable
    public CharSequence getTitle() {
        return mTitle.getText();
    }

    public void setTitle(@Nullable CharSequence title) {
        if (title == null) {
            mTitle.setVisibility(View.GONE);
            return;
        }
        mTitle.setText(title);
    }

    private void findAllCheckedCell() {
        int childCount = getChildCount();
        mCheckedCells = new ArrayList<>(childCount);

        for (int i = 0; i < childCount; i++) {
            View cell = getChildAt(i);
            if (cell instanceof CheckedCell) {
                mCheckedCells.add((CheckedCell) cell);
            }
        }
    }

    public OnCheckedChangeListener getOnCheckedChangeListener() {
        return mOnCheckedChangeListener;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        mOnCheckedChangeListener = onCheckedChangeListener;
    }

    /**
     * @return is the Checked Cell checked status changed
     */
    boolean check(CheckedCell checkedCell) {
        if (checkedCell.isChecked()) {
            // this cell has been checked
            return false;
        } else {
            performCheck(checkedCell);
            return true;
        }
    }

    private void performCheck(CheckedCell checkedCell) {
        int checkedId = 0;
        CheckedCell uncheckedCell = null;
        int uncheckedId = -1;

        for (int i = 0; i < mCheckedCells.size(); i++) {
            CheckedCell thatCheckedCell = mCheckedCells.get(i);

            if (thatCheckedCell.isChecked()) {
                uncheckedCell = thatCheckedCell;
                uncheckedId = i;
            }

            if (thatCheckedCell.getId() == checkedCell.getId()) {
                thatCheckedCell.setChecked(true);
                checkedId = i;
            } else {
                thatCheckedCell.setChecked(false);
            }
        }

        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChange(
                    this, checkedCell, checkedId, uncheckedCell, uncheckedId);
        }
    }

    /**
     * @return The id of checked cell. -1 if no checked
     */
    @IdRes
    public int getCheckedId() {
        for (CheckedCell checkedCell : mCheckedCells) {
            if (checkedCell.isChecked()) {
                return checkedCell.getId();
            }
        }
        return -1;
    }

    /**
     * The first or second or third ... CheckedCell checked, exclude other cell. Start with 0. -1 if
     * no checked
     */
    public int getCheckedIndex() {
        int checkedCellCount = mCheckedCells.size();

        for (int i = 0; i < checkedCellCount; i++) {
            if (mCheckedCells.get(i).isChecked()) {
                return i;
            }
        }
        return -1;
    }

    public interface OnCheckedChangeListener {

        void onCheckedChange(
                CellGroup cellGroup,
                CheckedCell checkedCell,
                int checkedIndex,
                CheckedCell uncheckedCell,
                int uncheckedIndex);
    }
}
