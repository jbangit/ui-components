package com.jbangit.sample.cell

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.view.View
import com.jbangit.uicomponents.cell.Cell

class CellViewModel(context: Application) : AndroidViewModel(context) {

    val toast: MutableLiveData<String> = MutableLiveData()

    fun onClick(view: View) {
        if (view is Cell) {
            toast.value = view.title.toString()
        }
    }
}
