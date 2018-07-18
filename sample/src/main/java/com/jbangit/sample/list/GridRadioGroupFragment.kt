package com.jbangit.sample.list

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jbangit.sample.R
import com.jbangit.sample.databinding.FragmentGridRadioFragmentBinding

class GridRadioGroupFragment : Fragment() {
    private lateinit var mBinding: FragmentGridRadioFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
                DataBindingUtil.inflate(
                    inflater,
                    R.layout.fragment_grid_radio_fragment,
                    container,
                    false
                )

        mBinding.gridRadioGroup.setItem(
            arrayListOf(
                "One",
                "Two",
                "Three",
                "Long Long Long"
            )
        )

        mBinding.gridRadioGroup.check(0)

        return mBinding.root
    }
}