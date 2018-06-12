package com.jbangit.sample

import android.support.v4.app.Fragment
import android.widget.Toast

fun Fragment.showToast(toast: String?) {
    Toast.makeText(context, toast, Toast.LENGTH_LONG).show()
}
