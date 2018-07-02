package com.jbangit.uicomponents.common;

import android.content.Context;

public class DensityUtils {

    public static int getPxFromDp(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public static int getPxFromSp(Context context, int sp) {
        float density = context.getResources().getDisplayMetrics().scaledDensity;
        return Math.round(sp * density);
    }
}
