package com.example.footballnewsmanager.helpers;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.caverock.androidsvg.SVG;
import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.interfaces.DragViewListener;
import com.example.footballnewsmanager.models.LayoutManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
    public static void setEnabled(View view, boolean enabled){
        view.setEnabled(enabled);
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
    public static void setLayoutManager(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
    }

    @androidx.databinding.BindingAdapter("imageUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        Context context = imageView.getContext();
        if(url.contains(".svg")){
            RequestBuilder<PictureDrawable> requestBuilder;
            Uri uri = Uri.parse(url);
            Log.d("Glide", "svg");
            requestBuilder = Glide.with(context)
                    .as(PictureDrawable.class)
                    .listener(new SvgSoftwareLayerSetter());
            requestBuilder
                    .placeholder(R.drawable.image_placeholer)
                    .error(R.drawable.image_error)
                    .load(uri)
                    .into(imageView);
        }
        else{
            Log.d("Glide", "other");
            Glide.with(context)
                    .load(url)
                    .placeholder(R.drawable.image_placeholer)
                    .error(R.drawable.image_error)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.1f)
                    .into(imageView);
        }
    }

    @androidx.databinding.BindingAdapter("switchChangeListener")
    public static void setSwitchChangeListener(Switch switchView, Switch.OnCheckedChangeListener listener){
        switchView.setOnCheckedChangeListener(listener);
    }

    @androidx.databinding.BindingAdapter("dragViewListener")
    public static void setDragViewListener(DragView dragView, DragViewListener listener){
        dragView.setDragViewListener(listener);
    }

    @androidx.databinding.BindingAdapter("drawableAsResource")
    public static void setViewDrawable(View view, int resource){
        view.setBackground(view.getResources().getDrawable(resource));
    }

    @androidx.databinding.BindingAdapter("viewpager2Adapter")
    public static void setViewPager2Adapter(ViewPager2 viewPager2, RecyclerView.Adapter adapter){
        viewPager2.setAdapter(adapter);
    }

    @androidx.databinding.BindingAdapter("pageChangeCallback")
    public static void setPageChangeCallback(ViewPager2 viewPager2, ViewPager2.OnPageChangeCallback callback){
        viewPager2.registerOnPageChangeCallback(callback);
    }

    @androidx.databinding.BindingAdapter("itemSelectedListener")
    public static void setItemSelectedListener(BottomNavigationView bottomNavigationView, BottomNavigationView.OnNavigationItemSelectedListener listener){
        bottomNavigationView.setOnNavigationItemSelectedListener(listener);
    }

    @androidx.databinding.BindingAdapter("userInputEnabled")
    public static void setUserInputEnabled(ViewPager2 viewPager2, boolean enabled){
        viewPager2.setUserInputEnabled(enabled);
    }

    @androidx.databinding.BindingAdapter("postRunnable")
    public static void setRecyclerViewPostRunnable(RecyclerView recyclerView, Runnable runnable){
        recyclerView.post(runnable);
    }

    @androidx.databinding.BindingAdapter("onSwipeRefreshListener")
    public static void setOnRefreshListener(SwipeRefreshLayout swipeRefreshLayout, SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
    }

    @androidx.databinding.BindingAdapter("setColorScheme")
    public static void setColorScheme(SwipeRefreshLayout swipeRefreshLayout, @ColorRes int color) {
        swipeRefreshLayout.setColorSchemeColors(swipeRefreshLayout.getResources().getColor(color));
    }

    @androidx.databinding.BindingAdapter("errorStatus")
    public static void setErrorStatus(ErrorView errorView, int status){
        errorView.setStatus(status);
    }

    @androidx.databinding.BindingAdapter("errorTryAgainListener")
    public static void setErrorTryAgainListener(ErrorView errorView, ErrorView.OnTryAgainListener listener){
        errorView.setOnTryAgainListener(listener);
    }

}
