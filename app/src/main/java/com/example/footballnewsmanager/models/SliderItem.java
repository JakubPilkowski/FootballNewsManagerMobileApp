package com.example.footballnewsmanager.models;

import android.graphics.drawable.Drawable;

public class SliderItem {

    private Drawable drawable;
    private String title;

    public SliderItem(Drawable drawable, String title) {
        this.drawable = drawable;
        this.title = title;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public String getTitle() {
        return title;
    }
}
