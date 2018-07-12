package com.jbangit.sample

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jbangit.sample.actionsheet.ActionSheetActivity
import com.jbangit.sample.animate.AnimateActivity
import com.jbangit.sample.assidebar.AsideBarActivity
import com.jbangit.sample.button.ButtonActivity
import com.jbangit.sample.cell.CellActivity
import com.jbangit.sample.databinding.FragmentMainBinding
import com.jbangit.sample.list.ListActivity
import com.jbangit.sample.slider.SliderActivity
import com.jbangit.sample.spinner.SpinnerActivity
import com.jbangit.sample.tab.TabActivity

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
            R.id.action_sheet -> {
                startActivity(
                    Intent(context, ActionSheetActivity::class.java)
                )
            }
            R.id.nav_spinner -> {
                startActivity(
                    Intent(context, SpinnerActivity::class.java)
                )
            }
            R.id.slider -> {
                startActivity(
                    Intent(context, SliderActivity::class.java)
                )
            }
            R.id.button -> {
                startActivity(
                    Intent(context, TabActivity::class.java)
                )
            }
            R.id.animate -> {
                startActivity(
                    Intent(context, AnimateActivity::class.java)
                )
            }
            R.id.nav_tab -> {
                startActivity(
                    Intent(context, ButtonActivity::class.java)
                )
            }
            R.id.nav_list -> {
                startActivity(
                    Intent(context, ListActivity::class.java)
                )
            }
        }
    }
}