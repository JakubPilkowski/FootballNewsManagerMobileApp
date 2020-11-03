package com.example.footballnewsmanager.models;

import android.graphics.drawable.Drawable;

public class LanguageField {

    private String name;
    private Drawable drawable;
    private Drawable drawableSmall;

    public LanguageField(String name, Drawable drawable, Drawable drawableSmall) {
        this.name = name;
        this.drawable = drawable;
        this.drawableSmall = drawableSmall;
    }

    public String getName() {
        return name;
    }

    public Drawable getDrawableSmall() {
        return drawableSmall;
    }
    public Drawable getDrawable() {
        return drawable;
    }
}
