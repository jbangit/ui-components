package com.jbangit.sample.list

import com.jbangit.sample.common.MultipleFragmentActivity

class ListActivity : MultipleFragmentActivity() {
    override val fragments = arrayListOf(
        GalleryFragment(),
        GridRadioGroupFragment()
    )
}
