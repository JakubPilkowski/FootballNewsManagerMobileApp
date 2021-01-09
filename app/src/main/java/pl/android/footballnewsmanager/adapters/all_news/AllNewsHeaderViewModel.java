package pl.android.footballnewsmanager.adapters.all_news;

import android.app.Activity;
import android.content.res.Resources;

import androidx.databinding.ObservableField;

import com.example.footballnewsmanager.R;

public class AllNewsHeaderViewModel {
    public ObservableField<String> countAll = new ObservableField<>();
    public ObservableField<String> countToday = new ObservableField<>();

    public void init(Long countAll, Long countToday, Activity activity) {
        Resources resources = activity.getResources();
        this.countAll.set(resources.getString(R.string.total)+countAll);
        this.countToday.set(resources.getString(R.string.today)+ countToday);
    }
}
