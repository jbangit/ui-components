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
import com.jbangit.uicomponents.common.dialog.OnFragmentResultListener
import com.jbangit.uicomponents.dialog.ActionSheet

private const val REQUEST_ACTION_SHEET = 100
private val ACTIONS = arrayListOf("示例菜单1", "示例菜单2", "示例菜单3", "示例菜单4")

class ActionSheetFragment : Fragment(), OnFragmentResultListener {

    private lateinit var mBinding: FragmentActionSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_action_sheet, container, false)
        mBinding.actionSheet.setOnClickListener {
            ActionSheet.show(
                this@ActionSheetFragment,
                REQUEST_ACTION_SHEET,
                ACTIONS
            )
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
        }
    }

}
