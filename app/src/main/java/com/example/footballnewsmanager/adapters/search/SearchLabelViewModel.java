package com.example.footballnewsmanager.adapters.search;

import androidx.databinding.ObservableField;

public class SearchLabelViewModel {

    public ObservableField<String> text = new ObservableField<>();

    public void init(String label){
        text.set(label);
    }
}
