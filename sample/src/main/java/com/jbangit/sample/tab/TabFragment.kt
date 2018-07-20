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
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_tab, container, false
        )
        mBinding.viewTab.adapter = object : ViewTab.ViewTabAdapter {
            override fun getItemView(container: ViewGroup, position: Int): View {
                return LayoutInflater.from(container.context).inflate(
                    R.layout.view_item_tab_fragment_tab, container, false
                )
            }

            override fun onSelected(item: View, position: Int, selected: Boolean) {
                item.findViewById<TextView>(R.id.title)
                    ?.setTextColor(if (selected) 0xFFFF00FF.toInt() else 0xFFFF0000.toInt())
            }

            override fun getCount() = 4
        }

        mBinding.viewTab.setCurrentItem(2)

        mBinding.runningTimeSetTab.setTitles(
            listOf(
                "one", "two", "three", "four"
            )
        )

        return mBinding.root
    }
}
