package com.jbangit.uicomponents.slider;

import android.support.v4.view.ViewPager;
import android.widget.Scroller;

import java.lang.reflect.Field;

class ViewPagerScroller extends Scroller {

    private int mScrollDuration = 1200;

    ViewPagerScroller(ViewPager viewPager) {
        super(viewPager.getContext());
        bind(viewPager);
    }

    private void bind(ViewPager viewPager) {
        try {
            Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            scrollerField.set(viewPager, this);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mScrollDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mScrollDuration);
    }

    public int getScrollDuration() {
        return mScrollDuration;
    }

    public void setScrollDuration(int scrollDuration) {
        mScrollDuration = scrollDuration;
    }
}
