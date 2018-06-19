package com.jbangit.sample.common

import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.jbangit.sample.R
import com.jbangit.sample.databinding.ActivitySingleFragmentBinding

abstract class SingleFragmentActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivitySingleFragmentBinding

    protected abstract val fragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            supportActionBar?.elevation = 0f
        }

        mBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_single_fragment
        )

        var fragment = supportFragmentManager.findFragmentById(R.id.content)

        if (fragment == null) {
            fragment = this.fragment
            supportFragmentManager.beginTransaction()
                .add(R.id.content, fragment)
                .commit()
        }
    }
}
