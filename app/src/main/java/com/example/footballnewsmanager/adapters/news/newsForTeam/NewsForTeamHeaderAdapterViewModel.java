package com.example.footballnewsmanager.adapters.news.newsForTeam;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableLong;

public class NewsForTeamHeaderAdapterViewModel {

    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> img = new ObservableField<>();
    public ObservableField<String> countAll = new ObservableField<>();
    public ObservableField<String> countToday = new ObservableField<>();

    public void init(String name, String img, Long countAll, Long countToday){
        this.name.set(name);
        this.img.set(img);
        this.countAll.set("Łącznie: "+countAll);
        this.countToday.set("Dzisiaj: "+ countToday);
    }

    public void toggleFavourites(){
        //logic
    }
}
