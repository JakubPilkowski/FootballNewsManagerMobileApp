package com.example.footballnewsmanager.notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.activites.main.MainActivity;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.models.NotificationData;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

import static com.example.footballnewsmanager.activites.BaseApplication.NEWS_NOTIFICATIONS_CHANNEL;

public class NewsNotificationsReceiver extends BroadcastReceiver {

    private Context context;
    private Long notificationsAmount;
    @Override
    public void onReceive(Context context, Intent intent) {

        UserPreferences.init(context);
        Connection.init();
        String name = UserPreferences.get().getAuthToken();
        notificationsAmount = UserPreferences.get().getNotificationAmount();
        this.context = context;
        Connection.get().getNotifications(newsCallback, name);
    }
    private Callback<Long> newsCallback = new Callback<Long>() {

        @Override
        protected void subscribeActual(@NonNull Observer<? super Long> observer) {

        }

        @Override
        public void onSuccess(Long notifications) {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

            Intent activityIntent = new Intent(context, MainActivity.class);
            activityIntent.putExtra("restart", "restart");
            PendingIntent contentIntent = PendingIntent.getActivity(context, 2, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            if(!notificationsAmount.equals(notifications) && notifications != 0){
                Notification notification = new NotificationCompat.Builder(context, NEWS_NOTIFICATIONS_CHANNEL)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle("Dodano " + notifications + " nowych newsów!")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setColor(context.getResources().getColor(R.color.colorPrimary))
                        .setContentIntent(contentIntent)
                        .setAutoCancel(true)
                        .build();
                UserPreferences.get().changeNotification(notifications);
                notificationManagerCompat.notify(1001, notification);
            }
        }

        @Override
        public void onSmthWrong(BaseError error) {

        }
    };
}
