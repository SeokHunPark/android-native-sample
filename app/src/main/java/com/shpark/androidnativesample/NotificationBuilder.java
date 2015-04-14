package com.shpark.androidnativesample;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

/**
 * Created by sh on 2015-03-12.
 */
public class NotificationBuilder {

    private String TAG = "NotificationBuilder";

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
