package com.shpark.androidnativesample;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.view.View;

/**
 * Created by sh on 2015-03-06.
 */
public class SystemBarUtil {

    private static String TAG = "SystemBarUtil";

    public static void HideSystemBar(Activity activity) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB) {
            Log.d(TAG, "HoneyComb");
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            Log.d(TAG, "IceCreamSandwich");
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
            Log.d(TAG, "JellyBean");
            HideSystemBarForJellyBean(activity);
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            Log.d(TAG, "KitKat");
            HideSystemBarForKitkat(activity);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.d(TAG, "Lollipop");
            HideSystemBarForKitkat(activity);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void HideSystemBarForKitkat(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        //| View.SYSTEM_UI_FLAG_LOW_PROFILE
                        //| View.SYSTEM_UI_FLAG_IMMERSIVE
        );
    }

    // Android 4.1 (API level 15).
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private static void HideSystemBarForJellyBean(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        //| View.SYSTEM_UI_FLAG_LOW_PROFILE
        );

        ActionBar actionBar = activity.getActionBar();
        actionBar.hide();
    }

    // @Test
    // Android 4.0 (API level 14) and Higher.
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private static void DimmingSystemBarICSAndHigher(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
    }

    // Android 4.0 (API level 14) and Lower.
    private static void HideSystemBarICSAndLower(Activity activity) {

    }

    // Android 4.1 (API level 15) and Higher.
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private static void HideSystemBarJellyBeanAndHigher(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
              //  View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
              //| View.SYSTEM_UI_FLAG_FULLSCREEN
              View.SYSTEM_UI_FLAG_LOW_PROFILE
        );
    }
}
