package com.jbangit.sample.list

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jbangit.sample.R
import com.jbangit.sample.databinding.FragmentGalleryBinding
import com.jbangit.sample.showToast

const val BASE = "http://i.imgur.com/"
const val EXT = ".jpg"
val URLS = arrayListOf(
    BASE + "CqmBjo5" + EXT, BASE + "zkaAooq" + EXT, BASE + "0gqnEaY" + EXT,
    BASE + "9gbQ7YR" + EXT, BASE + "aFhEEby" + EXT, BASE + "0E2tgV7" + EXT,
    BASE + "P5JLfjk" + EXT, BASE + "nz67a4F" + EXT, BASE + "dFH34N5" + EXT,
    BASE + "FI49ftb" + EXT, BASE + "DvpvklR" + EXT, BASE + "DNKnbG8" + EXT,
    BASE + "yAdbrLp" + EXT, BASE + "55w5Km7" + EXT, BASE + "NIwNTMR" + EXT,
    BASE + "DAl0KB8" + EXT, BASE + "xZLIYFV" + EXT, BASE + "HvTyeh3" + EXT,
    BASE + "Ig9oHCM" + EXT, BASE + "7GUv9qa" + EXT, BASE + "i5vXmXp" + EXT,
    BASE + "glyvuXg" + EXT, BASE + "u6JF6JZ" + EXT, BASE + "ExwR7ap" + EXT,
    BASE + "Q54zMKT" + EXT, BASE + "9t6hLbm" + EXT, BASE + "F8n3Ic6" + EXT,
    BASE + "P5ZRSvT" + EXT, BASE + "jbemFzr" + EXT, BASE + "8B7haIK" + EXT,
    BASE + "aSeTYQr" + EXT, BASE + "OKvWoTh" + EXT, BASE + "zD3gT4Z" + EXT,
    BASE + "z77CaIt" + EXT
)

class GalleryFragment : Fragment() {
    private lateinit var mBinding: FragmentGalleryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_gallery, container, false)

        with(mBinding) {
            deleteMode.setOnClickListener {
                gallery.isDeleteMode = !gallery.isDeleteMode
            }
            addMode.setOnClickListener {
                gallery.isAddMode = !gallery.isAddMode
            }
            gallery.setOnClickAddPictureListener {
                showToast("Click add!")
                gallery.addPicture(gallery.pictures[0])
            }
            gallery.setOnClickPictureListener { gallery, position, picture ->
                showToast("$position : $picture")
            }
            reload.setOnClickListener {
                gallery.pictures = URLS
            }
            gallery.pictures = URLS
        }
        return mBinding.root
    }
}
