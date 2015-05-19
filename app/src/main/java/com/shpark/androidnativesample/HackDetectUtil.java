package com.shpark.androidnativesample;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

/**
 * Created by sh on 2015-05-19.
 */
public class HackDetectUtil {

    public static String TAG = "HackDetectUtil";

    public static final String ROOT_PATH = Environment.getExternalStorageDirectory() + "";
    public static final String ROOTING_PATH_1 = "/system/bin/su";
    public static final String ROOTING_PATH_2 = "/system/xbin/su";
    public static final String ROOTING_PATH_3 = "/system/app/SuperUser.apk";
    public static final String ROOTING_PATH_4 = "/data/data/com.noshufou.android.su";

    private static String[] RootFilesPath = new String[]{
            ROOT_PATH + ROOTING_PATH_1 ,
            ROOT_PATH + ROOTING_PATH_2 ,
            ROOT_PATH + ROOTING_PATH_3 ,
            ROOT_PATH + ROOTING_PATH_4
    };

    public static void CheckRooting(Activity activity) {
        boolean isRootingFlag = false;

        try {
            Runtime.getRuntime().exec("su");
            isRootingFlag = true;
        } catch ( Exception e) {
            isRootingFlag = false;
        }

        if(!isRootingFlag){
            isRootingFlag = CheckRootingFiles(CreateFiles(RootFilesPath));
        }

        Toast.makeText(activity.getApplicationContext(), "isRootingFlag = " + isRootingFlag, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "isRootingFlag = " + isRootingFlag);
    }

    private static File[] CreateFiles(String[] sfiles){
        File[] rootingFiles = new File[sfiles.length];
        for(int i=0 ; i < sfiles.length; i++){
            rootingFiles[i] = new File(sfiles[i]);
        }
        return rootingFiles;
    }

    private static boolean CheckRootingFiles(File... file){
        boolean result = false;
        for(File f : file){
            if(f != null && f.exists() && f.isFile()){
                result = true;
                break;
            }else{
                result = false;
            }
        }
        return result;
    }
}
