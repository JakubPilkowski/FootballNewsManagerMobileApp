package com.example.footballnewsmanager.helpers;

import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class BindingAdapter {


    @androidx.databinding.BindingAdapter("viewpager_adapter")
    public static void setViewpagerAdapter(ViewPager viewPager, PagerAdapter pagerAdapter){
        viewPager.setAdapter(pagerAdapter);
    }

    @androidx.databinding.BindingAdapter("add_text_watcher")
    public static void addTextWatcher(EditText editText, TextWatcher textWatcher){
        editText.addTextChangedListener(textWatcher);
    }

    @androidx.databinding.BindingAdapter("set_editor_action_listener")
    public static void setEditorActionListener(EditText editText, TextView.OnEditorActionListener listener)
    {
        editText.setOnEditorActionListener(listener);
    }
}
