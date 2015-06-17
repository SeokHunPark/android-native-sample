package com.shpark.androidnativesample.gcm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

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
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        // Explicitly specify that GcmIntentService will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(), GcmIntentService.class.getName());

        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}
