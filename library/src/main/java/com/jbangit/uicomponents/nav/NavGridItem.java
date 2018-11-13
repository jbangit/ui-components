package com.jbangit.uicomponents.nav;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jbangit.uicomponents.R;
import com.jbangit.uicomponents.common.DensityUtils;

public class NavGridItem extends FrameLayout {

    private TextView mTitle;

    private ImageView mIcon;

    private String mAttrTitle;

    private Drawable mAttrIcon;
    private int mAttrTextSize;

    public NavGridItem(Context context) {
        super(context);
    }

    public NavGridItem(Context context,
                       @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NavGridItem);
        mAttrTitle = typedArray.getString(R.styleable.NavGridItem_navGridItemTitle);
        mAttrIcon = typedArray.getDrawable(R.styleable.NavGridItem_navGridItemIcon);
        mAttrTextSize = typedArray.getDimensionPixelOffset(R.styleable.NavGridItem_navGridItemTextSize, DensityUtils.getPxFromSp(context, 14));
        typedArray.recycle();
    }

    public NavGridItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        mIcon.setImageDrawable(icon);
    }

    private void setTextSize(int textSize) {
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        inflateContent();
        initViews();
    }

    private void inflateContent() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_item_nav_grid, this);
        mTitle = view.findViewById(R.id.title);
        mIcon = view.findViewById(R.id.icon);
    }

    private void initViews() {
        setTitle(mAttrTitle);
        setIcon(mAttrIcon);
        setTextSize(mAttrTextSize);
    }
}
