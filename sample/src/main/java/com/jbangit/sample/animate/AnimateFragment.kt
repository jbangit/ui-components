package com.jbangit.sample.animate

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jbangit.sample.R
import com.jbangit.sample.databinding.FragmentAnimateBinding
import com.jbangit.sample.showToast

class AnimateFragment : Fragment(), View.OnClickListener {

    private lateinit var mBinding: FragmentAnimateBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_animate, container, false
        )
        mBinding.view = this
        return mBinding.root
    }

    override fun onClick(v: View?) {
        showToast(v?.id.toString())
        mBinding.dragLayout.collapse()
    }
}
