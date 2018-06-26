package com.jbangit.uicomponents.badge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jbangit.uicomponents.R;

/**
 * Can be used as a layout
 * <p>
 * <P>{@link Badge#setNumber(int)}
 */
public class Badge extends FrameLayout {

    public static final int INFINITE = -1;

    private TextView mBadge;

    private ImageView mBadgeInfinite;

    private int mNumber = 0;

    public Badge(@NonNull Context context) {
        super(context);
    }

    public Badge(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Badge(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        addBadgeView();
        initViews();
    }

    private void addBadgeView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_badge, this, false);
        LayoutParams lp =
                new LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.END | Gravity.TOP;
        view.setLayoutParams(lp);
        addView(view);

        mBadge = view.findViewById(R.id.badge);
        mBadgeInfinite = view.findViewById(R.id.badge_infinite);
    }

    private void initViews() {
        showNumber();
    }

    @SuppressLint("SetTextI18n")
    private void showNumber() {
        if (mNumber == 0) {
            mBadge.setVisibility(View.GONE);
            mBadgeInfinite.setVisibility(View.GONE);
        } else if (mNumber == Badge.INFINITE) {
            mBadge.setVisibility(View.GONE);
            mBadgeInfinite.setVisibility(View.VISIBLE);
        } else if (mNumber > 99) {
            mBadge.setVisibility(View.VISIBLE);
            mBadge.setText(String.valueOf(99) + "+");
            mBadgeInfinite.setVisibility(View.GONE);
        } else {
            mBadge.setVisibility(View.VISIBLE);
            mBadge.setText(String.valueOf(mNumber));
            mBadgeInfinite.setVisibility(View.GONE);
        }
    }

    public int getNumber() {
        return mNumber;
    }

    public void setNumber(int number) {
        mNumber = number;
        showNumber();
    }
}
