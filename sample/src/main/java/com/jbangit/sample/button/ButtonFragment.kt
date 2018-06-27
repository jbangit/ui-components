package com.jbangit.sample.button

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jbangit.sample.R
import com.jbangit.sample.databinding.FragmentButtonBinding
import com.jbangit.sample.showToast

class ButtonFragment : Fragment() {
    private lateinit var binding: FragmentButtonBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_button, container, false)
        binding.view = this
        return binding.root
    }

    fun onClick(view: View) {
        showToast(view.id.toString())
    }
}
