package com.example.footballnewsmanager.activites;

import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import com.example.footballnewsmanager.notifications.NewsNotificationsReceiver;

import java.util.Calendar;

public class BaseApplication extends Application {

    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";

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
        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis()
                ,60000, alarmIntent);
    }

    private void createNotificationChannels() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,  "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is Channel 1");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }
}
