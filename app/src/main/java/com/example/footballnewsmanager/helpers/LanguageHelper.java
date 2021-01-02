package com.example.footballnewsmanager.helpers;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.models.Language;

public class LanguageHelper {

    public static String nameToLocale(String name, Resources resources) {
        String polish = resources.getString(R.string.polish);
        String english = resources.getString(R.string.english);
        String german = resources.getString(R.string.german);
        String french = resources.getString(R.string.french);
        String italish = resources.getString(R.string.italish);
        String spanish = resources.getString(R.string.spanish);
        if (name.equals(polish)) {
            return "pl";
        } else if (name.equals(english)) {
            return "en";
        } else if (name.equals(german)) {
            return "de";
        } else if (name.equals(french)) {
            return "fr";
        } else if (name.equals(italish)) {
            return "it";
        } else if (name.equals(spanish)) {
            return "es";
        } else {
            return "en";
        }
    }

    public static String getName(String locale, Resources resources) {
        switch (locale) {
            case "pl":
                return resources.getString(R.string.polish);
            case "it":
                return resources.getString(R.string.italish);
            case "fr":
                return resources.getString(R.string.french);
            case "de":
                return resources.getString(R.string.german);
            case "es":
                return resources.getString(R.string.spanish);
            case "en":
            default:
                return resources.getString(R.string.english);
        }
    }

    public static Drawable getDrawable(String locale, Resources resources){
        switch (locale) {
            case "pl":
                return resources.getDrawable(R.drawable.ic_poland_small);
            case "it":
                return resources.getDrawable(R.drawable.ic_italy_small);
            case "fr":
                return resources.getDrawable(R.drawable.ic_france_small);
            case "de":
                return resources.getDrawable(R.drawable.ic_germany_small);
            case "es":
                return resources.getDrawable(R.drawable.ic_spain_small);
            case "en":
            default:
                return resources.getDrawable(R.drawable.ic_united_kingdom_small);
        }
    }
}
