package com.example.footballnewsmanager.helpers;

import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    @androidx.databinding.BindingAdapter("clear_focus")
    public static void setClearFocus(EditText editText, boolean focus){
        if(focus){
            editText.clearFocus();
        }
    }

    @androidx.databinding.BindingAdapter("setEnabled")
    public static void setEnabled(Button button, boolean enabled){
        button.setEnabled(enabled);
    }

    @androidx.databinding.BindingAdapter("marginBottom")
    public static void setLayoutMarginBottom(View view, int value) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.bottomMargin = value;
    }

    @androidx.databinding.BindingAdapter("marginStart")
    public static void setLayoutMarginStart(View view, int value) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.leftMargin = value;
    }
}
