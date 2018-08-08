package com.jbangit.sample.tab

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jbangit.sample.R
import com.jbangit.sample.databinding.FragmentTabBinding
import com.jbangit.uicomponents.tab.ViewTab

class TabFragment : Fragment() {

    private lateinit var mBinding: FragmentTabBinding

    private var tabNumber = 0

    private val tabAdapter = object : ViewTab.ViewTabAdapter {
        override fun getItemView(container: ViewGroup, position: Int): View {
            return LayoutInflater.from(container.context).inflate(
                R.layout.view_item_tab_fragment_tab, container, false
            )
        }

        override fun onSelected(item: View, position: Int, selected: Boolean) {
            item.findViewById<TextView>(R.id.title)
                ?.setTextColor(if (selected) 0xFFFF00FF.toInt() else 0xFFFF0000.toInt())
        }

        override fun getCount() = tabNumber
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_tab, container, false
        )
        mBinding.viewTab.adapter = tabAdapter

        mBinding.viewTab.holderView =
                LayoutInflater.from(context).inflate(
                    R.layout.view_item_tab_fragment_tab, container, false
                )

        mBinding.viewTab.emptyView =
                LayoutInflater.from(context).inflate(
                    R.layout.view_empty_tab_fragment_tab, container, false
                )

        mBinding.runningTimeSetTab.setTitles(
            listOf(
                "one", "two", "three", "four"
            )
        )

        mBinding.setNull.setOnClickListener { mBinding.viewTab.setCurrentItem(-1) }
        mBinding.addTab.setOnClickListener {
            tabNumber++
            mBinding.viewTab.adapter = tabAdapter
        }

        return mBinding.root
    }
}
