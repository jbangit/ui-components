package com.jbangit.sample.common

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.jbangit.sample.R

abstract class MultipleFragmentActivity : AppCompatActivity() {
    protected abstract val fragments: List<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiple)

        findViewById<ViewPager>(R.id.container).adapter =
                object : FragmentPagerAdapter(supportFragmentManager) {
                    override fun getCount(): Int = fragments.size
                    override fun getItem(position: Int): Fragment = fragments[position]
                }
    }
}
