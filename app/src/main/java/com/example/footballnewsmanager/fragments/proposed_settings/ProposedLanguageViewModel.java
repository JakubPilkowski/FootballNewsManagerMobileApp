package com.example.footballnewsmanager.fragments.proposed_settings;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.adapters.LanguagesAdapter;
import com.example.footballnewsmanager.helpers.DragView;
import com.example.footballnewsmanager.helpers.ProposedLanguageDialogManager;
import com.example.footballnewsmanager.interfaces.DragViewListener;
import com.example.footballnewsmanager.interfaces.ProposedLanguageListener;
import com.example.footballnewsmanager.models.LanguageField;

import java.util.ArrayList;
import java.util.List;

public class ProposedLanguageViewModel extends ViewModel implements DragViewListener {


    private ProposedLanguageListener listener;
    public ObservableField<DragViewListener> dragViewListenerAdapter = new ObservableField<>();
    public ObservableField<LanguagesAdapter> recyclerViewAdapterObservable = new ObservableField<>();

    public void init(ProposedLanguageListener listener, Context context) {
        this.listener = listener;
        dragViewListenerAdapter.set(this);
        List<LanguageField> languages = new ArrayList<>();
        languages.add(new LanguageField(context.getString(R.string.polish), context.getDrawable(R.drawable.ic_poland),
                context.getDrawable(R.drawable.ic_poland_small)));
        languages.add(new LanguageField(context.getString(R.string.english), context.getDrawable(R.drawable.ic_united_kingdom),
                context.getDrawable(R.drawable.ic_united_kingdom_small)));
        languages.add(new LanguageField(context.getString(R.string.german), context.getDrawable(R.drawable.ic_germany),
                context.getDrawable(R.drawable.ic_germany_small)));
        languages.add(new LanguageField(context.getString(R.string.italish), context.getDrawable(R.drawable.ic_italy),
                context.getDrawable(R.drawable.ic_italy_small)));
        languages.add(new LanguageField(context.getString(R.string.spanish), context.getDrawable(R.drawable.ic_spain),
                context.getDrawable(R.drawable.ic_spain_small)));
        languages.add(new LanguageField(context.getString(R.string.french), context.getDrawable(R.drawable.ic_france),
                context.getDrawable(R.drawable.ic_france_small)));
        LanguagesAdapter languagesAdapter = new LanguagesAdapter();
        languagesAdapter.setListener(listener);
        languagesAdapter.setItems(languages);
        recyclerViewAdapterObservable.set(languagesAdapter);
    }


    @Override
    public void onClose() {
        ProposedLanguageDialogManager.get().dismiss();
    }
}
