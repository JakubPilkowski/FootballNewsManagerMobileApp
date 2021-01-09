package pl.android.footballnewsmanager.adapters.propsed_sites;

import android.view.View;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.example.footballnewsmanager.R;
import pl.android.footballnewsmanager.base.BaseAdapterViewModel;
import pl.android.footballnewsmanager.models.Site;

public class ProposedSitesAdapterViewModel extends BaseAdapterViewModel {

    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> imageUrl = new ObservableField<>();
    public ObservableInt background  = new ObservableInt();
    public ObservableInt visibility = new ObservableInt();
    private Site site;

    private boolean chosen = true;

    @Override
    public void init(Object[] values) {
        updateBackground();
        site = (Site) values[0];
        name.set(site.getName());
        imageUrl.set(site.getLogoUrl());
    }

    public void onClick(){
        chosen = !chosen;
        updateBackground();
    }

    private void updateBackground(){
        background.set(chosen ? R.drawable.proposed_item_active : R.drawable.proposed_item);
        visibility.set(chosen ? View.VISIBLE: View.INVISIBLE);
    }

    public Site getSite() {
        return site;
    }

    public boolean isChosen() {
        return chosen;
    }
}
