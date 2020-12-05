package com.example.footballnewsmanager.notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

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

import static com.example.footballnewsmanager.activites.BaseApplication.CHANNEL_1_ID;

public class NewsNotificationsReceiver extends BroadcastReceiver {

    public static final String TAG = "NewsNotifiReceiver";

    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Receiver", "onReceive");

        UserPreferences.init(context);
        Connection.init();
        String name = UserPreferences.get().getAuthToken();
//        new sendNotification(context).execute("name");
        this.context = context;
        Connection.get().getNotifications(newsCallback, name);
    }


    private Callback<NotificationResponse> newsCallback = new Callback<NotificationResponse>() {

        @Override
        protected void subscribeActual(@NonNull Observer<? super NotificationResponse> observer) {

        }

        @Override
        public void onSuccess(NotificationResponse data) {
            Log.d(TAG, "onSuccess: ");
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

            Intent activityIntent = new Intent(context, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 2, activityIntent, 0);

            for (NotificationData notificationData: data.getNotifications()) {
                if(!notificationData.getAmountBefore().equals(notificationData.getAmountAfter())) {
                    try {
                        URL url = new URL(notificationData.getTeam().getLogoUrl());
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream in = connection.getInputStream();
                        Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                                .setSmallIcon(R.drawable.notification_icon)
                                .setLargeIcon(BitmapFactory.decodeStream(in))
                                .setContentTitle("Nowe newsy (" + notificationData.getAmountAfter() + ") dla " + notificationData.getTeam().getName())
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                .setColor(context.getResources().getColor(R.color.colorPrimary))
                                .setContentIntent(contentIntent)
                                .setAutoCancel(true)
                                .setOnlyAlertOnce(true)
                                .build();
                        notificationManagerCompat.notify(notificationData.getTeam().getId().intValue(), notification);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        public void onSmthWrong(BaseError error) {
            Log.d(TAG, "onSmthWrong");
        }
    };

    private static class sendNotification extends AsyncTask<String, Void, Bitmap> {

        private Context ctx;

        sendNotification(Context context) {
            super();
            this.ctx = context;
            Log.d("Receiver", "onReceive");
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Log.d("Receiver", "onReceive");
            try {

                URL url = new URL("        https://www.football-italia.net/sites/default/files/imagecache/main_photo/[type]/[nid]/Hickey_Soriano_Bologna_2012.jpg");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream in = connection.getInputStream();
                return BitmapFactory.decodeStream(in);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

//        @Override
//        protected Bitmap doInBackground(News... news) {
//            return null;
//        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Log.d("Receiver", "onReceive");

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(ctx);

            Intent activityIntent = new Intent(ctx, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(ctx, 2, activityIntent, 0);

            Notification notification = new NotificationCompat.Builder(ctx, CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.notification_icon)
                    .setLargeIcon(bitmap)
//                    .setStyle(new NotificationCompat.BigTextStyle().bigText("qweqweqweqweqweqweqweqweqweqeqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqweqw")
//                            .setBigContentTitle("Big content title")
//                            .setSummaryText("Summary")
//                    )
                    .setContentTitle("Przykładowe powiadomienie")
                    .setContentText("jakiś tam tekscik na szybko")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setColor(ctx.getResources().getColor(R.color.colorPrimary))
                    .setContentIntent(contentIntent)
                    .setAutoCancel(true)
                    .setOnlyAlertOnce(true)
//                    .addAction(R.mipmap.ic_launcher, "Toast", actionIntent)
                    .build();
            notificationManagerCompat.notify(1, notification);
        }
    }
}
