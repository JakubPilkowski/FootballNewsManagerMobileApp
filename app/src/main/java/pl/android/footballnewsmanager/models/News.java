package pl.android.footballnewsmanager.models;

import java.util.ArrayList;

public class News {

    private Long siteId;
    private Long id;
    private String title;
    private String newsUrl;
    private String imageUrl;
    private String date;
    private Site site;
    private ArrayList<NewsTag> tags;
    private Long clicks;
    private Long likes;


    public Long getSiteId() {
        return siteId;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDate() {
        return date;
    }

    public Long getLikes() {
        return likes;
    }

    public Site getSite() {
        return site;
    }

    public ArrayList<NewsTag> getTags() {
        return tags;
    }

    public Long getClicks() {
        return clicks;
    }
}
