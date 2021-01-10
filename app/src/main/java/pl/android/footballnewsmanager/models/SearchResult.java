package pl.android.footballnewsmanager.models;

public class SearchResult {
    private String name;
    private String id;
    private SearchType type;
    private String imgUrl;
    private String newsUrl;
    private boolean favourite;

    public String getName() {
        return name;
    }


    public String getId() {
        return id;
    }


    public SearchType getType() {
        return type;
    }


    public String getImgUrl() {
        return imgUrl;
    }


    public String getNewsUrl() {
        return newsUrl;
    }

    public boolean isFavourite() {
        return favourite;
    }
}
