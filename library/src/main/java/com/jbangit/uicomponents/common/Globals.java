package com.jbangit.uicomponents.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;

import com.jbangit.uicomponents.R;

public class Globals {

    @ColorInt
    public static int getPrimaryColor(Context context) {
        TypedArray typedArray = context.obtainStyledAttributes(new int[]{R.attr.jColorPrimary});
        int color = typedArray.getColor(0, ContextCompat.getColor(context, R.color.jColorPrimary));
        typedArray.recycle();
        return color;
    }
}
