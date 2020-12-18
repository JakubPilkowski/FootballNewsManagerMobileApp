package com.example.footballnewsmanager.fragments.proposed_settings.others;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.helpers.ProposedLanguageDialogManager;
import com.example.footballnewsmanager.interfaces.ProposedLanguageListener;
import com.example.footballnewsmanager.models.LanguageField;

public class ProposedOthersViewModel extends BaseViewModel implements ProposedLanguageListener {

    public ObservableBoolean proposedNews = new ObservableBoolean(true);
    public ObservableField<Switch.OnCheckedChangeListener> proposedNewsChangeListenerAdapter = new ObservableField<>();
    public ObservableField<String> currentLanguage = new ObservableField<>("Polski");
    public ObservableField<Drawable> languageDrawable = new ObservableField<>();

        private Switch.OnCheckedChangeListener proposedNewsChangeListener = (buttonView, isChecked) -> proposedNews.set(isChecked);


    public void init() {
        languageDrawable.set(getActivity().getDrawable(R.drawable.ic_poland_small));
        proposedNewsChangeListenerAdapter.set(proposedNewsChangeListener);
    }

    public void languageClick() {
        ProposedLanguageDialogManager.get().show(this);
    }

    @Override
    public void onLanguageClick(LanguageField field) {
        currentLanguage.set(field.getName());
        languageDrawable.set(field.getDrawableSmall());
    }
}
