package com.jbangit.uicomponents.slider;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jbangit.uicomponents.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

class LoopPicPagerAdapter extends PagerAdapter {

    private List<String> mPics = Collections.emptyList();

    private ViewPager mViewPager;

    LoopPicPagerAdapter(ViewPager viewPager) {
        mViewPager = viewPager;
    }

    @Override
    public int getCount() {
        return mPics.size() == 0 ? 0 : Integer.MAX_VALUE;
    }

    @NonNull
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView =
                LayoutInflater.from(container.getContext())
                        .inflate(R.layout.view_item_slider, container, false);
        container.addView(itemView);
        Glide.with(container.getContext())
                .load(getPic(position))
                .into((ImageView) itemView.findViewById(R.id.pic));
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    private String getPic(int position) {
        return mPics.get(getPicIndex(position));
    }

    private int getPicIndex(int position) {
        return (position % mPics.size());
    }

    void setPics(@Nullable Collection<String> pics) {
        if (pics == null) {
            mPics = Collections.emptyList();
        } else {
            mPics = new ArrayList<>(pics);
        }
    }

    public int getInitPosition() {
        if (mPics.size() == 0) {
            return 0;
        } else {
            return Integer.MAX_VALUE / mPics.size() / 2;
        }

    }
}
