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
    private MediaSessionCompat mediaSession;

    @Override
    public void onReceive(Context context, Intent intent) {

        UserPreferences.init(context);
        Connection.init();
        String name = UserPreferences.get().getAuthToken();
        this.context = context;
        Connection.get().getNotifications(newsCallback, name);
    }
    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
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
                Notification notification = new NotificationCompat.Builder(context, NEWS_NOTIFICATIONS_CHANNEL)
                                .setSmallIcon(R.drawable.notification_icon)
                                .setContentTitle("Nowe newsy (" + notificationData.getAmountAfter() + ")")
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                .setColor(context.getResources().getColor(R.color.colorPrimary))
                                .setContentIntent(contentIntent)
                                .setAutoCancel(true)
                                .build();

                notificationManagerCompat.notify(notificationData.getTeam().getId().intValue(), notification);
//                if(!notificationData.getAmountBefore().equals(notificationData.getAmountAfter())) {
//                    try {
//                        URL url = new URL(notificationData.getTeam().getLogoUrl());
//                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                        connection.setDoInput(true);
//                        connection.connect();
//                        InputStream in = connection.getInputStream();
//                        Notification notification = new NotificationCompat.Builder(context, NEWS_NOTIFICATIONS_CHANNEL)
//                                .setSmallIcon(R.drawable.notification_icon)
//                                .setLargeIcon(BitmapFactory.decodeStream(in))
//                                .setContentTitle("Nowe newsy (" + notificationData.getAmountAfter() + ") dla " + notificationData.getTeam().getName())
//                                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                                .setColor(context.getResources().getColor(R.color.colorPrimary))
//                                .setContentIntent(contentIntent)
//                                .setAutoCancel(true)
//                                .setOnlyAlertOnce(true)
//                                .build();
//                        notificationManagerCompat.notify(notificationData.getTeam().getId().intValue(), notification);
//                    } catch (MalformedURLException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
            }
        }

        @Override
        public void onSmthWrong(BaseError error) {
        }
    };
}
