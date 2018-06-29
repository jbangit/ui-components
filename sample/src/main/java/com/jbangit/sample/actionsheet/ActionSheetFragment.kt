package com.jbangit.sample.actionsheet

import android.app.Activity
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jbangit.sample.R
import com.jbangit.sample.databinding.FragmentActionSheetBinding
import com.jbangit.sample.showToast
import com.jbangit.uicomponents.common.fragment.OnFragmentResultListener
import com.jbangit.uicomponents.dialog.ActionSheet
import com.jbangit.uicomponents.dialog.MultipleChoiceDialog

private const val REQUEST_ACTION_SHEET = 100
private const val REQUEST_MULTIPLE_CHOICE = 101

private val ACTIONS = arrayListOf("示例菜单1", "示例菜单2", "示例菜单3", "示例菜单4")
private val CHOICES = arrayListOf(
    "嘻哈", "流行", "摇滚", "民谣",
    "电子", "舞曲", "乡村", "金属",
    "爵士", "轻音乐", "拉丁", "R&B/SOUL",
    "朋克", "另类/独立", "雷鬼", "后摇"
)

class ActionSheetFragment : Fragment(), OnFragmentResultListener {

    private lateinit var mBinding: FragmentActionSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_action_sheet, container, false)
        with(mBinding) {
            actionSheet.setOnClickListener {
                ActionSheet.show(
                    this@ActionSheetFragment,
                    REQUEST_ACTION_SHEET,
                    ACTIONS
                )
            }

            multipleChoice.setOnClickListener {
                MultipleChoiceDialog.show(
                    this@ActionSheetFragment,
                    REQUEST_MULTIPLE_CHOICE,
                    CHOICES
                )
            }
        }
        return mBinding.root
    }

    override fun onFragmentResult(fragment: Fragment, requestCode: Int, resultCode: Int) {
        when (requestCode) {
            REQUEST_ACTION_SHEET -> when (resultCode) {
                Activity.RESULT_OK -> showToast(
                    """
                    ${(fragment as ActionSheet).actionIndex} : ${fragment.action}
                """.trimIndent()
                )
            }
            REQUEST_MULTIPLE_CHOICE -> when (resultCode) {
                Activity.RESULT_OK -> showToast(
                    """
                    ${(fragment as MultipleChoiceDialog).choicesIndexes} : ${fragment.chosenItem}
                """.trimIndent()
                )
            }
        }
    }

}
