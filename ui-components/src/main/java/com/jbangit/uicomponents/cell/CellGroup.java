package com.jbangit.uicomponents.cell;

import android.content.Context;
import android.content.res.TypedArray;
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

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
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
}
