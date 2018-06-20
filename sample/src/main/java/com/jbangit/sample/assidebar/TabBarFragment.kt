package com.jbangit.sample.assidebar

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jbangit.sample.R
import com.jbangit.sample.databinding.FragmentTabBarBinding
import com.jbangit.sample.showToast
import com.jbangit.uicomponents.badge.Badge
import com.jbangit.uicomponents.tabbar.TabBar
import kotlinx.android.synthetic.main.fragment_tab_bar.view.*

class TabBarFragment : Fragment() {
    private lateinit var mBinding: FragmentTabBarBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.fragment_tab_bar, container, false)

        mBinding.tabBar.onTabChangeListener =
                TabBar.OnTabChangeListener { oldItem, oldPosition, newItem, newPosition ->
                    var toast = """
                        from $oldPosition ${oldItem?.title
                            ?: "null"} to $newPosition ${newItem.title}
                    """.trimIndent()

                    if (oldPosition == 1 && newPosition == 2) {
                        toast += "\nInterrupt!"
                        mBinding.tabBar.sms.badge.number++
                        showToast(toast)
                        false
                    } else {
                        showToast(toast)
                        true
                    }
                }

        mBinding.viewPager.adapter = object : FragmentPagerAdapter(fragmentManager) {
            val fragments = Array(4) { i ->
                val fragment = TabBarPagerFragment()
                val arguments = Bundle()
                arguments.putString(TabBarPagerFragment.KEY_TITLE, i.toString())
                fragment.arguments = arguments
                fragment
            }

            override fun getItem(position: Int): Fragment {
                return fragments[position]
            }

            override fun getCount(): Int {
                return fragments.size
            }
        }

        mBinding.tabBar.setupWithViewPager(mBinding.viewPager)

        with(mBinding) {
            home.badge.number = 0
            near.badge.number = 1
            sms.badge.number = 95
            user.badge.number = Badge.INFINITE
        }

        return mBinding.root
    }
}
