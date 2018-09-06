package com.jbangit.uicomponents.tabbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jbangit.uicomponents.R;
import com.jbangit.uicomponents.badge.Badge;
import com.jbangit.uicomponents.common.Globals;

/**
 * If selected icon is not set, change icon by tint color between checked and unchecked state
 * <p>
 * <p>{@link TabBarItem#setTitle(CharSequence)}
 * <p>{@link TabBarItem#getBadge()}
 */
public class TabBarItem extends FrameLayout implements Checkable {

    private String mAttrTitle;

    private Drawable mAttrIcon;

    private Drawable mAttrSelectedIcon;

    @ColorInt
    private int mAttrCheckedTextColor = getContext().getResources().getColor(R.color.colorTextDark);

    private int mAttrUncheckedTextColor = getContext().getResources().getColor(R.color.colorTextGray);

    private TextView mTitle;

    private ImageView mIcon;

    private Badge mBadge;

    private boolean mChecked = false;

    private View mLayout;

    public TabBarItem(@NonNull Context context) {
        super(context);
    }

    public TabBarItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TabBarItem);
        mAttrTitle = typedArray.getString(R.styleable.TabBarItem_tabBarItemTitle);
        mAttrIcon = typedArray.getDrawable(R.styleable.TabBarItem_tabBarItemIcon);
        mAttrSelectedIcon = typedArray.getDrawable(R.styleable.TabBarItem_tabBarItemSelectedIcon);

        if (mAttrSelectedIcon == null) {
            mAttrIcon.mutate();
            mAttrSelectedIcon = typedArray.getDrawable(R.styleable.TabBarItem_tabBarItemIcon);

            DrawableCompat.setTint(mAttrIcon, context.getResources().getColor(R.color.colorTextGray));
            DrawableCompat.setTint(mAttrSelectedIcon, Globals.getPrimaryColor(context));
        }

        typedArray.recycle();
    }

    public TabBarItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        inflateLayout();
        initViews();
    }

    private void inflateLayout() {
        mLayout = inflate(getContext(), R.layout.view_item_tab_bar, this);
        mTitle = mLayout.findViewById(R.id.title);
        mIcon = mLayout.findViewById(R.id.icon);
        mBadge = mLayout.findViewById(R.id.badge);
    }

    private void initViews() {
        setTitle(mAttrTitle);
        setChecked(false);

        mLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getParent() instanceof TabBar) {
                    ((TabBar) getParent()).select(TabBarItem.this, true);
                }
            }
        });
    }

    public CharSequence getTitle() {
        return mTitle.getText();
    }

    public void setTitle(CharSequence title) {
        mTitle.setText(title);
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;
        showChecked();
    }

    private void showChecked() {
        if (mChecked) {
            mIcon.setImageDrawable(mAttrSelectedIcon);
            mTitle.setTextColor(mAttrCheckedTextColor);
        } else {
            mIcon.setImageDrawable(mAttrIcon);
            mTitle.setTextColor(mAttrUncheckedTextColor);
        }
    }

    @Override
    public void toggle() {
        mChecked = !mChecked;
        showChecked();
    }

    public Badge getBadge() {
        return mBadge;
    }
}
