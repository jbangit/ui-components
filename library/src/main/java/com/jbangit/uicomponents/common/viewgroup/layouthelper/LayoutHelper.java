package com.jbangit.uicomponents.common.viewgroup.layouthelper;

import android.view.View;
import android.view.ViewGroup;

public interface LayoutHelper {

    /**
     * invoke method in {@link ViewGroup#onMeasure(int, int)}
     */
    void onViewGroupMeasure(int widthMeasureSpec, int heightMeasureSpec);

    /**
     * invoke method in {@link ViewGroup#onLayout(boolean, int, int, int, int)}
     */
    void onViewGroupLayout(boolean changed, int l, int t, int r, int b);

    interface ViewGroupHelper {

        /**
         * @return which view you want to layout
         */
        View getChildView(int index);

        /**
         * @return how many group you want to layout
         */
        int getChildCount();

        /**
         * Invoke {@link ViewGroup#setMeasuredDimension(int, int)}
         */
        void onSetMeasuredDimension(int measuredWidth, int measuredHeight);
    }
}
