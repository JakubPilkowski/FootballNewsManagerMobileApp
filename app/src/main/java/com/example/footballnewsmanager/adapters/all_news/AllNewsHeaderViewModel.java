package com.example.footballnewsmanager.adapters.all_news;

import androidx.databinding.ObservableField;

public class AllNewsHeaderViewModel {
    public ObservableField<String> countAll = new ObservableField<>();
    public ObservableField<String> countToday = new ObservableField<>();

    public void init(Long countAll, Long countToday) {
        this.countAll.set("Łącznie: "+countAll);
        this.countToday.set("Dzisiaj: "+ countToday);
    }
}
