package com.example.footballnewsmanager.helpers;

import android.content.Context;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.footballnewsmanager.models.LayoutManager;

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

    @androidx.databinding.BindingAdapter("recyclerViewAdapter")
    public static void setRecyclerViewAdapter(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    @androidx.databinding.BindingAdapter("layoutManager")
    public static void setLayoutManager(RecyclerView recyclerView, LayoutManager layoutManager) {
        switch (layoutManager) {
            case LINEAR:
                recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
                break;
            case GRID:
                recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), 2));
                break;
        }
    }

    @androidx.databinding.BindingAdapter("imageUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        Context context = imageView.getContext();
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.1f)
                .into(imageView);
    }
}
