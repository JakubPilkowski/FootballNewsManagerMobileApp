package pl.android.footballnewsmanager.adapters;

import android.graphics.drawable.Drawable;

import androidx.databinding.ObservableField;

import pl.android.footballnewsmanager.base.BaseAdapterViewModel;
import pl.android.footballnewsmanager.helpers.ProposedLanguageDialogManager;
import pl.android.footballnewsmanager.interfaces.ProposedLanguageListener;
import pl.android.footballnewsmanager.models.LanguageField;

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
