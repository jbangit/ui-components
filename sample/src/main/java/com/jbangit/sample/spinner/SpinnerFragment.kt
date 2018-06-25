package com.jbangit.sample.spinner

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jbangit.sample.R
import com.jbangit.sample.databinding.FragmentSpinnerBinding
import com.jbangit.sample.showToast
import com.jbangit.uicomponents.spinner.Spinner

class SpinnerFragment : Fragment() {
    private lateinit var mBinding: FragmentSpinnerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_spinner, container, false)

        mBinding.spinner.onSpinnerChangeListener = Spinner.OnSpinnerChangeListener { _, number ->
            showToast(number.toString())
        }

        return mBinding.root
    }
}
