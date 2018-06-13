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

    private void addViewInLayout(View view) {
        addViewInLayout(view, -1, view.getLayoutParams());
    }

    private void initViews() {
        setTitle(mAttrTitle);
        initCheckedCell();
    }

    private View createHeaderView(Context context) {
        View headerView = LayoutInflater.from(context).inflate(R.layout.view_header_cell_group, this, false);
        mTitle = headerView.findViewById(R.id.title);
        return headerView;
    }

    private View createDividerView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.view_divider_cell_group, this, false);
    }

    private void initCheckedCell() {
        if (mCheckedCells.isEmpty()) {
            return;
        }

        boolean hasChecked = false;
        for (CheckedCell checkedCell : mCheckedCells) {
            if (!hasChecked) {
                //find the first checked
                if (checkedCell.isChecked()) {
                    hasChecked = true;
                }
            } else {
                //then uncheck the remain
                checkedCell.setChecked(false);
            }
        }

        if (!hasChecked) {
            // can't find the checked
            mCheckedCells.get(0).setChecked(true);
        }
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
     * @return is the CheckedCell checked status changed
     */
    boolean check(CheckedCell checkedCell, boolean byUser) {
        if (checkedCell.isChecked()) {
            // this cell has been checked
            return false;
        } else {
            performCheck(checkedCell, byUser);
            return true;
        }
    }

    private void performCheck(CheckedCell checkedCell, boolean byUser) {
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

        if (byUser && mOnCheckedChangeListener != null) {
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

    public void setCheckedId(@IdRes int id) {
        for (CheckedCell checkedCell : mCheckedCells) {
            if (checkedCell.getId() == id) {
                check(checkedCell, false);
                return;
            }
        }
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

    public void setCheckedIndex(int id) {
        if (id < mCheckedCells.size()) {
            check(mCheckedCells.get(id), false);
        }
    }

    public interface OnCheckedChangeListener {

        /**
         * Callback only when the cell checked with user.
         * {@link #setCheckedId(int)}, {@link #setCheckedIndex(int)} will NOT let it called
         */
        void onCheckedChange(
                CellGroup cellGroup,
                CheckedCell checkedCell,
                int checkedIndex,
                CheckedCell uncheckedCell,
                int uncheckedIndex);
    }
}
