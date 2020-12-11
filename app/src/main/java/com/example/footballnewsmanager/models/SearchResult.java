package com.example.footballnewsmanager.models;

public class SearchResult {
    private String name;
    private Long id;
    private SearchType type;
    private String imgUrl;

    public String getName() {
        return name;
    }


    public Long getId() {
        return id;
    }


    public SearchType getType() {
        return type;
    }


    public String getImgUrl() {
        return imgUrl;
    }
}
