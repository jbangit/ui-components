package com.jbangit.uicomponents.common;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
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

    public static CharSequence getDialogButtonOk(Context context) {
        TypedArray typedArray = context.obtainStyledAttributes(new int[]{R.attr.jDialogButtonOk});
        CharSequence ok = typedArray.getText(0);
        if (ok == null) {
            ok = context.getText(android.R.string.ok);
        }
        typedArray.recycle();
        return ok;
    }

    @ColorInt
    public static int getDialogButtonOkColor(Context context) {
        TypedArray typedArray = context.obtainStyledAttributes(new int[]{R.attr.jDialogButtonOkColor});
        int color = typedArray.getColor(0, getPrimaryColor(context));
        typedArray.recycle();
        return color;
    }

    public static CharSequence getDialogButtonCancel(Context context) {
        TypedArray typedArray = context.obtainStyledAttributes(new int[]{R.attr.jDialogButtonCancel});
        CharSequence ok = typedArray.getText(0);
        if (ok == null) {
            ok = context.getText(android.R.string.cancel);
        }
        typedArray.recycle();
        return ok;
    }

    @ColorInt
    public static int getDialogButtonCancelColor(Context context) {
        TypedArray typedArray = context.obtainStyledAttributes(new int[]{R.attr.jDialogButtonCancelColor});
        int color = typedArray.getColor(0, getPrimaryColor(context));
        typedArray.recycle();
        return color;
    }

    // TODO: 2018/6/27 refactor all ripple drawable
    public static Drawable addRipple(Context context, Drawable background) {
        return addRipple(context, background, background);
    }

    public static Drawable addRipple(Context context, Drawable background, Drawable mask) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            int rippleColor = context.getResources().getColor(R.color.colorTextDark);
            return new RippleDrawable(ColorStateList.valueOf(rippleColor), background, mask);
        }
        return background;
    }
}
