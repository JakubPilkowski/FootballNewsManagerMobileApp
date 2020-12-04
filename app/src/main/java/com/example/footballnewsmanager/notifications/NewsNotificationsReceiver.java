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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.example.footballnewsmanager.activites.BaseApplication.CHANNEL_1_ID;

public class NewsNotificationsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Receiver", "NewsNotifications");
        new sendNotification(context).execute("");
    }


    private static class sendNotification extends AsyncTask<String, Void, Bitmap> {

        private Context ctx;

        sendNotification(Context context) {
            super();
            this.ctx = context;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
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

        @Override
        protected void onPostExecute(Bitmap bitmap) {

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
