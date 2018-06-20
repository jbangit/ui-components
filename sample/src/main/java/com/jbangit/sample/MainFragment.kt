package com.jbangit.sample

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jbangit.sample.assidebar.AsideBarActivity
import com.jbangit.sample.cell.CellActivity
import com.jbangit.sample.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var mBinding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_main,
            container,
            false
        )
        mBinding.view = this

        return mBinding.root
    }

    fun onClickNavGridItem(view: View) {
        when (view.id) {
            R.id.nav_cell -> {
                startActivity(
                    Intent(context, CellActivity::class.java)
                )
            }
            R.id.aside_bar -> {
                startActivity(
                    Intent(context, AsideBarActivity::class.java)
                )
            }
        }
    }
}