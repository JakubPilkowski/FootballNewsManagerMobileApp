package pl.android.footballnewsmanager.fragments.proposed_settings;

import android.content.Context;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.example.footballnewsmanager.R;
import pl.android.footballnewsmanager.adapters.LanguagesAdapter;
import pl.android.footballnewsmanager.helpers.ProposedLanguageDialogManager;
import pl.android.footballnewsmanager.interfaces.DragViewListener;
import pl.android.footballnewsmanager.interfaces.ProposedLanguageListener;
import pl.android.footballnewsmanager.models.LanguageField;

import java.util.ArrayList;
import java.util.List;

public class ProposedLanguageViewModel extends ViewModel implements DragViewListener {


    public ObservableField<DragViewListener> dragViewListenerAdapter = new ObservableField<>();
    public ObservableField<LanguagesAdapter> recyclerViewAdapterObservable = new ObservableField<>();

    public void init(ProposedLanguageListener listener, Context context) {
        dragViewListenerAdapter.set(this);
        List<LanguageField> languages = new ArrayList<>();
        languages.add(new LanguageField(context.getString(R.string.polish), context.getDrawable(R.drawable.ic_poland),
                context.getDrawable(R.drawable.ic_poland_small), "pl"));
        languages.add(new LanguageField(context.getString(R.string.english), context.getDrawable(R.drawable.ic_united_kingdom),
                context.getDrawable(R.drawable.ic_united_kingdom_small), "en"));
        languages.add(new LanguageField(context.getString(R.string.german), context.getDrawable(R.drawable.ic_germany),
                context.getDrawable(R.drawable.ic_germany_small), "de"));
        languages.add(new LanguageField(context.getString(R.string.italish), context.getDrawable(R.drawable.ic_italy),
                context.getDrawable(R.drawable.ic_italy_small), "it"));
        languages.add(new LanguageField(context.getString(R.string.spanish), context.getDrawable(R.drawable.ic_spain),
                context.getDrawable(R.drawable.ic_spain_small), "es"));
        languages.add(new LanguageField(context.getString(R.string.french), context.getDrawable(R.drawable.ic_france),
                context.getDrawable(R.drawable.ic_france_small), "fr"));
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
