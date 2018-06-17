package com.jbangit.sample.cell

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.view.View
import com.jbangit.uicomponents.cell.Cell
import com.jbangit.uicomponents.cell.CellGroup
import com.jbangit.uicomponents.cell.CheckedCell
import com.jbangit.uicomponents.cell.RadioCell
import java.util.*


class CellViewModel(context: Application) : AndroidViewModel(context) {

    val toast: MutableLiveData<String> = MutableLiveData()

    val birthDate: MutableLiveData<Date> = MutableLiveData()

    init {
        birthDate.value = DATE_FORMAT.parse("1992-5-22")
    }

    fun onClick(view: View) {
        if (view is Cell) {
            toast.value = view.title.toString()
        }
    }

    fun onGroupCheckedChanged(
        cellGroup: CellGroup,
        checkedCell: CheckedCell?,
        checkedId: Int,
        uncheckedCell: CheckedCell?,
        uncheckedId: Int
    ) {
        toast.value = """
            |${cellGroup.title}:
            |the $checkedId, id ${checkedCell?.title}, checked
            |the $uncheckedId, id ${uncheckedCell?.title} unchecked
            """.trimMargin()
    }

    fun onCLickCheckGroupTitle(view: View) {
        if (view is CellGroup) {
            when (view.checkedIndex) {
                -1 -> view.checkedIndex = 0
                0 -> view.checkedIndex = 1
                1 -> view.checkedIndex = -1
            }
        }
    }

    fun onRadioChanged(radioCell: RadioCell, radio: Boolean) {
        toast.value = """
            |${radioCell.title} : is ${if (radio) "check" else "uncheck"}
            """.trimMargin()
    }

    fun updateBirthDate(date: Date) {
        birthDate.value = date
    }
}
