package com.jbangit.uicomponents.slider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.jbangit.uicomponents.R;
import com.jbangit.uicomponents.common.DensityUtils;

import java.util.Collection;

public class Slider extends FrameLayout {

    private ViewPager mViewPager;

    private CountDownTimer mPlayTimer;

    private LoopPicPagerAdapter mLoopPicPagerAdapter;

    private int mTimerInterval;

    public Slider(@NonNull Context context) {
        super(context);
    }

    public Slider(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Slider(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setPics(@Nullable Collection<String> pics) {
        mLoopPicPagerAdapter.setPics(pics);
        mLoopPicPagerAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(mLoopPicPagerAdapter.getInitPosition());
    }

    public void play(int interval) {
        mTimerInterval = interval;
        mPlayTimer.start();
    }

    public void stop() {
        mTimerInterval = 0;
        mPlayTimer.cancel();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View layout = inflate(getContext(), R.layout.view_slider, this);
        mViewPager = layout.findViewById(R.id.view_pager);
        initTimer();
        initViews();
    }

    private void initTimer() {
        mPlayTimer =
                new CountDownTimer(1000, 1000) {

                    private int counter = 0;

                    @Override
                    public void onTick(long millisUntilFinished) {
                        if (counter < mTimerInterval) {
                            counter++;
                        } else {
                            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
                            counter = 0;
                        }
                    }

                    @Override
                    public void onFinish() {
                        start();
                    }
                };
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {
        addSidePagers();
        mViewPager.setPageMargin(DensityUtils.getPxFromDp(getContext(), 12));
        mLoopPicPagerAdapter = new LoopPicPagerAdapter(mViewPager);
        mViewPager.setAdapter(mLoopPicPagerAdapter);

        mViewPager.setPageTransformer(true, new PageTransformer());

        mViewPager.setOnTouchListener(
                new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                mPlayTimer.cancel();
                                break;
                            case MotionEvent.ACTION_UP:
                                if (mTimerInterval != 0) {
                                    mPlayTimer.start();
                                }
                                break;
                        }
                        return false;
                    }
                });
    }

    private void addSidePagers() {
        int sidePagersPadding = DensityUtils.getPxFromDp(getContext(), 32);
        mViewPager.setClipToPadding(false);
        mViewPager.setPadding(sidePagersPadding, 0, sidePagersPadding, 0);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mPlayTimer != null) {
            mPlayTimer.cancel();
        }
    }
}
