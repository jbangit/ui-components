package com.jbangit.uicomponents.common.dialog;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

/**
 * Implements this interface to receive fragment result
 */
public interface OnFragmentResultListener {
    void onFragmentResult(@NonNull Fragment fragment, int requestCode, int resultCode);
}
