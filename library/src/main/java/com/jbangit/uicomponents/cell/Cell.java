package com.jbangit.uicomponents.cell;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jbangit.uicomponents.R;

public class Cell extends FrameLayout {

    private TextView mTitle;

    private TextView mSubject;

    private String mAttrTitle;

    private String mAttrSubject;

    private ImageView mIcAction;

    public Cell(Context context) {
        super(context);
    }

    public Cell(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Cell);

        mAttrTitle = typedArray.getString(R.styleable.Cell_cellTitle);
        mAttrSubject = typedArray.getString(R.styleable.Cell_cellSubject);

        typedArray.recycle();
    }

    public Cell(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    public CharSequence getTitle() {
        return mTitle.getText();
    }

    public void setTitle(CharSequence title) {
        mTitle.setText(title);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        addContentView(getContext());
        initViews();
    }

    public CharSequence getSubject() {
        return mSubject.getText();
    }

    public void setSubject(CharSequence subject) {
        mSubject.setText(subject);
    }

    private void addContentView(Context context) {
        inflate(context, R.layout.view_cell, this);

        mTitle = findViewById(R.id.title);
        mSubject = findViewById(R.id.subject);
        mIcAction = findViewById(R.id.ic_action);
    }


    private void initViews() {
        setTitle(mAttrTitle);
        setSubject(mAttrSubject);
        setOnClickListener(null);
    }


    @Override
    public void setOnClickListener(OnClickListener listener) {
        super.setOnClickListener(listener);
        if (listener == null) {
            mIcAction.setVisibility(View.GONE);
        } else {
            mIcAction.setVisibility(View.VISIBLE);
        }
    }
}
