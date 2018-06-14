package com.jbangit.uicomponents.cell;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
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

    public static int ID_NULL_CHECK = -1;

    public static int INDEX_NULL_CHECK = -1;

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
     * @return The id of checked cell. {@link #ID_NULL_CHECK} if no checked
     */
    @IdRes
    public int getCheckedId() {
        for (CheckedCell checkedCell : mCheckedCells) {
            if (checkedCell.isChecked()) {
                return checkedCell.getId();
            }
        }
        return ID_NULL_CHECK;
    }

    /**
     * @param id set to {@link #ID_NULL_CHECK} to clear check
     */
    public void setCheckedId(@IdRes int id) {
        if (id == ID_NULL_CHECK) {
            clearCheck();
            return;
        }

        for (CheckedCell checkedCell : mCheckedCells) {
            if (checkedCell.getId() == id) {
                check(checkedCell, false);
                return;
            }
        }
    }

    private void clearCheck() {
        mCheckedCells.get(getCheckedIndex()).setChecked(false);
    }

    /**
     * @return is the CheckedCell checked status changed
     */
    boolean check(CheckedCell checkedCell, boolean byUser) {
        if (checkedCell.isChecked()) {
            // this cell has been checked
            return false;
        }

        performCheck(checkedCell, byUser);
        return true;
    }

    /**
     * The first or second or third ... CheckedCell checked, exclude other cell. Start with 0.
     * {@link #INDEX_NULL_CHECK} if no checked
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

    private void performCheck(CheckedCell checkedCell, boolean byUser) {
        int checkedIndex = INDEX_NULL_CHECK;
        CheckedCell uncheckedCell = null;
        int uncheckedIndex = INDEX_NULL_CHECK;

        for (int i = 0; i < mCheckedCells.size(); i++) {
            CheckedCell thatCheckedCell = mCheckedCells.get(i);

            if (thatCheckedCell.isChecked()) {
                uncheckedCell = thatCheckedCell;
                uncheckedIndex = i;
            }

            if (thatCheckedCell.getId() == checkedCell.getId()) {
                thatCheckedCell.setChecked(true);
                checkedIndex = i;
            } else {
                thatCheckedCell.setChecked(false);
            }
        }

        if (byUser && mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChange(
                    this, checkedCell, checkedIndex, uncheckedCell, uncheckedIndex);
        }
    }

    /**
     * @param id set to {@link #INDEX_NULL_CHECK} to clear check
     */
    public void setCheckedIndex(int id) {
        if (id == -1) {
            clearCheck();
            return;
        }

        if (id < mCheckedCells.size()) {
            check(mCheckedCells.get(id), false);
        }
    }

    public interface OnCheckedChangeListener {

        /**
         * Callback only when the cell checked status change with user.
         * {@link #setCheckedId(int)}, {@link #setCheckedIndex(int)} will NOT let it called
         *
         * @param checkedCell null if there is no cell has been checked
         * @param checkedIndex {@link #INDEX_NULL_CHECK} if there is no cell has been checked
         * @param uncheckedCell null if there is no cell has been unchecked
         * @param uncheckedIndex {@link #INDEX_NULL_CHECK} if there is no cell has been unchecked
         */
        void onCheckedChange(
                @NonNull CellGroup cellGroup,
                @Nullable CheckedCell checkedCell,
                int checkedIndex,
                @Nullable CheckedCell uncheckedCell,
                int uncheckedIndex);
    }
}
