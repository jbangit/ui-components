package com.jbangit.uicomponents.common.viewgroup.scrollhelper;

import android.view.MotionEvent;
import android.view.ViewGroup;

public interface ScrollHelper {

    boolean onViewGroupTouchEvent(MotionEvent event);

    boolean onViewGroupInterceptTouchEvent(MotionEvent event);

    void onDetachedFromWindow();

    void onViewGroupComputeScroll();

    void setViewGroupHelper(ViewGroupHelper viewGroupHelper);


    interface ViewGroupHelper {

        ViewGroup getViewGroup();
    }
}
