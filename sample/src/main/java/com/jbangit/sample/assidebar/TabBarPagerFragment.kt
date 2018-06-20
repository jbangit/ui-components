package com.jbangit.sample.assidebar

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jbangit.sample.R
import com.jbangit.sample.databinding.FragmentTabBarPagerBinding

class TabBarPagerFragment : Fragment() {
    private lateinit var mBinding: FragmentTabBarPagerBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_tab_bar_pager, container, false)
        mBinding.title.text = arguments?.getCharSequence(KEY_TITLE)
        return mBinding.root
    }

    companion object {
        const val KEY_TITLE = "KEY_TITLE"
    }
}
