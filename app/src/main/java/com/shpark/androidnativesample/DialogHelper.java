package com.shpark.androidnativesample;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by sh on 2015-03-04.
 */
public class DialogHelper {

    public static void BuildYesDialog(Context context, String title, String message) {
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);
        alt_bld.setMessage(message).setCancelable(false).setPositiveButton(
                "확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'Yes' Button
                    }
                });
        AlertDialog alert = alt_bld.create();
        alert.setTitle(title);
        alert.setIcon(R.mipmap.ic_launcher);
        alert.show();
    }

    public static void BuildNoYesDialog(Context context, String title, String message) {
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);
        alt_bld.setMessage(message).setCancelable(false).setPositiveButton(
                "확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'Yes' Button
                    }
                }).setNegativeButton(
                "취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alt_bld.create();
        alert.setTitle(title);
        alert.setIcon(R.mipmap.ic_launcher);
        alert.show();
    }
}
