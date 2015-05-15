package com.shpark.androidnativesample;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

/**
 * Created by sh on 2015-03-12.
 */
public class NotificationBuilder {

    private String TAG = "NotificationBuilder";

    public void BuildOldNotification(Activity activity, String tickerText, String title, String message) {
        NotificationManager notificationManager = (NotificationManager)activity.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.panda, tickerText, System.currentTimeMillis());
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
        //notification.number = 1; // 미확인 알람 개수
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, new Intent(activity, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(activity, title, message, pendingIntent);
        notificationManager.notify(1234, notification);
    }

    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void BuildNotification(Activity activity, String tickerText, String title, String message) {
        NotificationManager notificationManager = (NotificationManager)activity.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, new Intent(activity, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(activity);
        builder.setSmallIcon(R.drawable.panda);
        builder.setTicker(tickerText);
        builder.setWhen(System.currentTimeMillis());
        builder.setNumber(1);
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        notificationManager.notify(111, builder.build());
    }

    // Android 4.1 (API level 15) and Lower.
    public void BuildNotificationCompat(Activity activity, String tickerText, String title, String message) {
        NotificationManager notificationManager = (NotificationManager)activity.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, new Intent(activity, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder compatBuilder = new NotificationCompat.Builder(activity);
        compatBuilder.setSmallIcon(R.drawable.panda);
        compatBuilder.setTicker(tickerText);
        compatBuilder.setWhen(System.currentTimeMillis());
        compatBuilder.setNumber(1);
        compatBuilder.setContentTitle(title);
        compatBuilder.setContentText(message);
        compatBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        compatBuilder.setContentIntent(pendingIntent);
        compatBuilder.setAutoCancel(true);
        compatBuilder.setPriority(NotificationCompat.PRIORITY_MAX);

        notificationManager.notify(111, compatBuilder.build());
    }

    public void BuildPicpictureNotification(Activity activity, String tickerText, String title, String message) {
        NotificationManager notificationManager = (NotificationManager)activity.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, new Intent(activity, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(activity);
        builder.setSmallIcon(R.drawable.panda);
        builder.setTicker(tickerText);
        builder.setWhen(System.currentTimeMillis());
        builder.setNumber(1);
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        Bitmap bigPicture = BitmapFactory.decodeResource(activity.getResources(), R.drawable.panda);

        Notification.BigPictureStyle bigPictureStyle = new Notification.BigPictureStyle(builder);
        bigPictureStyle.setBigContentTitle("Bigpicture expanded title");
        bigPictureStyle.setSummaryText("Bigpicture expanded message");
        bigPictureStyle.bigPicture(bigPicture);
        builder.setStyle(bigPictureStyle);

        notificationManager.notify(111, builder.build());
    }

    public void BuildBigTextStyle(Activity activity, String tickerText, String title, String message) {
        NotificationManager notificationManager = (NotificationManager)activity.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, new Intent(activity, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(activity);
        builder.setSmallIcon(R.drawable.panda);
        builder.setTicker(tickerText);
        builder.setWhen(System.currentTimeMillis());
        builder.setNumber(1);
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle(builder);
        bigTextStyle.setSummaryText("and More +");
        bigTextStyle.setBigContentTitle("Big text expanded title");
        bigTextStyle.bigText("Mir's IT Blog adress is \"itmir.tistory.com\"," +
                "Welcome to the Mir's Blog!! Nice to Meet you, this is Example JellyBean Notification");
        builder.setStyle(bigTextStyle);

        notificationManager.notify(111, builder.build());
    }

    public void BuildExpandedNotification(String title, String message, String ticker, int imageSource, int titleColor, int messageColor, String sound) {

    }

    private void BuildCompatNotification(Activity activity, String title, String message, String ticker, String imagePath, int titleColor, int messageColor, int soundId) {
        NotificationManager notificationManager = (NotificationManager)activity.getSystemService(Context.NOTIFICATION_SERVICE);
        //PendingIntent pendingIntent = PendingIntent.getActivities(activity.getApplicationContext(), 0, new Intent(activity, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void CreateSimpleNotification(Context context) {
        //NotificationCompat.Builder = new NotificationCompat.Builder(context);
    }
}
