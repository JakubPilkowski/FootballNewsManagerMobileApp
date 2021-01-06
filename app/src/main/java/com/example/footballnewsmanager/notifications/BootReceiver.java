package com.example.footballnewsmanager.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent newsNotificationsIntent = new Intent(context, NewsNotificationsReceiver.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, newsNotificationsIntent, 0);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+5000,5*1000, alarmIntent);
        }
    }
}
