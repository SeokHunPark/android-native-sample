package com.shpark.androidnativesample;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

/**
 * Created by sh on 2015-05-20.
 */

// Add to AndroidManifest.xml
// <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
public class NetworkStateUtil {

    private static String TAG = "NetworkStateUtil";

    public static void CheckNetworkState(Activity activity) {
        if (IsDataNetwork(activity)) {
            Toast.makeText(activity, "Using data network", Toast.LENGTH_SHORT).show();
        }

        if (IsWifiNetwork(activity)) {
            Toast.makeText(activity, "Using Wi-Fi network", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean IsDataNetwork(Activity activity) {
        ConnectivityManager manager = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
    }

    public static boolean IsWifiNetwork(Activity activity) {
        ConnectivityManager manager = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
    }
}
