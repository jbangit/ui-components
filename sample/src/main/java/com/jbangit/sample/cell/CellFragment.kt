package com.jbangit.sample.cell

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jbangit.sample.R
import com.jbangit.sample.databinding.FragmentCellBinding
import com.jbangit.sample.showToast
import com.jbangit.uicomponents.common.fragment.OnFragmentResultListener
import com.jbangit.uicomponents.dialog.DatePickerDialog
import com.jbangit.uicomponents.dialog.OptionDialog

private const val REQUEST_BIRTH_DATE = 1
private const val REQUEST_SEXUAL = 2

class CellFragment : Fragment(), OnFragmentResultListener {

    private lateinit var mBinding: FragmentCellBinding
    private lateinit var mViewModel: CellViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_cell, container, false
        )
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProviders.of(activity!!).get(CellViewModel::class.java)
        mBinding.viewModel = mViewModel
        mBinding.view = this
        subscribe(mViewModel)
    }

    private fun subscribe(viewModel: CellViewModel) {
        viewModel.toast.observe(this, Observer(this::showToast))
        viewModel.birthDate.observe(this, Observer { it ->
            mBinding.birthDate.subject = DATE_FORMAT.format(it)
        })
        viewModel.sexual.observe(this, Observer {
            mBinding.sexual.subject = SEXUAL[it!!]
        })
    }

    fun chooseBirthDate(view: View) {
        DatePickerDialog.show(this, REQUEST_BIRTH_DATE, null, null, mViewModel.birthDate.value)
    }

    fun chooseSexual(view: View) {
        OptionDialog.show(this, REQUEST_SEXUAL, SEXUAL.asList(), mViewModel.sexual.value!!)
    }

    override fun onFragmentResult(fragment: Fragment, requestCode: Int, resultCode: Int) {
        when (requestCode) {
            REQUEST_BIRTH_DATE -> when (resultCode) {
                Activity.RESULT_OK -> mViewModel.updateBirthDate((fragment as DatePickerDialog).date)
            }
            REQUEST_SEXUAL -> when (resultCode) {
                Activity.RESULT_OK -> mViewModel.updateSexual((fragment as OptionDialog).index)
            }
        }
    }
}

