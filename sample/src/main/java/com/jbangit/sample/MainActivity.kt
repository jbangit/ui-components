package com.jbangit.sample

import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jbangit.sample.cell.CellFragment
import com.jbangit.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            supportActionBar?.elevation = 0f
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        var fragment = supportFragmentManager.findFragmentById(R.id.content)

        if (fragment == null) {
            fragment = CellFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.content, fragment)
                .commit()
        }

    }

}
