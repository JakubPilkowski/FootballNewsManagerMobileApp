package com.example.footballnewsmanager.helpers;

import android.content.res.Resources;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.models.Language;

public class LanguageHelper {
    public static Language getLanguage(String language, Resources resources){
        String polish = resources.getString(R.string.polish);
        String english = resources.getString(R.string.english);
        String german = resources.getString(R.string.german);
        String french = resources.getString(R.string.french);
        String italish = resources.getString(R.string.italish);
        String spanish = resources.getString(R.string.spanish);
        if (language.equals(polish)) {
            return Language.POLSKI;
        } else if (language.equals(english)) {
            return Language.ANGIELSKI;
        } else if (language.equals(german)) {
            return Language.NIEMIECKI;
        } else if (language.equals(french)) {
            return Language.FRANCUSKI;
        } else if (language.equals(italish)) {
            return Language.WŁOSKI;
        } else if (language.equals(spanish)) {
            return Language.HISZPAŃSKI;
        } else{
            return Language.POLSKI;
        }
    }
}
