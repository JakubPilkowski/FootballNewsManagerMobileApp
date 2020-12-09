package com.example.footballnewsmanager.notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.media.session.MediaSessionCompat;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.activites.main.MainActivity;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.responses.news.NotificationResponse;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.models.NotificationData;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

import static com.example.footballnewsmanager.activites.BaseApplication.NEWS_NOTIFICATIONS_CHANNEL;

public class NewsNotificationsReceiver extends BroadcastReceiver {

    private Context context;
    @Override
    public void onReceive(Context context, Intent intent) {

        UserPreferences.init(context);
        Connection.init();
        String name = UserPreferences.get().getAuthToken();
        this.context = context;
        Connection.get().getNotifications(newsCallback, name);
    }
    private Callback<NotificationResponse> newsCallback = new Callback<NotificationResponse>() {

        @Override
        protected void subscribeActual(@NonNull Observer<? super NotificationResponse> observer) {

        }

        @Override
        public void onSuccess(NotificationResponse data) {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

            Intent activityIntent = new Intent(context, MainActivity.class);
            activityIntent.putExtra("restart", "restart");
            PendingIntent contentIntent = PendingIntent.getActivity(context, 2, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            for (NotificationData notificationData: data.getNotifications()) {
                if(!notificationData.getAmountBefore().equals(notificationData.getAmountAfter())) {
                    Notification notification = new NotificationCompat.Builder(context, NEWS_NOTIFICATIONS_CHANNEL)
                            .setSmallIcon(R.drawable.notification_icon)
                            .setContentTitle("Dodano " + notificationData.getAmountAfter() + " nowych newsów!")
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                            .setColor(context.getResources().getColor(R.color.colorPrimary))
                            .setContentIntent(contentIntent)
                            .setAutoCancel(true)
                            .build();
                    notificationManagerCompat.notify(1001, notification);
                }
            }
        }

        @Override
        public void onSmthWrong(BaseError error) {
        }
    };
}
