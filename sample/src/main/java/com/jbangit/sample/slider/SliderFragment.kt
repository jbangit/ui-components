package com.jbangit.sample.slider

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jbangit.sample.R
import com.jbangit.sample.databinding.FragmentSliderBinding

class SliderFragment : Fragment() {
    private lateinit var mBinding: FragmentSliderBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_slider, container, false)
        mBinding.slider.setPics(
            arrayListOf(
                "https://cdn.pixabay.com/photo/2018/06/10/00/11/seagull-3465550_1280.jpg",
                "https://cdn.pixabay.com/photo/2016/06/02/21/59/fall-1432252_1280.jpg",
                "https://cdn.pixabay.com/photo/2018/01/28/11/24/nature-3113318_1280.jpg"
            )
        )
        mBinding.slider.play(5)
        return mBinding.root
    }
}
