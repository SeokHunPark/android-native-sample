package com.shpark.androidnativesample;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sh on 2015-05-11.
 */
public class PreferenceUtil {

    private static final String DEFAULT_PREFERENCE_NAME = "pref";
    public static final String DEFAULT_VALUE = "_DEFAULT_";

    public static String GetPreferenceString(Context context, String key){
        SharedPreferences pref = context.getSharedPreferences(DEFAULT_PREFERENCE_NAME, Context.MODE_PRIVATE);
        // 첫번째 인자는 key, 두번째 인자는 key에 대한 데이터가 존재하지 않는 경우 반환하는 디폴트 값.
        return pref.getString(key, DEFAULT_VALUE);
    }

    public static void SavePreferenceString(Context context, String key, String value){
        SharedPreferences pref = context.getSharedPreferences(DEFAULT_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void RemovePreferences(Context context, String key){
        SharedPreferences pref = context.getSharedPreferences(DEFAULT_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.commit();
    }

    public static void RemoveAllPreferences(Context context){
        SharedPreferences pref = context.getSharedPreferences(DEFAULT_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
}
