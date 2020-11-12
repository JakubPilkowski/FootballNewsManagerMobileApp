package com.example.footballnewsmanager.interfaces;

import com.example.footballnewsmanager.models.UserNews;

public interface NewsRecyclerViewListener {
    void onDetached();
    void backToFront();
    void onChange(UserNews oldNews, UserNews newNews);

}
