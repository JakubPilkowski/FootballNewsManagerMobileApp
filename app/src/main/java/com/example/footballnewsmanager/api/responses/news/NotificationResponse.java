package com.example.footballnewsmanager.api.responses.news;

import com.example.footballnewsmanager.models.NotificationData;

import java.util.List;

public class NotificationResponse {

    private List<NotificationData> notifications;

    public List<NotificationData> getNotifications() {
        return notifications;
    }
}
