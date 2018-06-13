package com.jbangit.sample.cell

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.view.View
import com.jbangit.uicomponents.cell.Cell
import com.jbangit.uicomponents.cell.CellGroup
import com.jbangit.uicomponents.cell.CheckedCell

class CellViewModel(context: Application) : AndroidViewModel(context) {

    val toast: MutableLiveData<String> = MutableLiveData()

    fun onClick(view: View) {
        if (view is Cell) {
            toast.value = view.title.toString()
        }
    }

    fun onGroupCheckedChanged(
        cellGroup: CellGroup,
        checkedCell: CheckedCell,
        checkedId: Int,
        uncheckedCell: CheckedCell,
        uncheckedId: Int
    ) {
        toast.value = """
            |${cellGroup.title}:
            |the ${checkedId}, id ${checkedCell.title}, checked
            |the ${uncheckedId}, id ${uncheckedCell.title} unchecked
            """.trimMargin()
    }
}
