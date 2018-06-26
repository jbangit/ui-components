package com.jbangit.uicomponents.slider;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

class PageTransformer implements ViewPager.PageTransformer {

    @Override
    public void transformPage(@NonNull View page, float position) {
        if (position > 1) {
            position = 1;
        }
        if (position < -1) {
            position = -1;
        }

        long mVSmallSizeItemPadding = Math.round(page.getHeight() * 0.1);
        if (position > 0) {
            int vPadding = Math.round(position * mVSmallSizeItemPadding);
            page.setPadding(0, vPadding, 0, vPadding);
        } else if (position < 0) {
            position = -position;
            int vPadding = Math.round(position * mVSmallSizeItemPadding);
            page.setPadding(0, vPadding, 0, vPadding);
        } else {
            page.setPadding(0, 0, 0, 0);
        }
    }
}
