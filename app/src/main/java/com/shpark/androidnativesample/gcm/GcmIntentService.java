package com.shpark.androidnativesample.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.shpark.androidnativesample.NotificationHelper;

/**
 * Created by sh on 2015-05-28.
 *
 <application>
 ...
 <receiver
 android:name=".GcmBroadcastReceiver"
 android:permission="com.google.android.c2dm.permission.SEND" >
 <intent-filter>
 <action android:name="com.google.android.c2dm.intent.RECEIVE" />
 <category android:name="com.shpark.androidnativesample" />
 </intent-filter>
 </receiver>
 <service android:name=".GcmIntentService" />
 ...
 </application>
 */
public class GcmIntentService extends IntentService {

    private final String TAG = "GcmIntentService";

    public GcmIntentService() {
        super("GcmIntentService");
        // TODO Auto-generated constructor stub
    }

    public GcmIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO Auto-generated method stub
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                //sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                //sendNotification("Deleted messages on server: " + extras.toString());
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.
                /*for (int i=0; i<5; i++) {
                    Log.i(TAG, "Working... " + (i+1)
                            + "/5 @ " + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }
                }
                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());*/
                // Post notification of received message.
                //sendNotification(extras);
                Log.i(TAG, "Received: " + extras.toString());
                //String command = extras.getString("fttcmd");
                long currentTime = System.currentTimeMillis();
                int index = (int) (currentTime % 1000);

                NotificationHelper notificationUtil = new NotificationHelper();
                notificationUtil.BuildNotification(this, index, "GCM test push " + String.valueOf(index), "GCM test", "GCM test message " + String.valueOf(index));
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }
}
