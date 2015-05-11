package com.shpark.androidnativesample;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

/**
 * Created by sh on 2015-05-11.
 */

// Add AndroidManifest.xml
// <uses-permission android:name="com.android.launcher.permission.INSTALL_SHORTCUT" />

public class ShortCutTool {

    public static void AddShortCut(Context context) {

        Intent shortcutIntent = new Intent();
        shortcutIntent.setAction(Intent.ACTION_MAIN);
        shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        shortcutIntent.setClassName(context, context.getClass().getName());
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Parcelable iconResource = Intent.ShortcutIconResource.fromContext(context, R.drawable.icon);

        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getResources().getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,iconResource);
        intent.putExtra("duplicate", false);
        intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        context.sendBroadcast(intent);
    }
}
