package com.jbangit.sample.cell

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

class CellFragment : Fragment() {

    private lateinit var mBinding: FragmentCellBinding

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
        val viewModel = ViewModelProviders.of(this).get(CellViewModel::class.java)
        mBinding.viewModel = viewModel
        subscribe(viewModel)
    }

    private fun subscribe(viewModel: CellViewModel) {
        viewModel.toast.observe(this, Observer(this::showToast))
    }
}

