package pl.android.footballnewsmanager.models;

import java.util.List;

public class News {

    private Long siteId;
    private Long id;
    private String title;
    private String newsUrl;
    private String imageUrl;
    private String date;
    private Site site;
    private List<TeamNews> teamNews;
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

    public Long getClicks() {
        return clicks;
    }

    public List<TeamNews> getTeamNews() {
        return teamNews;
    }
}
