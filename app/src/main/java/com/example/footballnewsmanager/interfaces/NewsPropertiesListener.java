package com.example.footballnewsmanager.interfaces;

import com.example.footballnewsmanager.models.News;
import com.example.footballnewsmanager.models.UserNews;

public interface NewsPropertiesListener {
    void onChange(UserNews oldNews, UserNews newNews);
}
