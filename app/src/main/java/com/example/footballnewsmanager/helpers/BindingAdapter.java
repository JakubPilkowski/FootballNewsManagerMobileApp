package com.example.footballnewsmanager.helpers;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class BindingAdapter {


    @androidx.databinding.BindingAdapter("viewpager_adapter")
    public static void setViewpagerAdapter(ViewPager viewPager, PagerAdapter pagerAdapter){
        viewPager.setAdapter(pagerAdapter);
    }
}
