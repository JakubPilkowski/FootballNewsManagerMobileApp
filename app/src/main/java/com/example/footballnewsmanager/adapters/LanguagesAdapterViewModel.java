package com.example.footballnewsmanager.adapters;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import androidx.databinding.ObservableField;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.base.BaseAdapterViewModel;
import com.example.footballnewsmanager.helpers.ProposedLanguageDialogManager;
import com.example.footballnewsmanager.interfaces.ProposedLanguageListener;
import com.example.footballnewsmanager.models.LanguageField;

public class LanguagesAdapterViewModel extends BaseAdapterViewModel {

    private LanguageField field;
    private ProposedLanguageListener listener;
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<Drawable> drawable = new ObservableField<>();

    @Override
    public void init(Object[] values) {
        this.field = (LanguageField) values[0];
        this.listener = (ProposedLanguageListener) values[1];
        name.set(field.getName());
        drawable.set(field.getDrawable());
    }

    public void onClick(){
        listener.onLanguageClick(field);
        ProposedLanguageDialogManager.get().dismiss();
    }
}
