package com.example.footballnewsmanager.adapters.search;

import androidx.databinding.ObservableField;

import com.example.footballnewsmanager.base.BaseAdapterViewModel;
import com.example.footballnewsmanager.models.SearchResult;

import java.util.Observable;

public class SearchAdapterViewModel {

    public ObservableField<String> imageUrl = new ObservableField<>();
    public ObservableField<String> title = new ObservableField<>();

    private SearchResult searchResult;

    public void init(SearchResult searchResult){
        this.searchResult = searchResult;
        imageUrl.set(searchResult.getImgUrl());
        title.set(searchResult.getName().length() > 70 ? searchResult.getName().substring(0,66)+"..." : searchResult.getName());
    }


    public void onClick(){
        //type
    }
}
