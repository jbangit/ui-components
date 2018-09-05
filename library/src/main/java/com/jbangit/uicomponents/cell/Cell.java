package com.jbangit.uicomponents.cell;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jbangit.uicomponents.R;

public class Cell extends FrameLayout {

    private ImageView mIcon;

    private TextView mTitle;

    private TextView mSubject;

    private String mAttrTitle;

    private String mAttrSubject;

    private ImageView mIcAction;

    private Drawable mAttrIcon;

    private boolean mAttrFitIcon = false;

    private boolean mAttrShowAction = true;

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
        mAttrFitIcon = typedArray.getBoolean(R.styleable.Cell_cellFitIcon, false);
        mAttrIcon = typedArray.getDrawable(R.styleable.Cell_cellIcon);
        mAttrShowAction = typedArray.getBoolean(R.styleable.Cell_cellShowAction, mAttrShowAction);

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

    public Drawable getIcon() {
        return mIcon.getDrawable();
    }

    public void setIcon(Drawable icon) {
        if (icon == null) {
            if (mAttrFitIcon) {
                mIcon.setVisibility(View.VISIBLE);
            } else {
                mIcon.setVisibility(View.GONE);
            }
        } else {
            mIcon.setImageDrawable(icon);
            mIcon.setVisibility(View.VISIBLE);
        }
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
        mIcon = findViewById(R.id.icon);
    }

    private void initViews() {
        setTitle(mAttrTitle);
        setSubject(mAttrSubject);
        setIcon(mAttrIcon);
        setOnClickListener(null);
    }

    @Override
    public void setOnClickListener(OnClickListener listener) {
        super.setOnClickListener(listener);
        if (listener == null || !mAttrShowAction) {
            mIcAction.setVisibility(View.GONE);
        } else {
            mIcAction.setVisibility(View.VISIBLE);
        }
    }

    @BindingAdapter("cellTitle")
    public static void setTitle(Cell cell, CharSequence title) {
        cell.setTitle(title);
    }

    @BindingAdapter("cellSubject")
    public static void setSubject(Cell cell, CharSequence subject) {
        cell.setSubject(subject);
    }

    @BindingAdapter("cellIcon")
    public static void setIcon(Cell cell, Drawable drawable) {
        cell.setIcon(drawable);
    }
}
