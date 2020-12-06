package com.example.footballnewsmanager.activites;

import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.footballnewsmanager.notifications.NewsNotificationsReceiver;

public class BaseApplication extends Application {

    public static final String NEWS_NOTIFICATIONS_CHANNEL = "NewsNotificationsChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
        initAlarmManager();
    }

    private void initAlarmManager() {
        Log.d("Receiver", "init Alarm Manager");
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent newsNotificationsIntent = new Intent(this, NewsNotificationsReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 1, newsNotificationsIntent, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis()
                ,AlarmManager.INTERVAL_HALF_HOUR, alarmIntent);
    }

    private void createNotificationChannels() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel1 = new NotificationChannel(
                    NEWS_NOTIFICATIONS_CHANNEL,  "News",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("News notifications depends on chosen teams");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }
}
