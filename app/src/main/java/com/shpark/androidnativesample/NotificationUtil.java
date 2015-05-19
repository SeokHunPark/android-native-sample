package com.shpark.androidnativesample;

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
import android.util.Log;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Created by sh on 2015-03-12.
 */
public class NotificationUtil {

    private static String TAG = "NotificationBuilder";

    public static enum NotificationStyle {
        OLD,
        NORMAL,
        BIGPICTURE,
        BIGTEXT
    }

    public void BuildExpandedNotification(Activity activity, int id, String ticker, String title, String message, String expandedTitle, String expandedMessage) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Log.d(TAG, "JellyBean and higher.");
            //BuildNotification(activity, id, ticker, title, message);
        } else {
            Log.d(TAG, "IceCreamSandwich and lower.");
            //BuildNotificationCompat(activity, id, ticker, title, message);
        }
        NotificationManager notificationManager = (NotificationManager)activity.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = CreateBuilder(activity, id, ticker, title, message);

        notificationManager.notify(id, builder.build());
    }

    private Notification.Builder CreateBuilder(Activity activity,int id,  String ticker, String title, String message) {
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, new Intent(activity, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(activity);
        builder.setSmallIcon(R.drawable.panda);
        //builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker(ticker);
        builder.setWhen(System.currentTimeMillis());
        builder.setNumber(1);
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        return builder;
    }

    private Notification.Style CreateStyle() {
        Notification.Style style = null;

        return style;
    }

    public void BuildOldNotification(Activity activity,int id,  String ticker, String title, String message) {
        NotificationManager notificationManager = (NotificationManager)activity.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.panda, ticker, System.currentTimeMillis());
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
        //notification.number = 1; // 미확인 알람 개수
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, new Intent(activity, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(activity, title, message, pendingIntent);
        notificationManager.notify(id, notification);
    }

    // Android 4.2 (API level 16) and higher.
    public void BuildNotification(Activity activity, int id, String ticker, String title, String message) {
        NotificationManager notificationManager = (NotificationManager)activity.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = CreateBuilder(activity, id, ticker, title, message);

        notificationManager.notify(id, builder.build());
    }

    // Android 4.1 (API level 15) and Lower.
    public void BuildNotificationCompat(Activity activity, int id, String ticker, String title, String message) {
        NotificationManager notificationManager = (NotificationManager)activity.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, new Intent(activity, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder compatBuilder = new NotificationCompat.Builder(activity);
        compatBuilder.setSmallIcon(R.drawable.panda);
        //compatBuilder.setSmallIcon(R.mipmap.ic_launcher);
        compatBuilder.setTicker(ticker);
        compatBuilder.setWhen(System.currentTimeMillis());
        compatBuilder.setNumber(1);
        compatBuilder.setContentTitle(title);
        compatBuilder.setContentText(message);
        compatBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        compatBuilder.setContentIntent(pendingIntent);
        compatBuilder.setAutoCancel(true);
        compatBuilder.setPriority(NotificationCompat.PRIORITY_MAX);

        notificationManager.notify(id, compatBuilder.build());
    }

    public void BuildPicpictureNotification(Activity activity, int id, String ticker, String title, String message, String expandedTitle, String expandedMessage) {
        NotificationManager notificationManager = (NotificationManager)activity.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = CreateBuilder(activity, id, ticker, title, message);

        Bitmap bigPicture = BitmapFactory.decodeResource(activity.getResources(), R.drawable.panda);

        Notification.BigPictureStyle bigPictureStyle = new Notification.BigPictureStyle(builder);
        bigPictureStyle.setBigContentTitle(expandedTitle);
        bigPictureStyle.setSummaryText(expandedMessage);
        bigPictureStyle.bigPicture(bigPicture);
        builder.setStyle(bigPictureStyle);

        notificationManager.notify(id, builder.build());
    }

    public void BuildUrlBigPictureNotification(Activity activity, int id, String ticker, String title, String message, String expandedTitle, String expandedMessage, String imageUrl) {
        NotificationManager notificationManager = (NotificationManager)activity.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = CreateBuilder(activity, id, ticker, title, message);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(activity.getApplicationContext())
                .imageDownloader(new BaseImageDownloader(activity, 5 * 1000, 10 * 1000))
                .build();

        ImageLoader.getInstance().init(config);
        ImageLoader.getInstance().loadImage(imageUrl, new SimpleImageLoadingListener() {

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                //BuildCompatBigPictureStylePush(title, message, ticker, loadedImage, soundId);
            }

            /*@Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }*/
        });

        //Bitmap bigPicture = BitmapFactory.decodeResource(activity.getResources(), R.drawable.panda);

        Notification.BigPictureStyle bigPictureStyle = new Notification.BigPictureStyle(builder);
        bigPictureStyle.setBigContentTitle(expandedTitle);
        bigPictureStyle.setSummaryText(expandedMessage);
        //bigPictureStyle.bigPicture(bigPicture);
        builder.setStyle(bigPictureStyle);

        notificationManager.notify(id, builder.build());
    }

    public void BuildBigTextNotification(Activity activity, int id, String ticker, String title, String message, String expandedTitle, String expandedMessage) {
        NotificationManager notificationManager = (NotificationManager)activity.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = CreateBuilder(activity, id, ticker, title, message);

        Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle(builder);
        bigTextStyle.setSummaryText("and More +");
        bigTextStyle.setBigContentTitle(expandedTitle);
        bigTextStyle.bigText(expandedMessage);
        builder.setStyle(bigTextStyle);

        notificationManager.notify(id, builder.build());
    }
}
