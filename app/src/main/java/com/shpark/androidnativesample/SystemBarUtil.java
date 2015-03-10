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

    private String TAG = "SystemBarUtil";

    public void hideSystemBar(Activity activity) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB) {
            Log.d(TAG, "HoneyComb");
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            Log.d(TAG, "IceCreamSandwich");
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
            Log.d(TAG, "JellyBean");
            hideSystemBarForJellyBean(activity);
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            Log.d(TAG, "KitKat");
            hideSystemBarForKitkat(activity);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.d(TAG, "Lollipop");
            hideSystemBarForKitkat(activity);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void hideSystemBarForKitkat(Activity activity) {
		/*Window w = getWindow();
		w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);*/

		/*int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
		uiOptions ^= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
		uiOptions ^= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
		uiOptions ^= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
		uiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
		uiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
		uiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;*/
        View decorView = activity.getWindow().getDecorView();
        //decorView.setSystemUiVisibility(uiOptions);

        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        //| View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void hideSystemBarForJellyBean(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        //| View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );

        ActionBar actionBar = activity.getActionBar();
        actionBar.hide();
    }
}
