package com.example.footballnewsmanager.models;

import android.graphics.drawable.Drawable;

public class LanguageField {

    private String name;
    private Drawable drawable;
    private Drawable drawableSmall;
    private String locale;

    public LanguageField(String name, Drawable drawable, Drawable drawableSmall, String locale) {
        this.name = name;
        this.drawable = drawable;
        this.drawableSmall = drawableSmall;
        this.locale = locale;
    }

    public String getName() {
        return name;
    }

    public String getLocale() {
        return locale;
    }

    public Drawable getDrawableSmall() {
        return drawableSmall;
    }
    public Drawable getDrawable() {
        return drawable;
    }
}
