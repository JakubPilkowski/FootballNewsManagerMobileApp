package com.example.footballnewsmanager.api.responses.profile;

import com.example.footballnewsmanager.models.User;

public class UserProfileResponse {

    private User user;
    private Long likes;

    public User getUser() {
        return user;
    }

    public Long getLikes() {
        return likes;
    }
}
