package com.pfh.app.justweather.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefsUtils {

    public static boolean isAutoUpdate(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("AUTO_UPDATE",true);
    }

    public static void changeAutoUpdateState(Context context,boolean autoupdate){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean("AUTO_UPDATE",autoupdate).commit();
    }

    //查更新频率（分钟）
    public static int getAutoupdateTime(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("AUTO_UPDATE_Time",30);
    }

    //改变更新频率（分钟）
    public static void changeAutoupdateTime(Context context,int minutes){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt("AUTO_UPDATE_Time",minutes).commit();
    }


    public static boolean isShowNotification(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("SHOW_NOTIFICATION",true);
    }

    public static void changeNotificationState(Context context,boolean notification){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean("SHOW_NOTIFICATION",notification).commit();
    }
}
